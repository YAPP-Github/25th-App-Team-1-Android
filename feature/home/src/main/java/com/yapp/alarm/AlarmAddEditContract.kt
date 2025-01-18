package com.yapp.alarm

import com.yapp.ui.base.UiState

sealed class AlarmAddEditContract {

    data class State(
        val currentAmPm: String = "오전",
        val currentHour: Int = 6,
        val currentMinute: Int = 0,
        val alarmMessage: String = "",
        val days: Set<AlarmDay> = enumValues<AlarmDay>().toSet(),
        val isWeekdaysChecked: Boolean = false,
        val isWeekendsChecked: Boolean = false,
        val selectedDays: Set<AlarmDay> = setOf(),
        val isDisableHolidayEnabled: Boolean = false,
        val isDisableHolidayChecked: Boolean = false,
    ) : UiState

    sealed class Action {
        data class UpdateAlarmTime(val amPm: String, val hour: Int, val minute: Int) : Action()
        data object ToggleWeekdaysChecked : Action()
        data object ToggleWeekendsChecked : Action()
        data class ToggleDaySelection(val day: AlarmDay) : Action()
        data object ToggleDisableHolidayChecked : Action()
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
