package com.yapp.ui.component.switch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme

@Composable
fun OrbitSwitch(
    isSelected: Boolean,
    onClick: (Boolean) -> Unit,
) {
    val thumbColor = if (isSelected) {
        OrbitTheme.colors.gray_900
    } else {
        OrbitTheme.colors.gray_300
    }

    Switch(
        checked = isSelected,
        onCheckedChange = onClick,
        thumbContent = {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(
                        color = thumbColor,
                        shape = CircleShape
                    )
            )
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color.Transparent,
            checkedTrackColor = OrbitTheme.colors.main,
            uncheckedThumbColor = Color.Transparent,
            uncheckedTrackColor = OrbitTheme.colors.gray_600,
            uncheckedBorderColor = Color.Transparent
        )
    )
}

@Preview
@Composable
fun OrbitTogglePreview() {
    OrbitTheme {
        var isSelected by remember { mutableStateOf(false) }

        OrbitSwitch(
            isSelected = isSelected,
            onClick = {
                isSelected = it
            }
        )
    }
}
