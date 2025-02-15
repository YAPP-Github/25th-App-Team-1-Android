package com.yapp.alarm.pendingIntent.interaction

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import com.yapp.alarm.AlarmConstants
import com.yapp.alarm.receivers.AlarmReceiver

fun createAlarmDismissPendingIntent(
    applicationContext: Context,
    pendingIntentId: Long,
): PendingIntent {
    val alarmDismissIntent = createAlarmDismissIntent(applicationContext, pendingIntentId)
    return PendingIntent.getBroadcast(
        applicationContext,
        pendingIntentId.toInt(),
        alarmDismissIntent,
        FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE,
    )
}

fun createAlarmDismissIntent(
    context: Context,
    notificationId: Long,
): Intent {
    return Intent(AlarmConstants.ACTION_ALARM_DISMISSED).apply {
        setClass(context, AlarmReceiver::class.java)
        putExtra(AlarmConstants.EXTRA_NOTIFICATION_ID, notificationId)
    }
}
