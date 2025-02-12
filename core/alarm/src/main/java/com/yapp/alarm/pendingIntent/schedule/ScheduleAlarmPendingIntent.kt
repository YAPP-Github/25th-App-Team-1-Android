package com.yapp.alarm.pendingIntent.schedule

import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import com.yapp.alarm.AlarmConstants
import com.yapp.alarm.receivers.AlarmReceiver
import com.yapp.domain.model.Alarm
import com.yapp.domain.model.AlarmDay

fun createAlarmReceiverPendingIntentForSchedule(
    app: Application,
    alarm: Alarm,
    snoozeCount: Int,
    day: AlarmDay? = null,
): PendingIntent {
    val alarmReceiverIntent = createAlarmReceiverIntent(
        app,
        alarm,
        snoozeCount,
    )
    return PendingIntent.getBroadcast(
        app,
        generateAlarmIntentId(alarm.id.toInt(), day),
        alarmReceiverIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )
}

private fun createAlarmReceiverIntent(
    app: Application,
    alarm: Alarm,
    snoozeCount: Int,
): Intent {
    val isOneTimeAlarm: Boolean = alarm.repeatDays == 0
    return Intent(AlarmConstants.ACTION_ALARM_TRIGGERED).apply {
        setClass(app, AlarmReceiver::class.java)

        putExtra(AlarmConstants.EXTRA_NOTIFICATION_ID, alarm.id)
        putExtra(AlarmConstants.EXTRA_IS_ONE_TIME_ALARM, isOneTimeAlarm)
        putExtra(AlarmConstants.EXTRA_SNOOZE_ENABLED, alarm.isSnoozeEnabled)
        putExtra(AlarmConstants.EXTRA_SNOOZE_INTERVAL, alarm.snoozeInterval)
        putExtra(AlarmConstants.EXTRA_SNOOZE_COUNT, snoozeCount)
        putExtra(AlarmConstants.EXTRA_SOUND_ENABLED, alarm.isSoundEnabled)
        putExtra(AlarmConstants.EXTRA_SOUND_URI, alarm.soundUri)
        putExtra(AlarmConstants.EXTRA_SOUND_VOLUME, alarm.soundVolume)
        putExtra(AlarmConstants.EXTRA_VIBRATION_ENABLED, alarm.isVibrationEnabled)
    }
}

fun generateAlarmIntentId(id: Int, day: AlarmDay?): Int {
    return day?.let {
        (id * 10) + it.ordinal + 1
    } ?: id
}
