package com.yapp.analytics

data class AnalyticsEvent(
    val type: String,
    val properties: Map<String, Any?>? = null,
) {
    object OnboardingPropertiesKeys {
        const val STEP = "step"
        const val GENDER = "gender"
        const val PERMISSION = "permission"
    }

    object AlarmPropertiesKeys {
        const val ALARM_ID = "alarm_id"
        const val REPEAT_DAYS = "repeat_days"
        const val SNOOZE_TIME = "snooze_time"
        const val SNOOZE_OPTION = "snooze_option"
        const val DATE = "date"
        const val DISMISS_POSITION = "dismiss_position"
        const val MISSION_TYPE = "mission_type"
        const val PAGE_NUMBER = "page_number"
        const val DURATION = "duration"
    }
}
