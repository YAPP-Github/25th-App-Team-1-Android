package com.yapp.domain.model

enum class AlarmDay(val bitValue: Int) {
    SUN(0b0000001), // 1
    MON(0b0000010), // 2
    TUE(0b0000100), // 4
    WED(0b0001000), // 8
    THU(0b0010000), // 16
    FRI(0b0100000), // 32
    SAT(0b1000000), // 64
    ;
}

fun AlarmDay.toDayOfWeek(): java.time.DayOfWeek {
    return java.time.DayOfWeek.of(this.ordinal + 1)
}

fun Set<AlarmDay>.toRepeatDays(): Int {
    return this.fold(0) { acc, day ->
        acc or day.bitValue
    }
}

fun Int.toAlarmDays(): List<AlarmDay> {
    return AlarmDay.entries.filter { (this and it.bitValue) != 0 }
}
