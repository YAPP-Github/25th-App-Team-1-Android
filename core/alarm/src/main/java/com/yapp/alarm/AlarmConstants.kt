package com.yapp.alarm

object AlarmConstants {
    const val ACTION_ALARM_TRIGGERED = "com.yapp.orbit.ACTION_TRIGGERED"
    const val ACTION_ALARM_DISMISSED = "com.yapp.orbit.ACTION_DISMISSED"
    const val ACTION_ALARM_SNOOZED = "com.yapp.orbit.ACTION_SNOOZED"
    const val ACTION_ALARM_INTERACTION_ACTIVITY_CLOSE = "com.yapp.orbit.ACTION_ALERT_INTERACTION_CLOSE"

    const val EXTRA_NOTIFICATION_ID = "com.yapp.orbit.EXTRA_NOTIFICATION_ID"

    const val EXTRA_ALARM = "com.yapp.orbit.EXTRA_ALARM"

    const val EXTRA_SNOOZE_ENABLED = "com.yapp.orbit.EXTRA_SNOOZE_ENABLED"
    const val EXTRA_SNOOZE_INTERVAL = "com.yapp.orbit.EXTRA_SNOOZE_INTERVAL"
    const val EXTRA_SNOOZE_COUNT = "com.yapp.orbit.EXTRA_SNOOZE_COUNT"
    const val EXTRA_SOUND_ENABLED = "com.yapp.orbit.EXTRA_SOUND_ENABLED"
    const val EXTRA_SOUND_URI = "com.yapp.orbit.EXTRA_SOUND_URI"
    const val EXTRA_SOUND_VOLUME = "com.yapp.orbit.EXTRA_SOUND_VOLUME"
    const val EXTRA_VIBRATION_ENABLED = "com.yapp.orbit.EXTRA_VIBRATION_ENABLED"

    const val EXTRA_LAST_ALARM_TIME = "com.yapp.orbit.EXTRA_LAST_ALARM_TIME"

    const val EXTRA_IS_ONE_TIME_ALARM = "com.yapp.orbit.EXTRA_IS_ONE_TIME_ALARM"
    const val EXTRA_IS_DISMISS = "com.yapp.orbit.EXTRA_IS_DISMISS"

    const val WEEK_INTERVAL_MILLIS: Long = 7 * 24 * 60 * 60 * 1000
    const val DAY_INTERVAL_MILLIS: Long = 24 * 60 * 60 * 1000
}
