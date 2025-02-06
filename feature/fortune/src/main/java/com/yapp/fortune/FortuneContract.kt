package com.yapp.fortune

import FortunePagesProvider
import com.yapp.fortune.page.FortunePageData

sealed class FortuneContract {

    data class State(
        val currentStep: Int = 0,
        val hasReward: Boolean = true,
        val fortunePages: List<FortunePageData> = FortunePagesProvider.fortunePages,
    ) : com.yapp.ui.base.UiState

    sealed class Action {
        data object NextStep : Action()
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
