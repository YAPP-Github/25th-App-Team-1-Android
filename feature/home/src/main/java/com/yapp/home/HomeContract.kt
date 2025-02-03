package com.yapp.home

import com.yapp.domain.model.Alarm
import com.yapp.domain.model.AlarmDay
import com.yapp.domain.model.toRepeatDays
import com.yapp.ui.base.UiState

sealed class HomeContract {

    data class State(
        val alarms: List<Alarm> = listOf(
            Alarm(
                id = 0,
                repeatDays = listOf(AlarmDay.MON, AlarmDay.TUE, AlarmDay.WED, AlarmDay.FRI).toRepeatDays(),
            ),
            Alarm(
                id = 1,
                repeatDays = listOf(AlarmDay.SUN, AlarmDay.SAT).toRepeatDays(),
            ),
            Alarm(
                id = 2,
                repeatDays = listOf(AlarmDay.MON, AlarmDay.TUE, AlarmDay.WED, AlarmDay.THU, AlarmDay.FRI).toRepeatDays(),
            ),
            Alarm(
                id = 3,
                repeatDays = listOf(AlarmDay.SUN, AlarmDay.SAT).toRepeatDays(),
            ),
            Alarm(
                id = 4,
                repeatDays = listOf(AlarmDay.WED, AlarmDay.THU).toRepeatDays(),
            ),
        ),
        val dropdownMenuExpanded: Boolean = false,
        val selectedAlarmIds: Set<Long> = emptySet(),
        val isSelectionMode: Boolean = false,
        val isDeleteDialogVisible: Boolean = false,
        val lastFortuneScore: Int = 50,
        val deliveryTime: String = "2025-02-01T22:00",
        val name: String = "동현",
    ) : UiState {
        val isAllSelected: Boolean
            get() = alarms.isNotEmpty() && selectedAlarmIds.size == alarms.size
    }

    sealed class Action {
        data object NavigateToAlarmAdd : Action()
        data object ToggleSelectionMode : Action()
        data object ToggleDropdownMenu : Action()
        data class ToggleAlarmSelection(val alarmId: Long) : Action()
        data class ToggleAlarmActive(val alarmId: Long) : Action()
        data object ToggleAllAlarmSelection : Action()
        data object ShowDeleteDialog : Action()
        data object HideDeleteDialog : Action()
        data object ConfirmDelete : Action()
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
