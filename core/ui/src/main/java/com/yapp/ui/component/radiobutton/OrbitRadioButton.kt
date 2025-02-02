package com.yapp.ui.component.radiobutton

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme

@Composable
fun OrbitRadioButton(
    selected: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val backgroundColor = if (!enabled) {
        OrbitTheme.colors.gray_700
    } else if (selected) {
        OrbitTheme.colors.main.copy(alpha = 0.3f)
    } else {
        OrbitTheme.colors.gray_600
    }

    val circleColor = if (!enabled && selected) {
        OrbitTheme.colors.gray_600
    } else if (!enabled) {
        OrbitTheme.colors.gray_700
    } else if (selected) {
        OrbitTheme.colors.main
    } else {
        OrbitTheme.colors.gray_600
    }

    Box(
        modifier = Modifier
            .size(20.dp)
            .background(
                color = backgroundColor,
                shape = CircleShape,
            )
            .clip(CircleShape)
            .clickable(enabled) {
                onClick()
            },
        contentAlignment = Alignment.Center,
    ) {
        if (selected) {
            Spacer(
                modifier = Modifier.size(12.dp)
                    .background(
                        color = circleColor,
                        shape = CircleShape,
                    )
                    .clip(CircleShape),
            )
        }
    }
}

@Preview
@Composable
fun OrbitRadioButtonPreview() {
    OrbitTheme {
        var isSelected by remember { mutableStateOf(true) }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OrbitRadioButton(
                selected = isSelected,
                enabled = true,
                onClick = {
                    isSelected = !isSelected
                },
            )

            OrbitRadioButton(
                selected = true,
                enabled = true,
                onClick = { },
            )

            OrbitRadioButton(
                selected = false,
                enabled = false,
                onClick = { },
            )

            OrbitRadioButton(
                selected = true,
                enabled = false,
                onClick = { },
            )
        }
    }
}
