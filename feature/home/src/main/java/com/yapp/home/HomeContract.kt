package com.yapp.home

import androidx.compose.material3.SnackbarDuration
import com.yapp.domain.model.Alarm
import com.yapp.ui.base.UiState

sealed class HomeContract {

    data class State(
        val initialLoading: Boolean = true,
        val alarms: List<Alarm> = emptyList(),
        val lastAddedAlarmIndex: Int? = null,
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
        data object LoadMoreAlarms : Action()
        data object ResetLastAddedAlarmIndex : Action()
    }

    sealed class SideEffect : com.yapp.ui.base.SideEffect {
        data class Navigate(
            val route: String,
            val popUpTo: String? = null,
            val inclusive: Boolean = false,
        ) : SideEffect()

        data object NavigateBack : SideEffect()

        data class ShowSnackBar(
            val message: String,
            val label: String,
            val duration: SnackbarDuration = SnackbarDuration.Short,
            val onDismiss: () -> Unit,
            val onAction: () -> Unit,
        ) : SideEffect()
    }
}
