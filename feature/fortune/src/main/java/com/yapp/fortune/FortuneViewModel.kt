package com.yapp.fortune

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.yapp.common.navigation.destination.FortuneDestination
import com.yapp.datastore.UserPreferences
import com.yapp.domain.repository.FortuneRepository
import com.yapp.fortune.page.toFortunePages
import com.yapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

@HiltViewModel
class FortuneViewModel @Inject constructor(
    private val fortuneRepository: FortuneRepository,
    private val userPreferences: UserPreferences,
) : BaseViewModel<FortuneContract.State, FortuneContract.SideEffect>(
    FortuneContract.State(),
) {

    init {
        viewModelScope.launch {
            val fortuneId = userPreferences.fortuneIdFlow.firstOrNull()
            fortuneId?.let { getFortune(it) }
        }
    }

    private fun getFortune(fortuneId: Long) = intent {
        updateState { copy(isLoading = true) }

        fortuneRepository.getFortune(fortuneId).onSuccess { fortune ->
            updateState {
                copy(
                    isLoading = false,
                    dailyFortune = fortune.dailyFortune,
                    fortunePages = fortune.toFortunePages(),
                )
            }
        }.onFailure { error ->
            Log.e("FortuneViewModel", "운세 데이터 요청 실패: ${error.message}")
            updateState { copy(isLoading = false) }
        }
    }

    fun onAction(action: FortuneContract.Action) = intent {
        when (action) {
            is FortuneContract.Action.NextStep -> {
                if (state.hasReward) {
                    postSideEffect(
                        FortuneContract.SideEffect.Navigate(
                            route = FortuneDestination.Reward.route,
                            popUpTo = FortuneDestination.Fortune.route,
                            inclusive = true,
                        ),
                    )
                } else {
                    reduce { state.copy(currentStep = (state.currentStep + 1).coerceAtMost(5)) }
                }
            }
        }
    }

    fun setHasReward(hasReward: Boolean) = updateState {
        copy(hasReward = hasReward)
    }
}
