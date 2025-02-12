package com.yapp.alarm.pendingIntent.interaction

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import com.yapp.alarm.AlarmConstants

fun createAlarmAlertPendingIntent(
    context: Context,
    alarmId: Long,
    snoozeEnabled: Boolean,
    snoozeInterval: Int,
    snoozeCount: Int,
): PendingIntent {
    val alarmAlertIntent = createAlarmAlertIntent(
        alarmId,
        snoozeEnabled,
        snoozeInterval,
        snoozeCount,
    )
    return PendingIntent.getActivity(
        context,
        alarmId.toInt(),
        alarmAlertIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE,
    )
}

private fun createAlarmAlertIntent(
    notificationId: Long,
    snoozeEnabled: Boolean,
    snoozeInterval: Int,
    snoozeCount: Int,
): Intent {
    return Intent("com.yapp.alarm.interaction.ACTION_ALARM_INTERACTION").apply {
        putExtra(AlarmConstants.EXTRA_NOTIFICATION_ID, notificationId)
        putExtra(AlarmConstants.EXTRA_SNOOZE_ENABLED, snoozeEnabled)
        putExtra(AlarmConstants.EXTRA_SNOOZE_INTERVAL, snoozeInterval)
        putExtra(AlarmConstants.EXTRA_SNOOZE_COUNT, snoozeCount)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
}
