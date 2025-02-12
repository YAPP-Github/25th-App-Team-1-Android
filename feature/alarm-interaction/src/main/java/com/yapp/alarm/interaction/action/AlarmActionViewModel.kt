package com.yapp.alarm.interaction.action

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.alarm.pendingIntent.interaction.createAlarmDismissIntent
import com.yapp.common.navigation.Routes
import com.yapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmActionViewModel @Inject constructor(
    private val app: Application,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<AlarmActionContract.State, AlarmActionContract.SideEffect>(
    AlarmActionContract.State(),
) {
    private val notificationId = savedStateHandle.get<Long>("notificationId")

    init {
        updateState {
            copy(
                snoozeEnabled = savedStateHandle.get<Boolean>("snoozeEnabled") ?: false,
                snoozeCount = savedStateHandle.get<Int>("snoozeCount") ?: 5,
                snoozeInterval = savedStateHandle.get<Int>("snoozeInterval") ?: 5,
            )
        }

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
        sendAlarmDismissEventToAlarmReceiver()
    }

    private fun sendAlarmDismissEventToAlarmReceiver() {
        notificationId?.let { id ->
            val alarmDismissIntent = createAlarmDismissIntent(
                context = app,
                notificationId = id,
            )
            app.sendBroadcast(alarmDismissIntent)
        }
    }
}
