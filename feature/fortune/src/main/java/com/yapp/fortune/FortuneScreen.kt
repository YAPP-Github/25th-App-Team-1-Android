package com.yapp.fortune

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.fortune.component.FortuneTopAppBar
import com.yapp.fortune.component.SlidingIndicator
import com.yapp.fortune.page.FortunePager

@Composable
fun FortuneRoute(
    viewModel: FortuneViewModel = hiltViewModel(),
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState(
        initialPage = state.currentStep,
        pageCount = { state.fortunePages.size + 2 },
    )

    LaunchedEffect(pagerState.currentPage) {
        if (state.currentStep != pagerState.currentPage) {
            viewModel.onAction(FortuneContract.Action.UpdateStep(pagerState.currentPage))
        }
    }

    FortuneScreen(
        state = state,
        pagerState = pagerState,
        onNextStep = { viewModel.onAction(FortuneContract.Action.NextStep) },
        onCloseClick = { viewModel.onAction(FortuneContract.Action.NavigateToHome) },
    )
}

@Composable
fun FortuneScreen(
    state: FortuneContract.State,
    pagerState: PagerState,
    onNextStep: () -> Unit,
    onCloseClick: () -> Unit,
) {
    val backgroundRes = when (state.currentStep) {
        0 -> core.designsystem.R.drawable.ic_fortune_letter_background
        in 1..4 -> core.designsystem.R.drawable.ic_fortune_horoscope_background
        else -> core.designsystem.R.drawable.ic_fortune_complete_background
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4891F0))
            .navigationBarsPadding(),
    ) {
        Image(
            painter = painterResource(id = backgroundRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize(),
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            FortuneTopAppBar(
                titleLabel = "미래에서 온 편지",
                onCloseClick = onCloseClick,
            )

            SlidingIndicator(
                currentIndex = pagerState.currentPage,
                count = 6,
                dotHeight = 5.dp,
                spacing = 4.dp,
                inactiveColor = OrbitTheme.colors.white.copy(0.2f),
                activeColor = OrbitTheme.colors.white,
            )

            FortunePager(state, pagerState, onNextStep)
        }
    }
}

@Composable
@Preview
fun FortuneRoutePreview() {
}
