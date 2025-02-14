package com.yapp.fortune

import com.yapp.fortune.page.FortunePageData

sealed class FortuneContract {
    data class State(
        val isLoading: Boolean = true,
        val currentStep: Int = 0,
        val hasReward: Boolean = true,
        val dailyFortuneTitle: String = "",
        val dailyFortuneDescription: String = "",
        val avgFortuneScore: Int = 0,
        val fortunePages: List<FortunePageData> = emptyList(),
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
