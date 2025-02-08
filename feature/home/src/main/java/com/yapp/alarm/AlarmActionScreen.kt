package com.yapp.alarm

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.ui.component.button.OrbitButton
import com.yapp.ui.utils.heightForScreenPercentage

@Composable
internal fun AlarmActionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xFF496381),
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(
            modifier = Modifier.heightForScreenPercentage(
                0.17f,
            ),
        )

        AlarmTime(
            isAm = true,
            hour = 7,
            minute = 30,
            todayDate = "1월 1일 수요일",
        )

        Spacer(modifier = Modifier.height(102.dp))

        Icon(
            painter = painterResource(id = core.designsystem.R.drawable.ic_alarm_action_character),
            tint = Color(0xFF265894),
            contentDescription = "Alarm Action Character",
        )

        Spacer(modifier = Modifier.height(56.dp))

        AlarmSnoozeButton(
            snoozeInterval = 10,
            snoozeCount = 3,
        )

        Spacer(modifier = Modifier.weight(1f))

        OrbitButton(
            label = "알람끄기",
            enabled = true,
            modifier = Modifier
                .padding(
                    start = 40.dp,
                    end = 40.dp,
                    bottom = 48.dp,
                )
                .height(62.dp),
            onClick = {
            },
        )
    }
}

@Composable
private fun AlarmTime(
    isAm: Boolean,
    hour: Int,
    minute: Int,
    todayDate: String,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = if (isAm) "오전" else "오후",
                style = OrbitTheme.typography.title2Medium,
                color = OrbitTheme.colors.white,
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "$hour:$minute",
                style = OrbitTheme.typography.displaySemiBold,
                color = OrbitTheme.colors.white,
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = todayDate,
            style = OrbitTheme.typography.heading2SemiBold,
            color = OrbitTheme.colors.white,
        )
    }
}

@Composable
private fun AlarmSnoozeButton(
    snoozeInterval: Int,
    snoozeCount: Int,
) {
    Surface(
        color = OrbitTheme.colors.white.copy(
            alpha = 0.3f,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = OrbitTheme.colors.white.copy(
                alpha = 0.2f,
            ),
        ),
        shape = CircleShape,
    ) {
        Row(
            modifier = Modifier.padding(
                start = 20.dp,
                end = 10.dp,
                top = 12.dp,
                bottom = 12.dp,
            ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "${snoozeInterval}분 미루기",
                style = OrbitTheme.typography.headline2SemiBold,
                color = OrbitTheme.colors.white,
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                modifier = Modifier
                    .background(
                        color = OrbitTheme.colors.main.copy(
                            alpha = 0.3f,
                        ),
                        shape = CircleShape,
                    )
                    .padding(
                        horizontal = 10.dp,
                        vertical = 6.dp,
                    ),
                text = if (snoozeCount == -1) "무한" else "${snoozeCount}회",
                style = OrbitTheme.typography.body2Medium,
                color = OrbitTheme.colors.main,
            )
        }
    }
}

@Preview
@Composable
internal fun AlarmActionScreenPreview() {
    OrbitTheme {
        AlarmActionScreen()
    }
}
