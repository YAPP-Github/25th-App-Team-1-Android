package com.yapp.alarm.snooze

import Pretendard
import android.graphics.BlurMaskFilter
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.ui.utils.heightForScreenPercentage
import com.yapp.ui.utils.toPx
import feature.home.R
import java.util.Locale

@Composable
internal fun AlarmSnoozeTimerScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF3D5372)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.heightForScreenPercentage(0.14f))

        Text(
            text = stringResource(id = R.string.alarm_snooze_timer_title),
            style = OrbitTheme.typography.heading2SemiBold,
            color = OrbitTheme.colors.white,
        )

        Spacer(modifier = Modifier.heightForScreenPercentage(0.11f))

        AlarmSnoozeTimer(
            remainingSeconds = 200,
            totalSeconds = 300,
        )

        Spacer(modifier = Modifier.weight(1f))

        AlarmOffButton(
            onClick = { },
        )

        Spacer(modifier = Modifier.heightForScreenPercentage(0.06f))
    }
}

@Composable
private fun AlarmSnoozeTimer(
    remainingSeconds: Int,
    totalSeconds: Int,
) {
    Box(
        modifier = Modifier.size(274.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            progress = remainingSeconds.toFloat() / totalSeconds,
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(id = R.string.alarm_snooze_timer_remaining_time),
                style = OrbitTheme.typography.headline2SemiBold,
                color = OrbitTheme.colors.white,
            )

            Text(
                text = formatSecondsToTime(remainingSeconds),
                style = TextStyle(
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Pretendard,
                    lineHeight = 62.sp,
                ),
                color = OrbitTheme.colors.white,
            )
        }
    }
}

@Composable
private fun CircularProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float,
    size: Dp = 274.dp,
    backgroundWidth: Dp = 20.dp,
    progressWidth: Dp = 12.dp,
    progressBlurRadius: Dp = 2.dp,
) {
    val backgroundStrokePx = backgroundWidth.toPx()
    val progressStrokePx = progressWidth.toPx()

    val offset = (backgroundStrokePx - progressStrokePx) / 2

    val backgroundColor = Color.White.copy(alpha = 0.2f)

    val progressBlurEffect = BlurMaskFilter(
        progressBlurRadius.toPx(),
        BlurMaskFilter.Blur.NORMAL,
    )

    val progressPaint = Paint().apply {
        color = OrbitTheme.colors.main.toArgb()
        maskFilter = progressBlurEffect
        style = Paint.Style.STROKE
        strokeWidth = progressStrokePx
        strokeCap = Paint.Cap.ROUND
    }

    Canvas(
        modifier = modifier.size(size),
    ) {
        val center = Offset(size.toPx() / 2, size.toPx() / 2)
        val radius = (size.toPx() - backgroundStrokePx) / 2

        drawCircle(
            color = backgroundColor,
            center = center,
            radius = radius,
            style = Stroke(width = backgroundStrokePx),
        )

        drawIntoCanvas { canvas ->
            val rectF = android.graphics.RectF(
                offset * 2.5f,
                offset * 2.5f,
                size.toPx() - (offset * 2.5f),
                size.toPx() - (offset * 2.5f),
            )
            canvas.nativeCanvas.drawArc(
                rectF,
                -90f,
                360 * progress,
                false,
                progressPaint,
            )
        }
    }
}

private fun formatSecondsToTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format(Locale.getDefault(), "%02d:%02d", minutes, remainingSeconds)
}

@Composable
private fun AlarmOffButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    height: Dp = 58.dp,
    containerColor: Color = OrbitTheme.colors.white.copy(alpha = 0.2f),
    contentColor: Color = OrbitTheme.colors.white,
    shape: Shape = CircleShape,
) {
    Button(
        onClick = onClick,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
        modifier = modifier.height(height),
        contentPadding = PaddingValues(
            horizontal = 55.dp,
        ),
    ) {
        Text(
            text = stringResource(id = R.string.alarm_off_btn),
            style = OrbitTheme.typography.headline2SemiBold,
        )
    }
}

@Preview
@Composable
internal fun PreviewAlarmSnoozeTimerScreen() {
    OrbitTheme {
        AlarmSnoozeTimerScreen()
    }
}
