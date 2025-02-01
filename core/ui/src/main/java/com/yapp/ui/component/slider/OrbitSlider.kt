package com.yapp.ui.component.slider

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.ui.utils.toPx

@Composable
fun OrbitSlider(
    enabled: Boolean,
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    trackHeight: Dp = 6.dp,
    thumbSize: Dp = 22.dp,
    thumbColor: Color = Color.White,
    disabledColor: Color = OrbitTheme.colors.gray_500,
    inactiveBarColor: Color = OrbitTheme.colors.gray_600,
    activeBarColor: Color = OrbitTheme.colors.main,
) {
    val thumbRadius = thumbSize.toPx() / 2
    var sliderWidth by remember { mutableFloatStateOf(0f) }
    val startOffset = thumbRadius + 1.dp.toPx()

    var thumbX = remember(value, startOffset, sliderWidth) {
        startOffset + (value / 100f) * (sliderWidth - startOffset * 2)
    }

    var isDragging by remember { mutableStateOf(false) }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(thumbSize)
            .then(
                if (enabled) {
                    Modifier
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = { offset ->
                                    isDragging = isInCircle(
                                        offset.x,
                                        offset.y,
                                        thumbX,
                                        thumbRadius,
                                        thumbRadius,
                                    )
                                },
                                onTap = { offset ->
                                    thumbX = offset.x.coerceIn(startOffset, sliderWidth - startOffset)
                                    val newValue = (((thumbX - startOffset) / (sliderWidth - 2 * startOffset)) * 100)
                                        .toInt()
                                        .coerceIn(0, 100)
                                    onValueChange(newValue)
                                },
                            )
                        }
                        .pointerInput(Unit) {
                            detectHorizontalDragGestures(
                                onDragEnd = { isDragging = false },
                            ) { _, dragAmount ->
                                if (isDragging) {
                                    thumbX += dragAmount
                                    thumbX = thumbX.coerceIn(startOffset, sliderWidth - startOffset)
                                    val newValue = (((thumbX - startOffset) / (sliderWidth - 2 * startOffset)) * 100)
                                        .toInt()
                                        .coerceIn(0, 100)
                                    onValueChange(newValue)
                                }
                            }
                        }
                } else {
                    Modifier
                },
            ),
    ) {
        sliderWidth = size.width

        val normalizedThumbX by derivedStateOf {
            thumbX.coerceIn(startOffset, sliderWidth - startOffset)
        }
        val activeWidth = (normalizedThumbX - startOffset).coerceAtLeast(0f)

        drawRoundRect(
            color = if (enabled) activeBarColor else disabledColor,
            size = Size(activeWidth + 3.dp.toPx(), trackHeight.toPx()),
            topLeft = Offset(0f, (size.height - trackHeight.toPx()) / 2),
            cornerRadius = CornerRadius(100.dp.toPx(), 100.dp.toPx()),
        )

        drawRoundRect(
            color = inactiveBarColor,
            size = Size(size.width - activeWidth - 5.dp.toPx(), trackHeight.toPx()),
            topLeft = Offset(activeWidth + 2.dp.toPx(), (size.height - trackHeight.toPx()) / 2),
            cornerRadius = CornerRadius(100.dp.toPx(), 100.dp.toPx()),
        )

        drawCircle(
            color = thumbColor,
            radius = thumbSize.toPx() / 2,
            center = Offset(normalizedThumbX, size.height / 2),
        )
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
    var currentValue by remember { mutableIntStateOf(50) }

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
                enabled = false,
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
