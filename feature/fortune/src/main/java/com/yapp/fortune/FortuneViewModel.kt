package com.yapp.fortune

import com.yapp.common.navigation.destination.FortuneDestination
import com.yapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

@HiltViewModel
class FortuneViewModel @Inject constructor() : BaseViewModel<FortuneContract.State, FortuneContract.SideEffect>(
    FortuneContract.State(),
) {
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

    // 보상 검증 해야됨
    fun setHasReward(hasReward: Boolean) = updateState {
        copy(hasReward = hasReward)
    }
}
