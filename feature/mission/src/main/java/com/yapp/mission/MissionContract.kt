package com.yapp.mission

sealed class MissionContract {

    data class State(
        val showOverlayText: Boolean = false,
        val showOverlay: Boolean = true,
        val missionProgress: Int = 0,
        val isMissionCompleted: Boolean = false,
        val isFlipped: Boolean = false,
        val shakeCount: Int = 0,
        val rotationY: Float = 0f,
        val rotationZ: Float = 0f,
        val showExitDialog: Boolean = false,
        val errorMessage: String? = null,
    ) : com.yapp.ui.base.UiState

    sealed class Action {
        data object NextStep : Action()
        data object PreviousStep : Action()
        object StartOverlayTimer : Action()
        object ShakeCard : Action()
        object ClickCard : Action()
        object ShowExitDialog : Action()
        object HideExitDialog : Action()
        object RetryPostFortune : Action()
    }

    sealed class SideEffect : com.yapp.ui.base.SideEffect {
        data class Navigate(
            val route: String,
            val popUpTo: String? = null,
            val inclusive: Boolean = false,
        ) : SideEffect()

        data object NavigateBack : SideEffect()
    }
}
