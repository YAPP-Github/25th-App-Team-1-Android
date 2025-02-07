package com.yapp.fortune.page

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.yapp.fortune.FortuneContract
import kotlinx.coroutines.launch

@Composable
fun FortunePager(
    state: FortuneContract.State,
    pagerState: PagerState,
    onNextStep: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        if (pagerState.currentPage < pagerState.pageCount - 1) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                )
            },
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
