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
            HomeContract.Action.NavigateToAlarmAdd -> {
                emitSideEffect(HomeContract.SideEffect.Navigate(HomeDestination.AlarmAddEdit.route))
            }
            HomeContract.Action.ToggleSelectionMode -> {
                updateState { copy(isSelectionMode = !currentState.isSelectionMode) }
            }
            HomeContract.Action.ToggleDropdownMenu -> {
                updateState { copy(dropdownMenuExpanded = !currentState.dropdownMenuExpanded) }
            }
            is HomeContract.Action.ToggleAlarmSelection -> {
                updateState {
                    val updatedSelection = if (currentState.selectedAlarmIds.contains(action.alarmId)) {
                        currentState.selectedAlarmIds - action.alarmId
                    } else {
                        currentState.selectedAlarmIds + action.alarmId
                    }
                    copy(selectedAlarmIds = updatedSelection)
                }
            }
            HomeContract.Action.ToggleAllAlarmSelection -> {
                updateState {
                    val allIds = currentState.alarms.map { it.id }.toSet()
                    val updatedSelection = if (currentState.selectedAlarmIds == allIds) {
                        emptySet()
                    } else {
                        allIds
                    }
                    copy(selectedAlarmIds = updatedSelection)
                }
            }
            is HomeContract.Action.ToggleAlarmActive -> {
                updateState {
                    val updatedAlarms = currentState.alarms.map { alarm ->
                        if (alarm.id == action.alarmId) {
                            alarm.copy(isAlarmActive = !alarm.isAlarmActive)
                        } else {
                            alarm
                        }
                    }
                    copy(alarms = updatedAlarms)
                }
            }
        }
    }

    private fun navigateBack() {
        emitSideEffect(HomeContract.SideEffect.NavigateBack)
    }
}
