package com.yapp.mission

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.mission.component.FlipCard
import com.yapp.mission.component.MissionProgressBar
import com.yapp.ui.component.dialog.OrbitDialog
import com.yapp.ui.component.lottie.LottieAnimation
import com.yapp.ui.utils.heightForScreenPercentage
import com.yapp.ui.utils.paddingForScreenPercentage

@Composable
fun MissionProgressRoute(viewModel: MissionViewModel = hiltViewModel()) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val shakeDetector = remember { ShakeDetector(context) { viewModel.onAction(MissionContract.Action.ShakeCard) } }

    LaunchedEffect(Unit) {
        shakeDetector.start()
        viewModel.onAction(MissionContract.Action.StartOverlayTimer)
    }

    DisposableEffect(Unit) {
        onDispose { shakeDetector.stop() }
    }

    MissionProgressScreen(
        state = state,
        onShowExitDialog = { viewModel.onAction(MissionContract.Action.ShowExitDialog) },
        onDismissExitDialog = { viewModel.onAction(MissionContract.Action.HideExitDialog) },
    )
}

@Composable
fun MissionProgressScreen(
    state: MissionContract.State,
    onShowExitDialog: () -> Unit,
    onDismissExitDialog: () -> Unit,

) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Image(
            painter = painterResource(id = core.designsystem.R.drawable.img_mission_progress_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize(),
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.heightForScreenPercentage(0.066f))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.TopEnd,
                ) {
                    Row(
                        modifier = Modifier
                            .wrapContentWidth()
                            .clickable(onClick = { onShowExitDialog() }),
                    ) {
                        Icon(
                            painter = painterResource(id = core.designsystem.R.drawable.ic_cancel),
                            contentDescription = null,
                            tint = OrbitTheme.colors.white,
                            modifier = Modifier.size(24.dp),
                        )
                        Text(
                            text = "나가기",
                            color = OrbitTheme.colors.white,
                            style = OrbitTheme.typography.body1SemiBold,
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .align(Alignment.CenterVertically),
                        )
                    }
                }

                Spacer(modifier = Modifier.heightForScreenPercentage(0.0246f))
                MissionProgressBar(
                    currentProgress = state.clickCount,
                    totalProgress = 10,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .padding(horizontal = 20.dp)
                        .alpha(if (state.showOverlay) 0f else 1f),
                )
                Spacer(modifier = Modifier.heightForScreenPercentage(0.06f))
                Text(
                    text = "10회를 흔들어야 운세를 받아요",
                    color = OrbitTheme.colors.white,
                    style = OrbitTheme.typography.heading2SemiBold,
                    modifier = Modifier.alpha(if (state.showOverlay) 0f else 1f),
                )
                Spacer(modifier = Modifier.heightForScreenPercentage(0.005f))
                Text(
                    text = state.clickCount.toString(),
                    color = OrbitTheme.colors.white,
                    style = OrbitTheme.typography.displaySemiBold,
                    modifier = Modifier.alpha(if (state.showOverlay) 0f else 1f),
                )
                Spacer(modifier = Modifier.heightForScreenPercentage(0.0665f))
                FlipCard(
                    state = state,
                )
            }
        }

        if (state.showOverlay) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(OrbitTheme.colors.gray_900.copy(alpha = 0.7f))
                    .pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                awaitPointerEvent()
                            }
                        }
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.heightForScreenPercentage(0.226f))

                AnimatedVisibility(
                    visible = state.showOverlayText,
                    enter = scaleIn(
                        initialScale = 0.8f,
                        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
                    ) + fadeIn(animationSpec = tween(durationMillis = 300)),
                ) {
                    Text(
                        text = "흔들기 시작",
                        color = OrbitTheme.colors.white,
                        style = OrbitTheme.typography.title1Bold,
                    )
                }
            }
        }

        if (state.showExitDialog) {
            OrbitDialog(
                title = "나가면 운세를 받을 수 없어요",
                message = "미션을 수행하지 않고 나가시겠어요?",
                confirmText = "나가기",
                cancelText = "취소",
                onConfirm = onDismissExitDialog,
                onCancel = onDismissExitDialog,
            )
        }

        if (state.isMissionCompleted) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(OrbitTheme.colors.gray_900.copy(alpha = 0.7f))
                    .pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                awaitPointerEvent()
                            }
                        }
                    },
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    LottieAnimation(
                        modifier = Modifier
                            .matchParentSize(),
                        scaleXAdjustment = 1.3f,
                        scaleYAdjustment = 1.3f,
                        resId = core.designsystem.R.raw.mission_success,
                        iterations = 1,

                    )
                    Text(
                        text = "미션 성공!",
                        color = OrbitTheme.colors.white,
                        style = OrbitTheme.typography.title1Bold,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .paddingForScreenPercentage(topPercentage = 0.564f),
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun MissionProgressRoutePreview() {
    MissionProgressScreen(
        state = MissionContract.State(),
        onShowExitDialog = {},
        onDismissExitDialog = {},
    )
}
