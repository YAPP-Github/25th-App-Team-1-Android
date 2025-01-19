package com.yapp.ui.component.slider

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme

@Composable
fun OrbitSlider(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    trackHeight: Dp = 6.dp,
    thumbSize: Dp = 22.dp,
    thumbColor: Color = Color.White,
    inactiveBarColor: Color = OrbitTheme.colors.gray_600,
    activeBarColor: Color = OrbitTheme.colors.main,
) {
    var thumbX by remember { mutableFloatStateOf(value.toFloat()) }
    var isDragging by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.height(IntrinsicSize.Min),
    ) {
        Canvas(
            modifier = modifier
                .fillMaxWidth()
                .height(trackHeight)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(100.dp)),
        ) {
            val totalWidth = size.width
            val activeWidth = thumbX

            drawRect(
                color = activeBarColor,
                size = size.copy(width = activeWidth),
            )

            drawRect(
                color = inactiveBarColor,
                topLeft = Offset(activeWidth, 0f),
                size = size.copy(width = totalWidth - activeWidth),
            )
        }

        Canvas(
            modifier = modifier
                .fillMaxWidth()
                .height(thumbSize)
                .align(Alignment.Center)
                .pointerInput(true) {
                    detectTapGestures(
                        onPress = { offset ->
                            isDragging = isInCircle(
                                offset.x,
                                offset.y,
                                thumbX,
                                size.height.toFloat() / 2,
                                thumbSize.toPx() / 2,
                            )
                        },
                    )
                }
                .pointerInput(true) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            isDragging = false
                        },
                    ) { _, dragAmount ->
                        if (isDragging) {
                            thumbX += dragAmount
                            thumbX = thumbX.coerceIn(0f, size.width.toFloat())
                            val newValue = ((thumbX / size.width) * 100).toInt().coerceIn(0, 100)
                            onValueChange(newValue)
                        }
                    }
                },
        ) {
            val height = size.height

            drawCircle(
                color = thumbColor,
                radius = thumbSize.toPx() / 2,
                center = Offset(thumbX, height / 2),
            )
        }
    }
}

private fun isInCircle(x: Float, y: Float, centerX: Float, centerY: Float, radius: Float): Boolean {
    val dx = x - centerX
    val dy = y - centerY
    return (dx * dx + dy * dy) <= (radius * radius)
}

@Preview
@Composable
fun PreviewOrbitSlider() {
    var currentValue by remember { mutableStateOf(50) }

    OrbitTheme {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Value: $currentValue",
                style = OrbitTheme.typography.body1Medium,
                color = OrbitTheme.colors.gray_50,
            )

            Spacer(modifier = Modifier.height(20.dp))

            OrbitSlider(
                value = currentValue,
                onValueChange = { currentValue = it },
                trackHeight = 10.dp,
                thumbSize = 22.dp,
                thumbColor = OrbitTheme.colors.white,
                inactiveBarColor = OrbitTheme.colors.gray_600,
                activeBarColor = OrbitTheme.colors.main,
            )
        }
    }
}
