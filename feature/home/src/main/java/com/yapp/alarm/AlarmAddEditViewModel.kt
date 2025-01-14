package com.yapp.alarm

import com.yapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlarmAddEditViewModel @Inject constructor() : BaseViewModel<AlarmAddEditContract.State, AlarmAddEditContract.SideEffect>(
    initialState = AlarmAddEditContract.State(),
) {
    fun processAction(action: AlarmAddEditContract.Action) {
        when (action) {
            is AlarmAddEditContract.Action.UpdateAlarmTime -> updateAlarmTime(action.amPm, action.hour, action.minute)
            is AlarmAddEditContract.Action.ToggleWeekdaysChecked -> toggleWeekdaysChecked()
            is AlarmAddEditContract.Action.ToggleWeekendsChecked -> toggleWeekendsChecked()
            is AlarmAddEditContract.Action.ToggleDaySelection -> toggleDaySelection(action.day)
            is AlarmAddEditContract.Action.ToggleDisableHolidayChecked -> toggleDisableHolidayChecked()
        }
    }

    private fun updateAlarmTime(amPm: String, hour: Int, minute: Int) {
        updateState {
            copy(
                currentAmPm = amPm,
                currentHour = hour,
                currentMinute = minute,
            )
        }
    }

    private fun toggleWeekdaysChecked() {
        val weekdays = setOf(AlarmDay.MON, AlarmDay.TUE, AlarmDay.WED, AlarmDay.THU, AlarmDay.FRI)
        val isChecked = !currentState.isWeekdaysChecked
        val updatedDays = if (isChecked) {
            currentState.selectedDays + weekdays
        } else {
            currentState.selectedDays - weekdays
        }
        updateState {
            copy(
                isWeekdaysChecked = isChecked,
                selectedDays = updatedDays,
            )
        }
    }

    private fun toggleWeekendsChecked() {
        val weekends = setOf(AlarmDay.SAT, AlarmDay.SUN)
        val isChecked = !currentState.isWeekendsChecked
        val updatedDays = if (isChecked) {
            currentState.selectedDays + weekends
        } else {
            currentState.selectedDays - weekends
        }
        updateState {
            copy(
                isWeekendsChecked = isChecked,
                selectedDays = updatedDays,
            )
        }
    }

    private fun toggleDaySelection(day: AlarmDay) {
        val updatedDays = if (day in currentState.selectedDays) {
            currentState.selectedDays - day
        } else {
            currentState.selectedDays + day
        }
        val weekdays = setOf(AlarmDay.MON, AlarmDay.TUE, AlarmDay.WED, AlarmDay.THU, AlarmDay.FRI)
        val weekends = setOf(AlarmDay.SAT, AlarmDay.SUN)
        updateState {
            copy(
                selectedDays = updatedDays,
                isWeekdaysChecked = updatedDays.containsAll(weekdays),
                isWeekendsChecked = updatedDays.containsAll(weekends),
            )
        }
    }

    private fun toggleDisableHolidayChecked() {
        updateState {
            copy(
                isDisableHolidayChecked = !currentState.isDisableHolidayChecked,
            )
        }
    }
}
