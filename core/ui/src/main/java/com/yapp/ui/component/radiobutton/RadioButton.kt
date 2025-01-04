package com.yapp.ui.component.radiobutton

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
    isSelected: Boolean,
    onClick: (Boolean) -> Unit,
) {
    val backgroundColor = if (isSelected) {
        OrbitTheme.colors.main.copy(alpha = 0.3f)
    } else {
        OrbitTheme.colors.gray_600
    }

    val circleColor = if (isSelected) {
        OrbitTheme.colors.main
    } else {
        OrbitTheme.colors.gray_600
    }

    val circleSize = if (isSelected) {
        12.dp
    } else {
        8.dp
    }

    Box(
        modifier = Modifier
            .size(20.dp)
            .background(
                color = backgroundColor,
                shape = CircleShape,
            )
            .clip(CircleShape)
            .clickable {
                onClick(!isSelected)
            },
        contentAlignment = Alignment.Center,
    ) {
        Spacer(
            modifier = Modifier.size(circleSize)
                .background(
                    color = circleColor,
                    shape = CircleShape,
                )
                .clip(CircleShape),
        )
    }
}

@Preview
@Composable
fun OrbitRadioButtonPreview() {
    OrbitTheme {
        var isSelected by remember { mutableStateOf(false) }

        OrbitRadioButton(
            isSelected = isSelected,
            onClick = {
                isSelected = it
            },
        )
    }
}
