package com.yapp.mission.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme
import kotlinx.coroutines.launch

@Composable
fun MissionProgressBar(
    currentProgress: Int,
    totalProgress: Int,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(20.dp),
    progressColor: Color = OrbitTheme.colors.main,
    trackColor: Color = OrbitTheme.colors.white.copy(alpha = 0.2f),
    cornerRadius: Dp = 30.dp,
) {
    val animatedProgress = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(currentProgress) {
        scope.launch {
            animatedProgress.animateTo(
                targetValue = currentProgress / totalProgress.toFloat(),
                animationSpec = tween(
                    durationMillis = 500,
                    easing = FastOutSlowInEasing,
                ),
            )
        }
    }

    Box(
        modifier = modifier
            .background(trackColor, shape = RoundedCornerShape(cornerRadius)),
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction = animatedProgress.value)
                .clip(
                    RoundedCornerShape(
                        topStart = cornerRadius,
                        bottomStart = cornerRadius,
                        topEnd = if (currentProgress == totalProgress) cornerRadius else 0.dp,
                        bottomEnd = if (currentProgress == totalProgress) cornerRadius else 0.dp,
                    ),
                )
                .background(progressColor),
        )
    }
}

@Composable
@Preview
fun MissionProgressBarPreview() {
    OrbitTheme {
        MissionProgressBar(
            currentProgress = 3,
            totalProgress = 5,
        )
    }
}
