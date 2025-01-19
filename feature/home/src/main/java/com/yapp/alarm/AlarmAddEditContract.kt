package com.yapp.alarm

import com.yapp.ui.base.UiState

sealed class AlarmAddEditContract {

    data class State(
        val timeState: AlarmTimeState = AlarmTimeState(),
        val daySelectionState: AlarmDaySelectionState = AlarmDaySelectionState(),
        val holidayState: AlarmHolidayState = AlarmHolidayState(),
        val snoozeState: AlarmSnoozeState = AlarmSnoozeState(),
    ) : UiState

    data class AlarmTimeState(
        val currentAmPm: String = "오전",
        val currentHour: Int = 1,
        val currentMinute: Int = 0,
        val alarmMessage: String = "",
    )

    data class AlarmDaySelectionState(
        val days: Set<AlarmDay> = enumValues<AlarmDay>().toSet(),
        val isWeekdaysChecked: Boolean = false,
        val isWeekendsChecked: Boolean = false,
        val selectedDays: Set<AlarmDay> = setOf(),
    )

    data class AlarmHolidayState(
        val isDisableHolidayEnabled: Boolean = false,
        val isDisableHolidayChecked: Boolean = false,
    )

    data class AlarmSnoozeState(
        val isSnoozeEnabled: Boolean = true,
        val snoozeIntervalIndex: Int = 2,
        val snoozeCountIndex: Int = 1,
        val isBottomSheetOpen: Boolean = false,
        val snoozeIntervals: List<String> = listOf("1분", "3분", "5분", "10분", "15분"),
        val snoozeCounts: List<String> = listOf("1회", "3회", "5회", "10회", "무한"),
    )

    sealed class Action {
        data object ClickBack : Action()
        data object ClickSave : Action()
        data class UpdateAlarmTime(val amPm: String, val hour: Int, val minute: Int) : Action()
        data object ToggleWeekdaysChecked : Action()
        data object ToggleWeekendsChecked : Action()
        data class ToggleDaySelection(val day: AlarmDay) : Action()
        data object ToggleDisableHolidayChecked : Action()
        data object ToggleSnoozeSettingBottomSheetOpen : Action()
        data object ToggleSnoozeEnabled : Action()
        data class UpdateSnoozeInterval(val index: Int) : Action()
        data class UpdateSnoozeCount(val index: Int) : Action()
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
