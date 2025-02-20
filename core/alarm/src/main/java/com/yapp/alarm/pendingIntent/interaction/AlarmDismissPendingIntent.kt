package com.yapp.alarm.pendingIntent.interaction

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.net.Uri
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

fun createNavigateToMissionPendingIntent(
    applicationContext: Context,
    notificationId: Long,
): PendingIntent {
    val navigateToMissionIntent = createNavigateToMissionIntent(notificationId)
    return PendingIntent.getActivity(
        applicationContext,
        notificationId.toInt(),
        navigateToMissionIntent,
        FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE,
    )
}

fun createNavigateToMissionIntent(
    notificationId: Long,
): Intent {
    return Intent(Intent.ACTION_VIEW, Uri.parse("orbitapp://mission?notificationId=$notificationId")).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    }
}
