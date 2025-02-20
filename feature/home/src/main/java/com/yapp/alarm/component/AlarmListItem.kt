package com.yapp.alarm.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import com.yapp.ui.component.checkbox.OrbitCheckBox
import com.yapp.ui.component.switch.OrbitSwitch
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AlarmListItem(
    modifier: Modifier = Modifier,
    id: Long,
    repeatDays: Int,
    isHolidayAlarmOff: Boolean,
    selectable: Boolean = false,
    selected: Boolean = false,
    onClick: (Long) -> Unit,
    onToggleSelect: (Long) -> Unit,
    isAm: Boolean,
    hour: Int,
    minute: Int,
    isActive: Boolean,
    onToggleActive: (Long) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    CompositionLocalProvider(
        LocalRippleConfiguration provides RippleConfiguration(
            rippleAlpha = RippleAlpha(
                pressedAlpha = 1f,
                focusedAlpha = 1f,
                hoveredAlpha = 1f,
                draggedAlpha = 1f,
            ),
        ),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    if (selected) OrbitTheme.colors.gray_800 else OrbitTheme.colors.gray_900,
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = ripple(
                        color = OrbitTheme.colors.gray_800,
                    ),
                ) {
                    if (selectable) {
                        onToggleSelect(id)
                    } else {
                        onClick(id)
                    }
                }
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (selectable) {
                OrbitCheckBox(
                    checked = selected,
                    onCheckedChange = { onToggleSelect(id) },
                )
                Spacer(modifier = Modifier.width(26.dp))
            }

            AlarmListItemContent(
                repeatDays = repeatDays,
                isActive = isActive,
                isHolidayAlarmOff = isHolidayAlarmOff,
                isAm = isAm,
                hour = hour,
                minute = minute,
            )

            if (!selectable) {
                Spacer(modifier = Modifier.weight(1f))
                OrbitSwitch(
                    isChecked = isActive,
                ) {
                    onToggleActive(id)
                }
            }
        }
    }
}

@Composable
private fun AlarmListItemContent(
    repeatDays: Int,
    isActive: Boolean,
    isHolidayAlarmOff: Boolean,
    isAm: Boolean,
    hour: Int,
    minute: Int,
) {
    val (textColor, iconColor) = if (isActive) {
        OrbitTheme.colors.gray_300 to OrbitTheme.colors.gray_200
    } else {
        OrbitTheme.colors.gray_500 to OrbitTheme.colors.gray_500
    }

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = repeatDays.toRepeatDaysString(isAm, hour, minute),
                style = OrbitTheme.typography.label1SemiBold,
                color = textColor,
            )
            if (isHolidayAlarmOff) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(id = core.designsystem.R.drawable.ic_holiday),
                    contentDescription = "Holiday Alarm Off",
                    tint = iconColor,
                    modifier = Modifier.size(12.dp),
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = if (isAm) "오전" else "오후",
                style = OrbitTheme.typography.title2Medium,
                color = if (isActive) OrbitTheme.colors.white else OrbitTheme.colors.gray_500,
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = "$hour",
                style = OrbitTheme.typography.title2Medium,
                color = if (isActive) OrbitTheme.colors.white else OrbitTheme.colors.gray_500,
            )

            Spacer(modifier = Modifier.width(3.dp))

            Text(
                text = ":",
                style = OrbitTheme.typography.title2Medium,
                color = if (isActive) OrbitTheme.colors.white else OrbitTheme.colors.gray_500,
                modifier = Modifier.offset(y = (-2).dp),
            )

            Spacer(modifier = Modifier.width(3.dp))

            Text(
                text = minute.toString().padStart(2, '0'),
                style = OrbitTheme.typography.title2Medium,
                color = if (isActive) OrbitTheme.colors.white else OrbitTheme.colors.gray_500,
            )
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

    return nextAlarmDate.format(DateTimeFormatter.ofPattern("M월 d일"))
}

@Preview
@Composable
private fun AlarmListItemPreview() {
    OrbitTheme {
        val selectedDays = setOf(AlarmDay.MON, AlarmDay.WED, AlarmDay.FRI).toRepeatDays()
        var isActive by remember { mutableStateOf(true) }
        var selected by remember { mutableStateOf(true) }

        Column {
            AlarmListItem(
                id = 0,
                repeatDays = selectedDays,
                isHolidayAlarmOff = true,
                selectable = true,
                selected = selected,
                isAm = true,
                hour = 6,
                minute = 0,
                isActive = isActive,
                onClick = { },
                onToggleActive = {
                    isActive = !isActive
                },
                onToggleSelect = {
                    selected = !selected
                },
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(OrbitTheme.colors.gray_800)
                    .padding(horizontal = 24.dp),
            )
            AlarmListItem(
                id = 0,
                repeatDays = emptySet<AlarmDay>().toRepeatDays(),
                isHolidayAlarmOff = false,
                selectable = false,
                selected = false,
                isAm = true,
                hour = 6,
                minute = 0,
                isActive = isActive,
                onClick = { },
                onToggleActive = {
                    isActive = !isActive
                },
                onToggleSelect = { },
            )
        }
    }
}

@Preview
@Composable
private fun AlarmListItemContentPreview() {
    OrbitTheme {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "오전",
                    style = OrbitTheme.typography.title2Medium,
                    color = OrbitTheme.colors.white,
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "6:00",
                    style = OrbitTheme.typography.title2Medium,
                    color = OrbitTheme.colors.white,
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "오전",
                    style = OrbitTheme.typography.title2Medium,
                    color = OrbitTheme.colors.white,
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "6",
                    style = OrbitTheme.typography.title2Medium,
                    color = OrbitTheme.colors.white,
                )

                Spacer(modifier = Modifier.width(3.dp))

                Text(
                    modifier = Modifier.offset(
                        y = (-2).dp,
                    ),
                    text = ":",
                    style = OrbitTheme.typography.title2Medium,
                    color = OrbitTheme.colors.white,
                )

                Spacer(modifier = Modifier.width(3.dp))

                Text(
                    text = "00",
                    style = OrbitTheme.typography.title2Medium,
                    color = OrbitTheme.colors.white,
                )
            }
        }
    }
}
