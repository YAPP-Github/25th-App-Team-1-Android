package com.yapp.mission

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.yapp.common.navigation.destination.FortuneDestination
import com.yapp.common.navigation.destination.MissionDestination
import com.yapp.datastore.UserPreferences
import com.yapp.domain.repository.FortuneRepository
import com.yapp.media.haptic.HapticFeedbackManager
import com.yapp.media.haptic.HapticType
import com.yapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.orbitmvi.orbit.syntax.simple.intent
import javax.inject.Inject

@HiltViewModel
class MissionViewModel @Inject constructor(
    private val hapticFeedbackManager: HapticFeedbackManager,
    private val fortuneRepository: FortuneRepository,
    private val userPreferences: UserPreferences,
) : BaseViewModel<MissionContract.State, MissionContract.SideEffect>(
    MissionContract.State(),
) {

    fun onAction(action: MissionContract.Action) = intent {
        when (action) {
            is MissionContract.Action.NextStep -> {
                emitSideEffect(
                    MissionContract.SideEffect.Navigate(route = MissionDestination.Progress.route),
                )
            }

            is MissionContract.Action.PreviousStep -> {
                emitSideEffect(MissionContract.SideEffect.NavigateBack)
            }

            is MissionContract.Action.StartOverlayTimer -> startOverlayTimer()

            is MissionContract.Action.ShakeCard, is MissionContract.Action.ClickCard -> handleIncreaseCount()

            is MissionContract.Action.ShowExitDialog -> updateState { copy(showExitDialog = true) }
            is MissionContract.Action.HideExitDialog -> updateState { copy(showExitDialog = false) }
        }
    }

    private fun handleIncreaseCount() = intent {
        if (currentState.isAnimating) return@intent

        val currentCount = currentState.clickCount
        if (currentCount < 9) {
            hapticFeedbackManager.performHapticFeedback(HapticType.SUCCESS)
            updateState { copy(clickCount = currentCount + 1, isAnimating = true) }
            kotlinx.coroutines.delay(500)
            updateState { copy(isAnimating = false) }
        } else if (currentCount == 9 && !currentState.isFlipped) {
            hapticFeedbackManager.performHapticFeedback(HapticType.SUCCESS)
            postFortune()
            updateState {
                copy(
                    isMissionCompleted = true,
                    clickCount = 10,
                    isFlipped = true,
                    isAnimating = true,
                )
            }
            kotlinx.coroutines.delay(500)
            updateState { copy(isAnimating = false) }
        }
    }

    private fun postFortune() {
        viewModelScope.launch {
            val userId = userPreferences.userIdFlow.firstOrNull() ?: return@launch
            val fortuneResult = runCatching {
                withContext(Dispatchers.IO) {
                    fortuneRepository.postFortune(userId)
                }
            }
            fortuneResult.onSuccess { fortune ->
                val fortuneData = fortune.getOrThrow()
                userPreferences.saveFortuneId(fortuneData.id)

                emitSideEffect(
                    MissionContract.SideEffect.Navigate(
                        route = FortuneDestination.Route.route,
                        popUpTo = MissionDestination.Route.route,
                        inclusive = true,
                    ),
                )
            }.onFailure { error ->
                Log.e("MissionViewModel", "운세 데이터 요청 실패: ${error.message}")
            }
        }
    }

    private suspend fun startOverlayTimer() {
        updateState { copy(showOverlay = true) }
        kotlinx.coroutines.delay(1000)
        updateState { copy(showOverlayText = true) }
        kotlinx.coroutines.delay(2000)
        updateState { copy(showOverlay = false, showOverlayText = false) }
    }
}
