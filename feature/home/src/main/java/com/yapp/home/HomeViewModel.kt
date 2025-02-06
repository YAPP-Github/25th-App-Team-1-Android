package com.yapp.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.yapp.common.navigation.destination.HomeDestination
import com.yapp.common.util.ResourceProvider
import com.yapp.domain.usecase.AlarmUseCase
import com.yapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import feature.home.R
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val alarmUseCase: AlarmUseCase,
    private val resourceProvider: ResourceProvider,
) : BaseViewModel<HomeContract.State, HomeContract.SideEffect>(
    initialState = HomeContract.State(),
) {
    init {
        loadAllAlarms()
    }

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
            HomeContract.Action.LoadMoreAlarms -> loadAllAlarms()
            HomeContract.Action.ResetLastAddedAlarmIndex -> restLastAddedAlarmIndex()
            is HomeContract.Action.SelectAlarm -> selectAlarm(action.alarmId)
        }
    }

    fun scrollToAddedAlarm(id: Long) {
        val newAlarmIndex = currentState.alarms.indexOfFirst { it.id == id }
        if (newAlarmIndex == -1) return

        updateState {
            copy(
                lastAddedAlarmIndex = newAlarmIndex,
            )
        }

        emitSideEffect(
            HomeContract.SideEffect.ShowSnackBar(
                message = resourceProvider.getString(R.string.alarm_added),
                iconRes = resourceProvider.getDrawable(core.designsystem.R.drawable.ic_check_green),
                onAction = { },
                onDismiss = { },
            ),
        )
    }

    fun scrollToUpdatedAlarm(id: Long) {
        val updatedAlarmIndex = currentState.alarms.indexOfFirst { it.id == id }
        if (updatedAlarmIndex == -1) return

        updateState {
            copy(
                lastAddedAlarmIndex = updatedAlarmIndex,
            )
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
        viewModelScope.launch {
            val currentIndex = currentState.alarms.indexOfFirst { it.id == alarmId }
            if (currentIndex == -1) return@launch

            val currentAlarm = currentState.alarms[currentIndex]
            val updatedAlarm = currentAlarm.copy(isAlarmActive = !currentAlarm.isAlarmActive)

            alarmUseCase.updateAlarm(updatedAlarm).onSuccess { newAlarm ->
                updateState {
                    val updatedAlarms = currentState.alarms.toMutableList()
                    updatedAlarms[currentIndex] = newAlarm
                    copy(alarms = updatedAlarms)
                }
            }.onFailure { error ->
                Log.e("HomeViewModel", "Failed to update alarm state", error)
            }
        }
    }

    private fun showDeleteDialog() {
        updateState { copy(isDeleteDialogVisible = true) }
    }

    private fun hideDeleteDialog() {
        updateState { copy(isDeleteDialogVisible = false) }
    }

    private fun confirmDelete() {
        val selectedIds = currentState.selectedAlarmIds
        if (selectedIds.isEmpty()) return

        val alarmsWithIndex = currentState.alarms.withIndex()
            .filter { it.value.id in selectedIds }
            .map { it.index to it.value }

        val alarmsToDelete = alarmsWithIndex.map { it.second }

        updateState {
            copy(
                alarms = currentState.alarms - alarmsToDelete.toSet(),
                selectedAlarmIds = emptySet(),
                isDeleteDialogVisible = false,
                isSelectionMode = false,
            )
        }

        emitSideEffect(
            HomeContract.SideEffect.ShowSnackBar(
                message = resourceProvider.getString(R.string.alarm_deleted),
                label = resourceProvider.getString(R.string.alarm_delete_dialog_btn_cancel),
                iconRes = resourceProvider.getDrawable(core.designsystem.R.drawable.ic_check_green),
                onDismiss = {
                    viewModelScope.launch {
                        alarmsToDelete.forEach { alarm ->
                            alarmUseCase.deleteAlarm(alarm.id)
                        }
                    }
                },
                onAction = {
                    updateState {
                        val restoredAlarms = currentState.alarms.toMutableList()
                        alarmsWithIndex.forEach { (index, alarm) ->
                            restoredAlarms.add(index, alarm)
                        }
                        copy(alarms = restoredAlarms)
                    }
                },
            ),
        )
    }

    private fun restLastAddedAlarmIndex() {
        updateState { copy(lastAddedAlarmIndex = null) }
    }

    private fun loadAllAlarms() {
        updateState { copy(initialLoading = true) }

        viewModelScope.launch {
            alarmUseCase.getAllAlarms().collect {
                updateState {
                    copy(
                        alarms = it,
                        initialLoading = false,
                    )
                }
            }
        }
    }

    private fun selectAlarm(alarmId: Long) {
        emitSideEffect(HomeContract.SideEffect.Navigate("${HomeDestination.AlarmAddEdit.route}?id=$alarmId"))
    }

    /*
    private fun loadMoreAlarms() {
        val currentPage = currentState.paginationState.currentPage
        if (currentState.paginationState.isLoading || !currentState.paginationState.hasMoreData) return

        val pageSize = 10
        val offset = currentPage * pageSize

        updateState {
            copy(
                paginationState = currentState.paginationState.copy(isLoading = true),
            )
        }

        viewModelScope.launch {
            alarmUseCase.getPagedAlarms(limit = pageSize, offset = offset)
                .onSuccess {
                    updateState {
                        copy(
                            alarms = currentState.alarms + it,
                            paginationState = currentState.paginationState.copy(
                                currentPage = currentPage + 1,
                                isLoading = false,
                                hasMoreData = it.size == pageSize,
                            ),
                            initialLoading = false,
                        )
                    }
                }
                .onFailure {
                    Log.e("HomeViewModel", "Failed to get paged alarms", it)
                    updateState {
                        copy(
                            paginationState = currentState.paginationState.copy(isLoading = false),
                            initialLoading = false,
                        )
                    }
                }
        }
    }*/
}
