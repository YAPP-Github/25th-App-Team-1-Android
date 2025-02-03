package com.yapp.home

import com.yapp.common.navigation.destination.HomeDestination
import com.yapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel<HomeContract.State, HomeContract.SideEffect>(
    initialState = HomeContract.State(),
) {
    fun processAction(action: HomeContract.Action) {
        when (action) {
            HomeContract.Action.NavigateToAlarmAdd -> navigateToAlarmAdd()
            HomeContract.Action.ToggleSelectionMode -> toggleSelectionMode()
            HomeContract.Action.ToggleDropdownMenu -> toggleDropdownMenu()
            is HomeContract.Action.ToggleAlarmSelection -> toggleAlarmSelection(action.alarmId)
            HomeContract.Action.ToggleAllAlarmSelection -> toggleAllAlarmSelection()
            is HomeContract.Action.ToggleAlarmActive -> toggleAlarmActive(action.alarmId)
            HomeContract.Action.ShowDeleteDialog -> showDeleteDialog()
            HomeContract.Action.HideDeleteDialog -> hideDeleteDialog()
            HomeContract.Action.ConfirmDelete -> confirmDelete()
        }
    }

    private fun navigateToAlarmAdd() {
        emitSideEffect(HomeContract.SideEffect.Navigate(HomeDestination.AlarmAddEdit.route))
    }

    private fun toggleSelectionMode() {
        updateState {
            copy(
                isSelectionMode = !currentState.isSelectionMode,
                selectedAlarmIds = emptySet(),
                dropdownMenuExpanded = false,
            )
        }
    }

    private fun toggleDropdownMenu() {
        updateState { copy(dropdownMenuExpanded = !currentState.dropdownMenuExpanded) }
    }

    private fun toggleAlarmSelection(alarmId: Long) {
        updateState {
            val updatedSelection = currentState.selectedAlarmIds.toMutableSet().apply {
                if (contains(alarmId)) remove(alarmId) else add(alarmId)
            }
            copy(selectedAlarmIds = updatedSelection)
        }
    }

    private fun toggleAllAlarmSelection() {
        updateState {
            val allIds = currentState.alarms.map { it.id }.toSet()
            val updatedSelection = if (currentState.selectedAlarmIds == allIds) emptySet() else allIds
            copy(selectedAlarmIds = updatedSelection)
        }
    }

    private fun toggleAlarmActive(alarmId: Long) {
        updateState {
            val updatedAlarms = currentState.alarms.map { alarm ->
                if (alarm.id == alarmId) {
                    alarm.copy(isAlarmActive = !alarm.isAlarmActive)
                } else {
                    alarm
                }
            }
            copy(alarms = updatedAlarms)
        }
    }

    private fun showDeleteDialog() {
        updateState { copy(isDeleteDialogVisible = true) }
    }

    private fun hideDeleteDialog() {
        updateState { copy(isDeleteDialogVisible = false) }
    }

    private fun confirmDelete() {
        updateState {
            val updatedAlarms = currentState.alarms.filterNot { it.id in currentState.selectedAlarmIds }
            copy(
                alarms = updatedAlarms,
                selectedAlarmIds = emptySet(),
                isDeleteDialogVisible = false,
                isSelectionMode = false,
            )
        }
    }
}
