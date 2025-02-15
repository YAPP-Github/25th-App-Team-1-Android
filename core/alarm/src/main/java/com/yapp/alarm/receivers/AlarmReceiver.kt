package com.yapp.alarm.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.yapp.alarm.AlarmConstants
import com.yapp.alarm.AlarmHelper
import com.yapp.alarm.services.AlarmService
import com.yapp.domain.model.Alarm
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmHelper: AlarmHelper

    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        intent ?: return

        val alarmServiceIntent = createAlarmServiceIntent(context, intent)
        when (intent.action) {
            AlarmConstants.ACTION_ALARM_TRIGGERED -> {
                Log.d("AlarmReceiver", "Alarm Triggered")
                context.startForegroundService(alarmServiceIntent)
            }

            AlarmConstants.ACTION_ALARM_SNOOZED -> {
                Log.d("AlarmReceiver", "Alarm Snoozed")
                val alarm: Alarm? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(AlarmConstants.EXTRA_ALARM, Alarm::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    intent.getParcelableExtra(AlarmConstants.EXTRA_ALARM)
                }

                alarm?.let { handleSnooze(context, it) }
            }

            AlarmConstants.ACTION_ALARM_DISMISSED -> {
                Log.d("AlarmReceiver", "Alarm Dismissed")
                context.stopService(alarmServiceIntent)
                sendBroadCastToCloseAlarmInteractionActivity(context)
            }
        }
    }

    private fun createAlarmServiceIntent(
        context: Context,
        intent: Intent,
    ): Intent {
        return Intent(context, AlarmService::class.java).apply {
            putExtras(intent.extras!!)
        }
    }

    private fun handleSnooze(context: Context, alarm: Alarm) {
        if (alarm.snoozeCount == 0) return

        val newSnoozeCount = if (alarm.snoozeCount == -1) {
            alarm.snoozeCount
        } else {
            alarm.snoozeCount - 1
        }

        val snoozeDateTime = LocalDateTime.now()
            .plusMinutes(alarm.snoozeInterval.toLong())

        val updatedAlarm = alarm.copy(
            isAm = snoozeDateTime.hour < 12,
            hour = if (snoozeDateTime.hour == 0) 12 else if (snoozeDateTime.hour > 12) snoozeDateTime.hour - 12 else snoozeDateTime.hour,
            minute = snoozeDateTime.minute,
            second = snoozeDateTime.second,
            snoozeCount = newSnoozeCount,
        )

        Log.d(
            "AlarmReceiver",
            "Scheduling snooze alarm: alarmId=${alarm.id}, newTime=${updatedAlarm.hour}:${updatedAlarm.minute}, remaining snoozeCount=$newSnoozeCount",
        )

        context.stopService(Intent(context, AlarmService::class.java))
        alarmHelper.scheduleAlarm(updatedAlarm)
    }

    private fun sendBroadCastToCloseAlarmInteractionActivity(context: Context) {
        Log.d("AlarmReceiver", "Send Broadcast to close Alarm Interaction Activity")
        val alarmAlertActivityCloseIntent =
            Intent(AlarmConstants.ACTION_ALARM_INTERACTION_ACTIVITY_CLOSE).apply {
                putExtra(AlarmConstants.EXTRA_IS_SNOOZED, false)
            }
        context.sendBroadcast(alarmAlertActivityCloseIntent)
    }
}
