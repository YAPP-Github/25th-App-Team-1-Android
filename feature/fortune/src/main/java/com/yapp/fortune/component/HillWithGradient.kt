package com.yapp.fortune.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
internal fun HillWithGradient() {
    val density = LocalDensity.current
    val hillTopY = with(density) { (LocalConfiguration.current.screenHeightDp.dp * 0.35f).toPx() }

    Canvas(
        modifier = Modifier.fillMaxSize(),
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val circleRadius = canvasWidth / 0.65f

        val gradientBrush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF4583D4),
                Color(0xFF4891F0),
            ),
            startY = hillTopY,
            endY = canvasHeight,
        )

        drawRoundRect(
            brush = gradientBrush,
            topLeft = Offset(0f, hillTopY + circleRadius),
            size = Size(canvasWidth, canvasHeight - hillTopY),
            cornerRadius = CornerRadius(0f, 0f),
        )

        drawIntoCanvas { canvas ->
            val paint = Paint().asFrameworkPaint().apply {
                isAntiAlias = true
                shader = android.graphics.LinearGradient(
                    0f,
                    hillTopY,
                    0f,
                    hillTopY + circleRadius,
                    intArrayOf(Color(0xFF4583D4).toArgb(), Color(0xFF4891F0).toArgb()),
                    null,
                    android.graphics.Shader.TileMode.CLAMP,
                )
                maskFilter = android.graphics.BlurMaskFilter(15f, android.graphics.BlurMaskFilter.Blur.NORMAL)
            }

            canvas.nativeCanvas.drawCircle(
                canvasWidth / 2,
                hillTopY + circleRadius,
                circleRadius,
                paint,
            )
        }
    }
}
