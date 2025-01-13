package com.yapp.alarm.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme

@Composable
internal fun AlarmDayButton(
    label: String,
    isPressed: Boolean,
    onClick: () -> Unit,
) {
    val containerColor = if (isPressed) {
        OrbitTheme.colors.main.copy(alpha = 0.1f)
    } else {
        OrbitTheme.colors.gray_700
    }

    val contentColor = if (isPressed) {
        OrbitTheme.colors.main
    } else {
        OrbitTheme.colors.gray_300
    }

    val borderColor = if (isPressed) {
        OrbitTheme.colors.main
    } else {
        Color.Transparent
    }

    Box(
        modifier = Modifier
            .size(36.dp)
            .background(
                color = containerColor,
                shape = RoundedCornerShape(8.dp),
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp),
            )
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = OrbitTheme.typography.body1Medium,
            color = contentColor,
        )
    }
}

@Preview
@Composable
fun AlarmDayButtonPreview() {
    var isPressed by remember { mutableStateOf(false) }

    AlarmDayButton(
        label = "월",
        isPressed = isPressed,
        onClick = { isPressed = !isPressed },
    )
}
