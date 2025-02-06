package com.yapp.ui.utils

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

fun LazyListState.reachedBottom(
    limitCount: Int = 6,
    triggerOnEnd: Boolean = false,
): Boolean {
    val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
    return (triggerOnEnd && lastVisibleItem?.index == layoutInfo.totalItemsCount - 1) ||
        lastVisibleItem?.index != 0 && lastVisibleItem?.index == layoutInfo.totalItemsCount - (limitCount + 1)
}

@Composable
fun LazyListState.OnLoadMore(
    limitCount: Int = 6,
    loadOnBottom: Boolean = true,
    hasMoreData: Boolean,
    isLoading: Boolean,
    action: () -> Unit,
) {
    val reached by remember {
        derivedStateOf {
            reachedBottom(limitCount = limitCount, triggerOnEnd = loadOnBottom)
        }
    }
    LaunchedEffect(reached, hasMoreData, isLoading) {
        if (reached && hasMoreData && !isLoading) action()
    }
}
