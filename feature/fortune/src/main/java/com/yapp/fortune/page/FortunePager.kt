package com.yapp.fortune.page

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yapp.fortune.FortuneContract

@Composable
fun FortunePager(
    state: FortuneContract.State,
    pagerState: PagerState,
    onNextStep: () -> Unit,
) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
    ) { page ->
        when (page) {
            0 -> FortuneFirstPage()
            in 1..4 -> {
                val index = (page - 1).coerceIn(0, state.fortunePages.lastIndex)
                FortunePageLayout(state.fortunePages[index])
            }
            5 -> FortuneCompletePage(
                hasReward = state.hasReward,
                onCompleteClick = onNextStep,
            )
            else -> DefaultFortunePage(page)
        }
    }
}
