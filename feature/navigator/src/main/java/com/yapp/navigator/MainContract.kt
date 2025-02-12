package com.yapp.navigator

import com.yapp.ui.base.UiState

sealed class MainContract {
    data class State(
        val userId: Long? = null,
        val onboardingCompleted: Boolean = false,
        val isLoading: Boolean = true,
    ) : UiState

    sealed class SideEffect : com.yapp.ui.base.SideEffect
}
