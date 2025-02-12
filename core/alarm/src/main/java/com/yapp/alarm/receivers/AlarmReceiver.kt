package com.yapp.alarm.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.yapp.alarm.AlarmConstants
import com.yapp.alarm.services.AlarmService

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        intent ?: return

        val alarmServiceIntent = createAlarmServiceIntent(context, intent)
        when (intent.action) {
            AlarmConstants.ACTION_ALARM_TRIGGERED -> {
                Log.d("AlarmReceiver", "Alarm Triggered")
                context.startForegroundService(alarmServiceIntent)
            }
            AlarmConstants.ACTION_ALARM_SNOOZE -> {
                Log.d("AlarmReceiver", "Alarm Snoozed")
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

    private fun sendBroadCastToCloseAlarmInteractionActivity(context: Context) {
        Log.d("AlarmReceiver", "Send Broadcast to close Alarm Interaction Activity")
        val alarmAlertActivityCloseIntent =
            Intent(AlarmConstants.ACTION_ALARM_INTERACTION_ACTIVITY_CLOSE)
        context.sendBroadcast(alarmAlertActivityCloseIntent)
    }
}
