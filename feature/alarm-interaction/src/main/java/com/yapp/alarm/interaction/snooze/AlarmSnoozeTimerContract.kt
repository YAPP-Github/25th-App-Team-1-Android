package com.yapp.alarm.interaction.snooze

import com.yapp.ui.base.UiState

class AlarmSnoozeTimerContract {

    data class State(
        val initialLoading: Boolean = true,
        val alarmTimeStamp: Long = 0L,
        val remainingSeconds: Int = 1,
        val totalSeconds: Int = 300,
    ) : UiState

    sealed class Action {
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
