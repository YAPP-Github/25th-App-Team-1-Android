package com.yapp.alarm

import android.app.AlarmManager
import android.app.Application
import android.util.Log
import com.yapp.alarm.pendingIntent.schedule.createAlarmReceiverPendingIntentForSchedule
import com.yapp.alarm.pendingIntent.schedule.createAlarmReceiverPendingIntentForUnSchedule
import com.yapp.domain.model.Alarm
import com.yapp.domain.model.AlarmDay
import com.yapp.domain.model.toAlarmDays
import com.yapp.domain.model.toDayOfWeek
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class AlarmHelper @Inject constructor(
    private val app: Application,
    private val alarmManager: AlarmManager,
) {
    fun scheduleAlarm(alarm: Alarm) {
        val selectedDays = alarm.repeatDays.toAlarmDays()

        if (selectedDays.isEmpty()) {
            setNonRepeatingAlarm(alarm)
        } else {
            selectedDays.forEach { day ->
                setRepeatingAlarm(day, alarm)
            }
        }
    }

    fun unScheduleAlarm(alarm: Alarm) {
        val selectedDays = alarm.repeatDays.toAlarmDays()

        if (selectedDays.isEmpty()) {
            val pendingIntent = createAlarmReceiverPendingIntentForUnSchedule(
                app,
                alarm,
                null,
            )
            alarmManager.cancel(pendingIntent)
        } else {
            selectedDays.forEach { day ->
                val pendingIntent = createAlarmReceiverPendingIntentForUnSchedule(
                    app,
                    alarm,
                    day,
                )
                alarmManager.cancel(pendingIntent)
            }
        }
    }

    private fun setRepeatingAlarm(day: AlarmDay, alarm: Alarm) {
        val alarmReceiverPendingIntent =
            createAlarmReceiverPendingIntentForSchedule(app, alarm, day)
        val firstAlarmTriggerMillis = getNextAlarmTimeMillis(alarm, day)

        Log.d("AlarmHelper", "Setting repeating alarm at: $firstAlarmTriggerMillis")

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            firstAlarmTriggerMillis,
            alarmReceiverPendingIntent,
        )
    }

    private fun setNonRepeatingAlarm(alarm: Alarm) {
        val alarmReceiverPendingIntent =
            createAlarmReceiverPendingIntentForSchedule(app, alarm)

        val triggerMillis = getNextAlarmTimeMillis(alarm, null)

        Log.d("AlarmHelper", "Setting one-time alarm at: $triggerMillis")

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerMillis,
            alarmReceiverPendingIntent,
        )
    }

    private fun getNextAlarmTimeMillis(alarm: Alarm, day: AlarmDay?): Long {
        val now = LocalDateTime.now().withNano(0) // 밀리초 제거하여 정확한 초 기준 설정

        val alarmHour = when {
            alarm.isAm && alarm.hour == 12 -> 0
            !alarm.isAm && alarm.hour != 12 -> alarm.hour + 12
            else -> alarm.hour
        }

        var alarmDateTime = now.withHour(alarmHour).withMinute(alarm.minute).withSecond(alarm.second)

        if (day != null) {
            val targetDayOfWeek = day.toDayOfWeek()
            while (alarmDateTime.dayOfWeek != targetDayOfWeek || alarmDateTime.isBefore(now)) {
                alarmDateTime = alarmDateTime.plusDays(1)
            }
        } else {
            if (alarmDateTime.isBefore(now)) {
                alarmDateTime = alarmDateTime.plusDays(1)
            }
        }

        val epochMillis = alarmDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        Log.d("AlarmHelper", "Alarm scheduled at: $alarmDateTime (epochMillis=$epochMillis)")

        return epochMillis
    }
}
