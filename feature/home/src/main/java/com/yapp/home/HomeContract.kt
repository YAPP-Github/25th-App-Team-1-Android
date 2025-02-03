package com.yapp.home

import com.yapp.domain.model.Alarm
import com.yapp.ui.base.UiState

sealed class HomeContract {

    data class State(
        val alarms: List<Alarm> = emptyList(),
        val paginationState: PaginationState = PaginationState(),
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

    data class PaginationState(
        val currentPage: Int = 0,
        val isLoading: Boolean = false,
        val hasMoreData: Boolean = true,
    )

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
        data object LoadMoreAlarms : Action()
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
