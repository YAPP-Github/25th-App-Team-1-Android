package com.yapp.alarm

import com.yapp.ui.base.UiState

class AlarmActionContract {

    data class State(
        val isAm: Boolean = true,
        val hour: Int = 0,
        val minute: Int = 0,
        val todayDate: String = "",
        val snoozeInterval: Int = 1,
        val snoozeCount: Int = 1,
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
