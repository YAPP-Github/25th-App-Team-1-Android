package com.yapp.fortune

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable

@Composable
fun FortunePager(pagerState: PagerState) {
    HorizontalPager(
        state = pagerState,
    ) { page ->
        FortunePage(page)
    }
}
