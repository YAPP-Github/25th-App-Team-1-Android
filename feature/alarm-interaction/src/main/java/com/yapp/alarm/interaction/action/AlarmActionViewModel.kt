package com.yapp.alarm.interaction.action

import androidx.lifecycle.viewModelScope
import com.yapp.common.navigation.Routes
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
                val now = java.time.LocalTime.now()
                val today = java.time.LocalDate.now()
                val dayOfWeek = today.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.KOREAN)

                updateState {
                    copy(
                        isAm = now.hour < 12,
                        hour = if (now.hour % 12 == 0) 12 else now.hour % 12,
                        minute = now.minute,
                        todayDate = "${today.monthValue}월 ${today.dayOfMonth}일 $dayOfWeek",
                        initialLoading = false,
                    )
                }

                delay(1000L)
            }
        }
    }

    fun processAction(action: AlarmActionContract.Action) {
        when (action) {
            is AlarmActionContract.Action.Snooze -> snooze()
            is AlarmActionContract.Action.Dismiss -> dismiss()
        }
    }

    private fun snooze() {
        updateState {
            copy(
                snoozeCount = currentState.snoozeCount - 1,
            )
        }
        emitSideEffect(AlarmActionContract.SideEffect.Navigate(Routes.AlarmInteraction.ALARM_SNOOZE_TIMER))
    }

    private fun dismiss() {
        emitSideEffect(AlarmActionContract.SideEffect.Navigate(Routes.Mission.ROUTE))
    }
}
