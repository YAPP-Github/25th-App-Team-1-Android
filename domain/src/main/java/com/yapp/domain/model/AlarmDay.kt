package com.yapp.domain.model

enum class AlarmDay {
    SUN, MON, TUE, WED, THU, FRI, SAT,
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
