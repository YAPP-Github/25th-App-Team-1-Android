package com.yapp.analytics

data class AnalyticsEvent(
    val type: String,
    val properties: Map<String, Any?>? = null,
) {
    object OnboardingPropertiesKeys {
        const val STEP = "step"
        const val GENDER = "gender"
        const val IS_PERMISSION_GRANTED = "permission"
    }

    object AlarmPropertiesKeys {
        const val ALARM_ID = "alarm_id"
        const val ALARM_TIME = "alarm_time"
        const val REPEAT_DAYS = "repeat_days"
        const val SNOOZE_OPTION = "snooze_option"
        const val DISMISS_IS_FIRST_ALARM = "dismiss_is_first_alarm"
    }

    object MissionPropertiesKeys {
        const val MISSION_TYPE = "mission_type"
    }

    object FortunePropertiesKeys {
        const val FORTUNE_PAGE_NUMBER = "fortune_page_number"
        const val DURATION = "duration"
    }
}
