package com.yapp.alarm.pendingIntent.schedule

import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import com.yapp.alarm.AlarmConstants
import com.yapp.alarm.receivers.AlarmReceiver
import com.yapp.domain.model.Alarm
import com.yapp.domain.model.AlarmDay

fun createAlarmReceiverPendingIntentForUnSchedule(
    app: Application,
    alarm: Alarm,
    day: AlarmDay? = null,
): PendingIntent {
    val alarmReceiverIntent = createAlarmReceiverIntent(app, alarm)
    return PendingIntent.getBroadcast(
        app,
        generateAlarmIntentId(alarm.id.toInt(), day),
        alarmReceiverIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )
}

private fun createAlarmReceiverIntent(app: Application, alarm: Alarm): Intent {
    val isOneTimeAlarm: Boolean = alarm.repeatDays == 0
    return Intent(AlarmConstants.ACTION_ALARM_TRIGGERED).apply {
        setClass(app, AlarmReceiver::class.java)
        putExtra(AlarmConstants.EXTRA_NOTIFICATION_ID, alarm.id)
        putExtra(AlarmConstants.EXTRA_IS_ONE_TIME_ALARM, isOneTimeAlarm)
        putExtra(AlarmConstants.EXTRA_SNOOZE_ENABLED, alarm.isSnoozeEnabled)
        putExtra(AlarmConstants.EXTRA_SNOOZE_INTERVAL, alarm.snoozeInterval)
        putExtra(AlarmConstants.EXTRA_SNOOZE_COUNT, alarm.snoozeCount)
        putExtra(AlarmConstants.EXTRA_SOUND_ENABLED, alarm.isSoundEnabled)
        putExtra(AlarmConstants.EXTRA_SOUND_URI, alarm.soundUri)
        putExtra(AlarmConstants.EXTRA_SOUND_VOLUME, alarm.soundVolume)
        putExtra(AlarmConstants.EXTRA_VIBRATION_ENABLED, alarm.isVibrationEnabled)
    }
}
