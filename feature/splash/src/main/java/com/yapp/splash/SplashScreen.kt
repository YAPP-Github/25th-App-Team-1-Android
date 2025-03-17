package com.yapp.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yapp.analytics.AnalyticsEvent
import com.yapp.analytics.LocalAnalyticsHelper
import com.yapp.common.navigation.OrbitNavigator
import com.yapp.common.navigation.destination.SplashDestination
import com.yapp.designsystem.theme.OrbitTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SplashRoute(
    navigator: OrbitNavigator,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    val analyticsHelper = LocalAnalyticsHelper.current

    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val sideEffect = viewModel.container.sideEffectFlow

    LaunchedEffect(Unit) {
        // 테스트 로그
        analyticsHelper.logEvent(AnalyticsEvent("TEST"))
    }

    LaunchedEffect(sideEffect) {
        sideEffect.collectLatest { effect ->
            when (effect) {
                is SplashContract.SideEffect.Navigate -> {
                    navigator.navigateTo(
                        route = effect.route,
                        popUpTo = SplashDestination.Route.route,
                        inclusive = true,
                    )
                }
            }
        }
    }

    SplashScreen(state = state)
}

@Composable
fun SplashScreen(
    state: SplashContract.State,
) {
    val alpha by animateFloatAsState(
        targetValue = if (state.isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "logoFade",
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OrbitTheme.colors.gray_900),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(id = core.designsystem.R.drawable.ic_splash_logo),
            contentDescription = "Splash Logo",
            modifier = Modifier
                .size(120.dp)
                .graphicsLayer(alpha = alpha),
        )
    }
}
