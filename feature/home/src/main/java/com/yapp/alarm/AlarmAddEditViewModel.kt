package com.yapp.alarm

import com.yapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlarmAddEditViewModel @Inject constructor(

) : BaseViewModel<AlarmAddEditContract.State, AlarmAddEditContract.SideEffect>(
    initialState = AlarmAddEditContract.State(),
) {
    fun processAction(action: AlarmAddEditContract.Action) {
        when (action) {
            is AlarmAddEditContract.Action.UpdateAlarmTime -> updateAlarmTime(action.amPm, action.hour, action.minute)
            is AlarmAddEditContract.Action.UpdateWeekdaysChecked -> updateWeekdaysChecked(action.isChecked)
            is AlarmAddEditContract.Action.UpdateWeekendsChecked -> updateWeekendsChecked(action.isChecked)
            is AlarmAddEditContract.Action.UpdateSelectedDays -> updateSelectedDays(action.selectedDays)
            is AlarmAddEditContract.Action.UpdateDisableHolidayChecked -> updateDisableHolidayChecked(action.isChecked)
        }
    }

    private fun updateAlarmTime(amPm: String, hour: Int, minute: Int) {
        updateState {
            copy(
                currentAmPm = amPm,
                currentHour = hour,
                currentMinute = minute
            )
        }
    }

    private fun updateWeekdaysChecked(isChecked: Boolean) {
        updateState { copy(isWeekdaysChecked = isChecked) }
    }

    private fun updateWeekendsChecked(isChecked: Boolean) {
        updateState { copy(isWeekendsChecked = isChecked) }
    }

    private fun updateSelectedDays(selectedDays: List<String>) {
        updateState { copy(selectedDays = selectedDays) }
    }

    private fun updateDisableHolidayChecked(isChecked: Boolean) {
        updateState { copy(isDisableHolidayChecked = isChecked) }
    }
}
