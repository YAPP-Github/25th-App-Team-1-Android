package com.yapp.domain.model

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

fun Alarm.copyFrom(source: Alarm): Alarm {
    return this.copy(
        repeatDays = source.repeatDays,
        isHolidayAlarmOff = source.isHolidayAlarmOff,
        isSnoozeEnabled = source.isSnoozeEnabled,
        snoozeInterval = source.snoozeInterval,
        snoozeCount = source.snoozeCount,
        isVibrationEnabled = source.isVibrationEnabled,
        isSoundEnabled = source.isSoundEnabled,
        soundUri = source.soundUri,
        soundVolume = source.soundVolume,
        isAlarmActive = source.isAlarmActive,
    )
}
