package com.yapp.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.yapp.common.navigation.destination.HomeDestination
import com.yapp.domain.model.toAlarm
import com.yapp.domain.usecase.AlarmUseCase
import com.yapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val alarmUseCase: AlarmUseCase,
) : BaseViewModel<HomeContract.State, HomeContract.SideEffect>(
    initialState = HomeContract.State(),
) {
    init {
        loadMoreAlarms()
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
            HomeContract.Action.LoadMoreAlarms -> loadMoreAlarms()
        }
    }

    fun addNewAlarm(alarmJson: String) {
        val newAlarm = alarmJson.toAlarm()
        if (newAlarm != null) {
            updateState {
                copy(
                    alarms = (currentState.alarms + newAlarm)
                        .sortedWith(compareBy({ !it.isAm }, { it.hour }, { it.minute })), // ✅ 시간 순 정렬
                )
            }

            emitSideEffect(
                HomeContract.SideEffect.ShowSnackBar(
                    message = "기상알람이 추가되었어요.",
                    label = "확인",
                    onAction = { },
                    onDismiss = { },
                ),
            )
        } else {
            Log.e("HomeViewModel", "Failed to parse new alarm from JSON")
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
            val currentAlarm = currentState.alarms.find { it.id == alarmId }
            currentAlarm?.let {
                val updatedAlarm = it.copy(isAlarmActive = !it.isAlarmActive)

                alarmUseCase.updateAlarm(updatedAlarm).onSuccess { newAlarm ->
                    updateState {
                        val updatedAlarms = currentState.alarms.map { alarm ->
                            if (alarm.id == alarmId) newAlarm else alarm
                        }
                        copy(alarms = updatedAlarms)
                    }
                }.onFailure { error ->
                    Log.e("HomeViewModel", "Failed to update alarm state", error)
                }
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

        // 삭제할 알람과 그들의 원래 인덱스 저장
        val alarmsWithIndex = currentState.alarms.withIndex()
            .filter { it.value.id in selectedIds }
            .map { it.index to it.value }

        // 삭제할 알람 리스트만 추출
        val alarmsToDelete = alarmsWithIndex.map { it.second }

        updateState {
            copy(
                alarms = currentState.alarms - alarmsToDelete.toSet(), // UI에서 바로 삭제
                selectedAlarmIds = emptySet(),
                isDeleteDialogVisible = false,
                isSelectionMode = false,
            )
        }

        emitSideEffect(
            HomeContract.SideEffect.ShowSnackBar(
                message = "삭제되었어요.",
                label = "취소",
                onDismiss = {
                    viewModelScope.launch {
                        alarmsToDelete.forEach { alarm ->
                            alarmUseCase.deleteAlarm(alarm.id)
                        }
                    }
                },
                onAction = {
                    updateState {
                        // 기존 alarms 리스트에 원래 위치에 삽입
                        val restoredAlarms = currentState.alarms.toMutableList()
                        alarmsWithIndex.forEach { (index, alarm) ->
                            restoredAlarms.add(index, alarm) // 원래 위치에 추가
                        }
                        copy(alarms = restoredAlarms)
                    }
                },
            ),
        )
    }

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
    }
}
