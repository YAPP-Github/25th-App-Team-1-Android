package com.yapp.mission

import com.yapp.common.navigation.destination.FortuneDestination
import com.yapp.common.navigation.destination.MissionDestination
import com.yapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import javax.inject.Inject

@HiltViewModel
class MissionViewModel @Inject constructor() : BaseViewModel<MissionContract.State, MissionContract.SideEffect>(
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

            is MissionContract.Action.CompleteMission -> {
                emitSideEffect(
                    MissionContract.SideEffect.Navigate(
                        route = FortuneDestination.Route.route,
                        popUpTo = MissionDestination.Route.route,
                        inclusive = true,
                    ),
                )
            }

            is MissionContract.Action.StartOverlayTimer -> startOverlayTimer()

            is MissionContract.Action.ShakeCard -> handleIncreaseCount()

            is MissionContract.Action.ShowExitDialog -> updateState { copy(showExitDialog = true) }
            is MissionContract.Action.HideExitDialog -> updateState { copy(showExitDialog = false) }
        }
    }

    private fun handleIncreaseCount() = intent {
        if (currentState.isAnimating) return@intent

        val currentCount = currentState.clickCount
        if (currentCount < 9) {
            updateState { copy(clickCount = currentCount + 1, isAnimating = true) }
            kotlinx.coroutines.delay(500)
            updateState { copy(isAnimating = false) }
        } else if (currentCount == 9 && !currentState.isFlipped) {
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

    private suspend fun startOverlayTimer() {
        updateState { copy(showOverlay = true) }
        kotlinx.coroutines.delay(1000)
        updateState { copy(showOverlayText = true) }
        kotlinx.coroutines.delay(2000)
        updateState { copy(showOverlay = false, showOverlayText = false) }
    }
}
