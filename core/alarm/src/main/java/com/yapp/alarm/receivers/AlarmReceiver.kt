package com.yapp.alarm.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.yapp.alarm.AlarmConstants
import com.yapp.alarm.AlarmHelper
import com.yapp.alarm.services.AlarmService
import com.yapp.datastore.UserPreferences
import com.yapp.domain.model.Alarm
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmHelper: AlarmHelper

    @Inject
    lateinit var userPreferences: UserPreferences

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

                Toast.makeText(context, "알람이 ${alarm?.snoozeInterval}분 후 다시 울려요", Toast.LENGTH_SHORT).show()
            }

            AlarmConstants.ACTION_ALARM_DISMISSED -> {
                Log.d("AlarmReceiver", "Alarm Dismissed")
                val alarmId = intent.getLongExtra(AlarmConstants.EXTRA_NOTIFICATION_ID, -1L)
                if (alarmId != -1L) {
                    handleFirstAlarmDismissed(alarmId)
                    alarmHelper.cancelSnoozedAlarm(alarmId)
                } else {
                    Log.e("AlarmReceiver", "알람 ID 수신 실패")
                }
                alarmHelper.cancelSnoozedAlarm(alarmId)
                context.stopService(alarmServiceIntent)
                sendBroadCastToCloseAlarmInteractionActivity(context)

                Toast.makeText(context, "알람이 해제되었어요", Toast.LENGTH_SHORT).show()
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
            repeatDays = 0,
            snoozeCount = newSnoozeCount,
            id = alarm.id + AlarmConstants.SNOOZE_ID_OFFSET,
        )

        Log.d(
            "AlarmReceiver",
            "Scheduling snooze alarm: alarmId=${updatedAlarm.id}, newTime=${updatedAlarm.hour}:${updatedAlarm.minute}, remaining snoozeCount=$newSnoozeCount",
        )

        context.stopService(Intent(context, AlarmService::class.java))
        alarmHelper.scheduleAlarm(updatedAlarm)
    }

    private fun handleFirstAlarmDismissed(alarmId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            val existingId = userPreferences.firstDismissedAlarmIdFlow.firstOrNull()
            if (existingId == null) {
                // 첫 번째 알람 해제 기록
                userPreferences.saveFirstDismissedAlarmId(alarmId)
            } else if (existingId != alarmId) {
                // 두 번째 알람 해제 감지 - 기존 기록 삭제
                userPreferences.clearDismissedAlarmId()
            }
        }
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
