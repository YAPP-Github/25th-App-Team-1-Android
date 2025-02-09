package com.yapp.alarm.action

import com.yapp.ui.base.UiState

class AlarmActionContract {

    data class State(
        val initialLoading: Boolean = true,
        val isAm: Boolean = true,
        val hour: Int = 0,
        val minute: Int = 0,
        val todayDate: String = "",
        val snoozeInterval: Int = 5,
        val snoozeCount: Int = 5,
    ) : UiState

    sealed class Action {
        data object Snooze : Action()
        data object Dismiss : Action()
    }

    sealed class SideEffect : com.yapp.ui.base.SideEffect {
        data class Navigate(
            val route: String,
            val popUpTo: String? = null,
            val inclusive: Boolean = false,
        ) : SideEffect()
    }
}
