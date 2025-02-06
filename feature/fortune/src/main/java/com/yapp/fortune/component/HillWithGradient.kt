package com.yapp.fortune.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
internal fun HillWithGradient(
    heightPercentage: Float = 0.22f,
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenHeightDp = configuration.screenHeightDp.dp
    val hillTopY = screenHeightDp * heightPercentage

    val hillTopYPx = with(density) { hillTopY.toPx() }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                clip = true
                shape = RoundedCornerShape(bottomStart = 0.dp, bottomEnd = 0.dp)
            },
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val circleRadius = canvasWidth / 0.65f

        val gradientBrush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF4583D4),
                Color(0xFF4891F0),
            ),
            startY = hillTopYPx,
            endY = canvasHeight,
        )
        clipRect(left = 0f, top = hillTopYPx, right = canvasWidth, bottom = canvasHeight) {
            drawRoundRect(
                brush = gradientBrush,
                topLeft = Offset(0f, hillTopYPx + circleRadius),
                size = Size(canvasWidth, canvasHeight - hillTopYPx),
                cornerRadius = CornerRadius(0f, 0f),
            )

            drawIntoCanvas { canvas ->
                val paint = Paint().asFrameworkPaint().apply {
                    isAntiAlias = true
                    shader = android.graphics.LinearGradient(
                        0f,
                        hillTopYPx,
                        0f,
                        hillTopYPx + circleRadius,
                        intArrayOf(Color(0xFF4583D4).toArgb(), Color(0xFF4891F0).toArgb()),
                        null,
                        android.graphics.Shader.TileMode.CLAMP,
                    )
                    maskFilter = android.graphics.BlurMaskFilter(15f, android.graphics.BlurMaskFilter.Blur.NORMAL)
                }

                canvas.nativeCanvas.drawCircle(
                    canvasWidth / 2,
                    hillTopYPx + circleRadius,
                    circleRadius,
                    paint,
                )
            }
        }
    }
}
