package com.yapp.alarm

import com.yapp.ui.base.UiState

sealed class AlarmAddEditContract {

    data class State(
        val timeState: AlarmTimeState = AlarmTimeState(), // 알람 시간 관련 상태
        val daySelectionState: AlarmDaySelectionState = AlarmDaySelectionState(), // 요일 선택 상태
        val holidayState: AlarmHolidayState = AlarmHolidayState(), // 휴일 관련 상태
        val snoozeState: AlarmSnoozeState = AlarmSnoozeState(), // 스누즈 관련 상태
    ) : UiState

    data class AlarmTimeState(
        val currentAmPm: String = "오전",
        val currentHour: Int = 6,
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
        val isSnoozeEnabled: Boolean = true, // 스누즈 활성화 여부
        val snoozeIntervalIndex: Int = 2, // 선택된 간격 인덱스
        val snoozeCountIndex: Int = 1, // 선택된 횟수 인덱스
    )

    sealed class Action {
        data object ClickBack : Action()
        data object ClickSave : Action()
        data class UpdateAlarmTime(val amPm: String, val hour: Int, val minute: Int) : Action()
        data object ToggleWeekdaysChecked : Action()
        data object ToggleWeekendsChecked : Action()
        data class ToggleDaySelection(val day: AlarmDay) : Action()
        data object ToggleDisableHolidayChecked : Action()
        data object OpenSnoozeSettingBottomSheet : Action()
        data object ToggleSnoozeEnabled : Action() // 스누즈 활성화 상태 토글
        data class UpdateSnoozeInterval(val index: Int) : Action() // 간격 변경
        data class UpdateSnoozeCount(val index: Int) : Action() // 횟수 변경
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
