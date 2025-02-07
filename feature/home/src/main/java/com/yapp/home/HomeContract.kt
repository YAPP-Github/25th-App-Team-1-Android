package com.yapp.home

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
        val lastFortuneScore: Int = -1,
        val deliveryTime: String = "받을 수 있는 운세가 없어요",
        val name: String = "동현",
    ) : UiState {
        val isAllSelected: Boolean
            get() = alarms.isNotEmpty() && selectedAlarmIds.size == alarms.size
    }

    sealed class Action {
        data object NavigateToAlarmCreation : Action()
        data object ToggleMultiSelectionMode : Action()
        data object ToggleDropdownMenuVisibility : Action()
        data class ToggleAlarmSelection(val alarmId: Long) : Action()
        data class ToggleAlarmActivation(val alarmId: Long) : Action()
        data object ToggleAllAlarmSelection : Action()
        data object ShowDeleteDialog : Action()
        data object HideDeleteDialog : Action()
        data object ConfirmDeletion : Action()
        data class DeleteSingleAlarm(val alarmId: Long) : Action()
        data object LoadMoreAlarms : Action()
        data object ResetLastAddedAlarmIndex : Action()
        data class EditAlarm(val alarmId: Long) : Action()
        data object FakeAction : Action()
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
            val label: String = "",
            val iconRes: Int,
            val bottomPadding: Dp = 12.dp,
            val duration: SnackbarDuration = SnackbarDuration.Short,
            val onDismiss: () -> Unit,
            val onAction: () -> Unit,
        ) : SideEffect()
    }
}
