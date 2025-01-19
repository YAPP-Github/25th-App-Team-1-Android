package com.yapp.ui.component.switch

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme
import kotlin.math.roundToInt

@Composable
fun OrbitSwitch(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    isEnabled: Boolean = true,
    onClick: () -> Unit,
) {
    val (thumbColor, backgroundColor) = if (isChecked && isEnabled) {
        Pair(OrbitTheme.colors.gray_900, OrbitTheme.colors.main)
    } else if (!isEnabled) {
        Pair(OrbitTheme.colors.gray_500, OrbitTheme.colors.gray_700)
    } else {
        Pair(OrbitTheme.colors.gray_300, OrbitTheme.colors.gray_600)
    }

    val density = LocalDensity.current
    val minBound = with(density) { 0.dp.toPx() }
    val maxBound = with(density) { 20.dp.toPx() }
    val state by animateFloatAsState(
        targetValue = if (isChecked && isEnabled) maxBound else minBound,
        animationSpec = tween(durationMillis = 200),
        label = "custom_switch",
    )

    Box(
        modifier = modifier
            .size(width = 46.dp, height = 26.dp)
            .background(
                color = backgroundColor,
                shape = CircleShape,
            )
            .clip(CircleShape)
            .padding(3.dp)
            .pointerInput(isEnabled) {
                detectTapGestures(
                    onTap = {
                        if (isEnabled) {
                            onClick()
                        }
                    },
                )
            },
    ) {
        Box(
            modifier = Modifier
                .offset { IntOffset(state.roundToInt(), 0) }
                .size(20.dp)
                .background(
                    color = thumbColor,
                    shape = CircleShape,
                ),
        )
    }
}

@Preview
@Composable
fun OrbitTogglePreview() {
    OrbitTheme {
        var isSelected by remember { mutableStateOf(false) }

        var isEnabled by remember { mutableStateOf(true) }

        Column {
            OrbitSwitch(
                isChecked = isSelected,
                isEnabled = isEnabled,
                onClick = {
                    isSelected = !isSelected
                },
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    isEnabled = !isEnabled
                },
            ) {
                Text(
                    text = "스위치 활성화 여부 반영",
                    style = OrbitTheme.typography.title2Medium,
                )
            }
        }
    }
}
