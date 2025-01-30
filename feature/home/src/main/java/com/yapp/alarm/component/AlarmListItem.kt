package com.yapp.alarm.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.domain.model.AlarmDay
import com.yapp.domain.model.toRepeatDays
import com.yapp.ui.component.switch.OrbitSwitch
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
internal fun AlarmListItem(
    repeatDays: Int,
    isHolidayAlarmOff: Boolean,
    isAm: Boolean,
    hour: Int,
    minute: Int,
    isActive: Boolean,
    onToggleActive: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 22.dp,
                vertical = 20.dp,
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = repeatDays.toRepeatDaysString(
                        isAm = isAm,
                        hour = hour,
                        minute = minute,
                    ),
                    style = OrbitTheme.typography.label1SemiBold,
                    color = OrbitTheme.colors.gray_300,
                )

                if (isHolidayAlarmOff) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        painter = painterResource(id = core.designsystem.R.drawable.ic_holiday),
                        contentDescription = "Holiday Alarm Off",
                        tint = OrbitTheme.colors.gray_200,
                        modifier = Modifier.size(12.dp),
                    )
                }
            }

            Text(
                text = "${if (isAm) "오전" else "오후"} $hour:${minute.toString().padStart(2, '0')}",
                style = OrbitTheme.typography.title2Medium,
                color = OrbitTheme.colors.white,
            )
        }

        OrbitSwitch(
            isChecked = isActive,
        ) {
            onToggleActive()
        }
    }
}

private fun Int.toRepeatDaysString(isAm: Boolean, hour: Int, minute: Int): String {
    val days = AlarmDay.entries.filter { (this and it.bitValue) != 0 }

    return when {
        days.size == 7 -> "매일"
        days.isNotEmpty() -> "매주 " + days.joinToString(", ") { it.toKoreanString() }
        else -> getNextAlarmDateWithTime(
            isAm = isAm,
            hour = hour,
            minute = minute,
        )
    }
}

private fun AlarmDay.toKoreanString(): String {
    return when (this) {
        AlarmDay.SUN -> "일"
        AlarmDay.MON -> "월"
        AlarmDay.TUE -> "화"
        AlarmDay.WED -> "수"
        AlarmDay.THU -> "목"
        AlarmDay.FRI -> "금"
        AlarmDay.SAT -> "토"
    }
}

private fun getNextAlarmDateWithTime(isAm: Boolean, hour: Int, minute: Int): String {
    val now = LocalDateTime.now()

    val alarmHour = if (isAm) {
        if (hour == 12) 0 else hour
    } else {
        if (hour == 12) 12 else hour + 12
    }

    val alarmTime = LocalTime.of(alarmHour, minute)
    val todayAlarm = LocalDateTime.of(now.toLocalDate(), alarmTime)

    // 오늘 시간 이미 지났으면 내일로 설정
    val nextAlarmDate = if (todayAlarm.isAfter(now)) {
        todayAlarm.toLocalDate()
    } else {
        todayAlarm.plusDays(1).toLocalDate()
    }

    // 포맷 설정 (년도가 바뀌면 YY년 MM월 DD일)
    return if (now.year != nextAlarmDate.year) {
        nextAlarmDate.format(DateTimeFormatter.ofPattern("yy년 MM월 dd일"))
    } else {
        nextAlarmDate.format(DateTimeFormatter.ofPattern("MM월 dd일"))
    }
}

@Preview
@Composable
private fun AlarmListItemPreview() {
    OrbitTheme {
        val selectedDays = listOf(AlarmDay.MON, AlarmDay.WED, AlarmDay.FRI).toRepeatDays()
        var isActive by remember { mutableStateOf(true) }

        Column {
            AlarmListItem(
                repeatDays = selectedDays,
                isHolidayAlarmOff = true,
                isAm = true,
                hour = 6,
                minute = 0,
                isActive = isActive,
                onToggleActive = {
                    isActive = !isActive
                },
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(OrbitTheme.colors.gray_800),
            )
            AlarmListItem(
                repeatDays = emptyList<AlarmDay>().toRepeatDays(),
                isHolidayAlarmOff = false,
                isAm = true,
                hour = 6,
                minute = 0,
                isActive = isActive,
                onToggleActive = {
                    isActive = !isActive
                },
            )
        }
    }
}
