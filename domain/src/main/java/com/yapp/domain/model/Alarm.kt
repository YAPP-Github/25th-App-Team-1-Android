package com.yapp.domain.model

import org.json.JSONObject

data class Alarm(
    val id: Long = 0,

    val isAm: Boolean = true,

    val hour: Int = 6,
    val minute: Int = 0,

    // 반복 요일 (bitmask 를 통해 설정)
    val repeatDays: Int = 0,

    val isHolidayAlarmOff: Boolean = false,
    val isSnoozeEnabled: Boolean = false,

    val snoozeInterval: Int = 5,
    val snoozeCount: Int = 1,

    val isVibrationEnabled: Boolean = true,
    val isSoundEnabled: Boolean = true,

    val soundUri: String = "",
    val soundVolume: Int = 70,

    val isAlarmActive: Boolean = true,
)

fun Alarm.toJson(): String {
    return JSONObject().apply {
        put("id", id)
        put("isAm", isAm)
        put("hour", hour)
        put("minute", minute)
        put("repeatDays", repeatDays)
        put("isHolidayAlarmOff", isHolidayAlarmOff)
        put("isSnoozeEnabled", isSnoozeEnabled)
        put("snoozeInterval", snoozeInterval)
        put("snoozeCount", snoozeCount)
        put("isVibrationEnabled", isVibrationEnabled)
        put("isSoundEnabled", isSoundEnabled)
        put("soundUri", soundUri)
        put("soundVolume", soundVolume)
        put("isAlarmActive", isAlarmActive)
    }.toString()
}

fun String.toAlarm(): Alarm? {
    return try {
        val jsonObject = JSONObject(this)
        Alarm(
            id = jsonObject.getLong("id"),
            isAm = jsonObject.getBoolean("isAm"),
            hour = jsonObject.getInt("hour"),
            minute = jsonObject.getInt("minute"),
            repeatDays = jsonObject.getInt("repeatDays"),
            isHolidayAlarmOff = jsonObject.getBoolean("isHolidayAlarmOff"),
            isSnoozeEnabled = jsonObject.getBoolean("isSnoozeEnabled"),
            snoozeInterval = jsonObject.getInt("snoozeInterval"),
            snoozeCount = jsonObject.getInt("snoozeCount"),
            isVibrationEnabled = jsonObject.getBoolean("isVibrationEnabled"),
            isSoundEnabled = jsonObject.getBoolean("isSoundEnabled"),
            soundUri = jsonObject.getString("soundUri"),
            soundVolume = jsonObject.getInt("soundVolume"),
            isAlarmActive = jsonObject.getBoolean("isAlarmActive"),
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
