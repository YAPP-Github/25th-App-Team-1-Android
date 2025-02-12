package com.yapp.alarm.interaction.snooze

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.alarm.pendingIntent.interaction.createAlarmDismissIntent
import com.yapp.domain.model.Alarm
import com.yapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class AlarmSnoozeTimerViewModel @Inject constructor(
    private val app: Application,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<AlarmSnoozeTimerContract.State, AlarmSnoozeTimerContract.SideEffect>(
    AlarmSnoozeTimerContract.State(),
) {
    private val alarmJson: String? = savedStateHandle.get<String>("alarm")
    private val alarm: Alarm? = alarmJson?.let { Alarm.fromJson(it) }

    init {
        startClock()
    }

    private fun startClock() {
        viewModelScope.launch {
            val nowMillis = System.currentTimeMillis()
            val nextSnoozeTimeMillis = alarm?.let { getNextSnoozeAlarmTimeMillis(it) } ?: nowMillis
            val remainingMillis = max(0, nextSnoozeTimeMillis - nowMillis)
            val remainingSeconds = (remainingMillis / 1000).toInt()

            updateState {
                copy(
                    remainingSeconds = remainingSeconds,
                    totalSeconds = remainingSeconds,
                    alarmTimeStamp = nextSnoozeTimeMillis / 1000,
                    initialLoading = true,
                )
            }.join()

            while (isActive) {
                val currentTime = System.currentTimeMillis() / 1000
                val remaining = max(0, currentState.alarmTimeStamp - currentTime)

                updateState {
                    copy(
                        remainingSeconds = remaining.toInt(),
                        initialLoading = false,
                    )
                }

                if (remaining.toInt() == 0) break

                delay(1000L)
            }
        }
    }

    fun processAction(action: AlarmSnoozeTimerContract.Action) {
        when (action) {
            is AlarmSnoozeTimerContract.Action.Dismiss -> dismiss()
        }
    }

    private fun dismiss() {
        sendAlarmDismissEventToAlarmReceiver()
    }

    private fun sendAlarmDismissEventToAlarmReceiver() {
        alarm?.id?.let { id ->
            val alarmDismissIntent = createAlarmDismissIntent(
                context = app,
                notificationId = id,
            )
            app.sendBroadcast(alarmDismissIntent)
        }
    }

    private fun getNextSnoozeAlarmTimeMillis(
        alarm: Alarm,
    ): Long {
        val now = LocalDateTime.now().withNano(0)

        val alarmHour = when {
            alarm.isAm && alarm.hour == 12 -> 0
            !alarm.isAm && alarm.hour != 12 -> alarm.hour + 12
            else -> alarm.hour
        }

        var alarmDateTime = now.withHour(alarmHour).withMinute(alarm.minute).withSecond(0)
        alarmDateTime = alarmDateTime.plusMinutes(alarm.snoozeInterval.toLong())

        return alarmDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
}
