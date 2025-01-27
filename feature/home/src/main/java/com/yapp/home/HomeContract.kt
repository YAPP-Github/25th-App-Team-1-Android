package com.yapp.home

import com.yapp.domain.model.Alarm
import com.yapp.ui.base.UiState

sealed class HomeContract {

    data class State(
        val alarms: List<Alarm> = listOf(Alarm()),
        val lastFortuneScore: Int = 50,
        val deliveryTime: String = "2025-01-28T22:00",
        val name: String = "동현",
    ) : UiState

    sealed class Action

    sealed class SideEffect : com.yapp.ui.base.SideEffect {
        data class Navigate(
            val route: String,
            val popUpTo: String? = null,
            val inclusive: Boolean = false,
        ) : SideEffect()

        data object NavigateBack : SideEffect()
    }
}
