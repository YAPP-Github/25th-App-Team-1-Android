package com.yapp.alarm

import com.yapp.ui.base.UiState

sealed class AlarmAddEditContract {

    data class State(
        val currentAmPm: String = "오전",
        val currentHour: Int = 1,
        val currentMinute: Int = 0,
        val isWeekdaysChecked: Boolean = false,
        val isWeekendsChecked: Boolean = false,
        val selectedDays: List<String> = emptyList(),
        val isDisableHolidayChecked: Boolean = false,
    ) : UiState

    sealed class Action {
        data class UpdateAlarmTime(
            val amPm: String,
            val hour: Int,
            val minute: Int
        ) : Action()
        data class UpdateWeekdaysChecked(val isChecked: Boolean) : Action()
        data class UpdateWeekendsChecked(val isChecked: Boolean) : Action()
        data class UpdateSelectedDays(val selectedDays: List<String>) : Action()
        data class UpdateDisableHolidayChecked(val isChecked: Boolean) : Action()
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
