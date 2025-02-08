package com.yapp.alarm

import androidx.lifecycle.viewModelScope
import com.yapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmActionViewModel @Inject constructor() : BaseViewModel<AlarmActionContract.State, AlarmActionContract.SideEffect>(
    AlarmActionContract.State(),
) {
    init {
        startClock()
    }

    private fun startClock() {
        viewModelScope.launch {
            while (true) {
                delay(1000L)
                val now = java.time.LocalTime.now()
                val today = java.time.LocalDate.now()
                val dayOfWeek = today.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.KOREAN)

                updateState {
                    copy(
                        isAm = now.hour < 12,
                        hour = if (now.hour % 12 == 0) 12 else now.hour % 12,
                        minute = now.minute,
                        todayDate = "${today.monthValue}월 ${today.dayOfMonth}일 $dayOfWeek",
                    )
                }
            }
        }
    }

    fun processAction(action: AlarmActionContract.Action) {
        when (action) {
            is AlarmActionContract.Action.Snooze -> {
            }
            is AlarmActionContract.Action.Dismiss -> {
            }
        }
    }
}
