package com.yapp.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.yapp.common.navigation.destination.HomeDestination
import com.yapp.common.navigation.destination.MissionDestination
import com.yapp.common.util.ResourceProvider
import com.yapp.domain.model.Alarm
import com.yapp.domain.model.toAlarmDays
import com.yapp.domain.model.toDayOfWeek
import com.yapp.domain.usecase.AlarmUseCase
import com.yapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import feature.home.R
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
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
            HomeContract.Action.NavigateToAlarmCreation -> navigateToAlarmCreation()
            HomeContract.Action.ToggleMultiSelectionMode -> toggleMultiSelectionMode()
            HomeContract.Action.ToggleDropdownMenuVisibility -> toggleDropdownMenuVisibility()
            is HomeContract.Action.ToggleAlarmSelection -> toggleAlarmSelection(action.alarmId)
            HomeContract.Action.ToggleAllAlarmSelection -> toggleAllAlarmSelection()
            is HomeContract.Action.ToggleAlarmActivation -> toggleAlarmActivation(action.alarmId)
            HomeContract.Action.ShowDeleteDialog -> showDeleteDialog()
            HomeContract.Action.HideDeleteDialog -> hideDeleteDialog()
            HomeContract.Action.ConfirmDeletion -> confirmDeletion()
            is HomeContract.Action.DeleteSingleAlarm -> deleteSingleAlarm(action.alarmId)
            HomeContract.Action.LoadMoreAlarms -> loadAllAlarms()
            HomeContract.Action.ResetLastAddedAlarmIndex -> restLastAddedAlarmIndex()
            is HomeContract.Action.EditAlarm -> editAlarm(action.alarmId)
            HomeContract.Action.FakeAction -> {
                emitSideEffect(
                    HomeContract.SideEffect.Navigate(
                        route = MissionDestination.Route.route,
                        popUpTo = HomeDestination.Route.route,
                        inclusive = true,
                    ),
                )
            }
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

    private fun navigateToAlarmCreation() {
        emitSideEffect(HomeContract.SideEffect.Navigate(HomeDestination.AlarmAddEdit.route))
    }

    private fun toggleMultiSelectionMode() {
        updateState {
            copy(
                isSelectionMode = !currentState.isSelectionMode,
                selectedAlarmIds = emptySet(),
                dropdownMenuExpanded = false,
            )
        }
    }

    private fun toggleDropdownMenuVisibility() {
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

    private fun toggleAlarmActivation(alarmId: Long) {
        viewModelScope.launch {
            val currentIndex = currentState.alarms.indexOfFirst { it.id == alarmId }
            if (currentIndex == -1) return@launch

            val currentAlarm = currentState.alarms[currentIndex]
            val updatedAlarm = currentAlarm.copy(isAlarmActive = !currentAlarm.isAlarmActive)

            alarmUseCase.updateAlarm(updatedAlarm).onSuccess { newAlarm ->
                val updatedAlarms = currentState.alarms.toMutableList()
                updatedAlarms[currentIndex] = newAlarm
                updateState { copy(alarms = updatedAlarms) }
                updateDeliveryTime(updatedAlarms)
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

    private fun confirmDeletion() {
        deleteAlarms(currentState.selectedAlarmIds)
    }

    private fun deleteSingleAlarm(alarmId: Long) {
        deleteAlarms(setOf(alarmId))
    }

    private fun deleteAlarms(alarmIds: Set<Long>) {
        if (alarmIds.isEmpty()) return

        val alarmsWithIndex = currentState.alarms.withIndex()
            .filter { it.value.id in alarmIds }
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
                    restoreDeletedAlarms(alarmsWithIndex)
                },
            ),
        )
    }

    private fun restoreDeletedAlarms(alarmsWithIndex: List<Pair<Int, Alarm>>) {
        updateState {
            val restoredAlarms = currentState.alarms.toMutableList()
            alarmsWithIndex.forEach { (index, alarm) ->
                restoredAlarms.add(index, alarm)
            }
            copy(alarms = restoredAlarms)
        }
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
                updateDeliveryTime(it)
            }
        }
    }

    private fun editAlarm(alarmId: Long) {
        emitSideEffect(HomeContract.SideEffect.Navigate("${HomeDestination.AlarmAddEdit.route}?id=$alarmId"))
    }

    private fun updateDeliveryTime(alarms: List<Alarm>) {
        val earliestAlarm = alarms
            .filter { it.isAlarmActive }
            .minByOrNull { alarm ->
                getNextAlarmDateWithTime(alarm.isAm, alarm.hour, alarm.minute, alarm.repeatDays)
            }

        val deliveryTime = earliestAlarm?.let { alarm ->
            val alarmDateTime = getNextAlarmDateWithTime(alarm.isAm, alarm.hour, alarm.minute, alarm.repeatDays)
            alarmDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
        } ?: "NONE"

        updateState { copy(deliveryTime = formatDeliveryTime(deliveryTime)) }
    }

    private fun getNextAlarmDateWithTime(isAm: Boolean, hour: Int, minute: Int, repeatDays: Int): LocalDateTime {
        val now = LocalDateTime.now()

        val alarmHour = when {
            isAm && hour == 12 -> 0
            !isAm && hour != 12 -> hour + 12
            else -> hour
        }
        val alarmTime = LocalTime.of(alarmHour, minute)
        val todayAlarm = LocalDateTime.of(now.toLocalDate(), alarmTime)

        // 반복 요일이 설정되지 않은 경우 → 단일 알람
        if (repeatDays == 0) {
            return if (todayAlarm.isAfter(now)) todayAlarm else todayAlarm.plusDays(1)
        }

        // 비트마스크 기반 반복 요일 추출
        val selectedDays = repeatDays.toAlarmDays().map { it.toDayOfWeek() }.sortedBy { it.value }
        val currentDayOfWeek = now.dayOfWeek

        // 가장 빠른 다음 알람 날짜 계산
        val nextDayOffset = selectedDays
            .map { (it.value + 7 - currentDayOfWeek.value) % 7 }
            .filter { it > 0 || todayAlarm.isAfter(now) }
            .minOrNull() ?: (selectedDays.first().value + 7 - currentDayOfWeek.value)

        return todayAlarm.plusDays(nextDayOffset.toLong())
    }

    private fun formatDeliveryTime(deliveryTime: String): String {
        return try {
            if (deliveryTime == "NONE") return resourceProvider.getString(R.string.home_fortune_no_alarm)

            val inputDateTime = LocalDateTime.parse(deliveryTime, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
            val now = LocalDateTime.now()
            val today = now.toLocalDate()
            val tomorrow = today.plusDays(1)

            return when {
                inputDateTime.toLocalDate() == tomorrow ->
                    resourceProvider.getString(R.string.home_fortune_delivery_tomorrow, inputDateTime.format(DateTimeFormatter.ofPattern("a h:mm")))
                inputDateTime.year == now.year ->
                    resourceProvider.getString(
                        R.string.home_fortune_delivery_this_year,
                        inputDateTime.format(DateTimeFormatter.ofPattern("M월 d일 a h:mm")),
                    )
                else ->
                    resourceProvider.getString(
                        R.string.home_fortune_delivery_other_year,
                        inputDateTime.format(DateTimeFormatter.ofPattern("yy년 M월 d일 a h:mm")),
                    )
            }
        } catch (e: Exception) {
            resourceProvider.getString(R.string.home_fortune_no_alarm)
        }
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
