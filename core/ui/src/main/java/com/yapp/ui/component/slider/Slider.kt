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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.ui.utils.toPx

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
    var thumbX by remember { mutableFloatStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    var sliderSize by remember { mutableStateOf(IntSize.Zero) }
    val startOffset = thumbSize.toPx() / 2 + 1.dp.toPx()

    Box(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .onSizeChanged {
                    size ->
                sliderSize = size
                thumbX = startOffset + (value / 100f) * (sliderSize.width - startOffset * 2)
            },
    ) {
        Canvas(
            modifier = modifier
                .fillMaxWidth()
                .height(trackHeight)
                .align(Alignment.Center),
        ) {
            val totalWidth = sliderSize.width - startOffset * 2
            val normalizedThumbX = thumbX.coerceIn(startOffset, sliderSize.width - startOffset)
            val activeWidth = (normalizedThumbX - startOffset).coerceAtLeast(startOffset)

            val activePath = Path().apply {
                val activeRect = Rect(0f, 0f, activeWidth + 3.dp.toPx(), size.height)
                addRoundRect(
                    RoundRect(
                        rect = activeRect,
                        topLeft = CornerRadius(100.dp.toPx()),
                        bottomLeft = CornerRadius(100.dp.toPx()),
                    ),
                )
            }
            drawPath(
                path = activePath,
                color = activeBarColor,
            )

            drawRoundRect(
                color = inactiveBarColor,
                size = size.copy(width = totalWidth - activeWidth),
                cornerRadius = CornerRadius(100.dp.toPx()),
                topLeft = Offset(activeWidth + 2.dp.toPx(), 0f),
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
                                sliderSize.height.toFloat() / 2,
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
                            thumbX = thumbX.coerceIn(startOffset, sliderSize.width - startOffset)
                            val newValue = (((thumbX - startOffset) / (sliderSize.width - startOffset * 2)) * 100)
                                .toInt()
                                .coerceIn(0, 100)
                            onValueChange(newValue)
                        }
                    }
                },
        ) {
            val height = sliderSize.height.toFloat()

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
