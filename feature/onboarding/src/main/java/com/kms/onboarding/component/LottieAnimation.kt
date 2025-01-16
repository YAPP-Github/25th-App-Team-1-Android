package com.kms.onboarding.component

import androidx.annotation.RawRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun LottieAnimation(
    modifier: Modifier = Modifier,
    @RawRes resId: Int,
    iterations: Int = LottieConstants.IterateForever,
    contentScale: ContentScale = ContentScale.FillWidth,
    scaleXAdjustment: Float = 1f,
    scaleYAdjustment: Float = 1f,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = iterations,
    )
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(composition) {
        if (composition != null) {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 500),
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer(
                scaleX = when (contentScale) {
                    ContentScale.Fit -> scaleXAdjustment
                    ContentScale.FillWidth -> 1.2f * scaleXAdjustment
                    ContentScale.FillHeight -> 1.5f * scaleXAdjustment
                    else -> scaleXAdjustment
                },
                scaleY = when (contentScale) {
                    ContentScale.Fit -> scaleYAdjustment
                    ContentScale.FillWidth -> 1.2f * scaleYAdjustment
                    ContentScale.FillHeight -> 1.5f * scaleYAdjustment
                    else -> scaleYAdjustment
                },
                alpha = alpha.value,
            ),
    ) {
        if (composition != null) {
            com.airbnb.lottie.compose.LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
