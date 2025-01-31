package com.yapp.fortune

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.fortune.component.FortuneCharacter
import com.yapp.fortune.component.FortuneTopAppBar
import com.yapp.fortune.component.HillWithGradient
import com.yapp.fortune.component.SlidingIndicator
import com.yapp.ui.utils.paddingForScreenPercentage

@Composable
fun FortuneRoute() {
    FortuneScreen()
}

@Composable
fun FortuneScreen() {
    val pagerState = rememberPagerState { 6 }

    val backgroundRes = when (pagerState.currentPage) {
        0 -> core.designsystem.R.drawable.ic_fortune_letter_background
        else -> core.designsystem.R.drawable.ic_fortune_horoscope_background
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4891F0)),
    ) {
        Image(
            painter = painterResource(id = backgroundRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize(),
        )
        if (pagerState.currentPage == 0) {
            Image(
                painter = painterResource(id = core.designsystem.R.drawable.ic_100_buble),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .paddingForScreenPercentage(topPercentage = 0.178f),
            )
            FortuneCharacter(
                modifier = Modifier
                    .paddingForScreenPercentage(topPercentage = 0.243f)
                    .zIndex(1f)
                    .align(Alignment.TopCenter),
                fortuneScore = 100,
            )
            HillWithGradient()
        }
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            FortuneTopAppBar(onCloseClick = {})
            SlidingIndicator(
                pagerState = pagerState,
                count = 6,
                dotHeight = 5.dp,
                spacing = 4.dp,
                inactiveColor = OrbitTheme.colors.white.copy(alpha = 0.2f),
                activeColor = OrbitTheme.colors.white,
            )
            FortunePager(pagerState)
        }
    }
}

@Composable
@Preview
fun FortuneRoutePreview() {
    FortuneScreen()
}
