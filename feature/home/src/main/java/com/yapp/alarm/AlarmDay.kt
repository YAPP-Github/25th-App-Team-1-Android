package com.yapp.alarm

import feature.home.R

enum class AlarmDay(
    val label: Int,
) {
    SUN(
        R.string.alarm_add_edit_sunday,
    ),
    MON(
        R.string.alarm_add_edit_monday,
    ),
    TUE(
        R.string.alarm_add_edit_tuesday,
    ),
    WED(
        R.string.alarm_add_edit_wednesday,
    ),
    THU(
        R.string.alarm_add_edit_thursday,
    ),
    FRI(
        R.string.alarm_add_edit_friday,
    ),
    SAT(
        R.string.alarm_add_edit_saturday,
    ),
}

fun AlarmDay.toDayOfWeek(): java.time.DayOfWeek {
    return when (this) {
        AlarmDay.SUN -> java.time.DayOfWeek.SUNDAY
        AlarmDay.MON -> java.time.DayOfWeek.MONDAY
        AlarmDay.TUE -> java.time.DayOfWeek.TUESDAY
        AlarmDay.WED -> java.time.DayOfWeek.WEDNESDAY
        AlarmDay.THU -> java.time.DayOfWeek.THURSDAY
        AlarmDay.FRI -> java.time.DayOfWeek.FRIDAY
        AlarmDay.SAT -> java.time.DayOfWeek.SATURDAY
    }
}
