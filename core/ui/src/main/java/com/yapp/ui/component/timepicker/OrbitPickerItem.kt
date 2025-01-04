package com.yapp.ui.component.timepicker

import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.yapp.designsystem.theme.OrbitTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
fun OrbitPickerItem(
    modifier: Modifier = Modifier,
    items: List<String>,
    state: PickerState = rememberPickerState(),
    startIndex: Int = 0,
    visibleItemsCount: Int,
    textModifier: Modifier = Modifier,
    infiniteScroll: Boolean = true,
) {
    val visibleItemsMiddle = visibleItemsCount / 2
    val listScrollCount = if (infiniteScroll) Int.MAX_VALUE else items.size + visibleItemsMiddle * 2
    val listScrollMiddle = listScrollCount / 2
    val listStartIndex = calculateStartIndex(
        infiniteScroll,
        items.size,
        listScrollMiddle,
        visibleItemsMiddle,
        startIndex,
    )

    fun getItem(index: Int) = items.getOrNull(index % items.size) ?: ""

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = listStartIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val itemHeightPixels = remember { mutableIntStateOf(0) }
    val itemHeightDp = with(LocalDensity.current) { itemHeightPixels.intValue.toDp() }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { index -> getItem(index + visibleItemsMiddle) }
            .distinctUntilChanged()
            .collect { item -> state.selectedItem = item }
    }

    Box(modifier = modifier) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeightDp * visibleItemsCount)
                .pointerInput(Unit) { detectVerticalDragGestures { change, _ -> change.consume() } },
        ) {
            items(listScrollCount) { index ->
                Text(
                    text = getItemForIndex(index, items, infiniteScroll, visibleItemsMiddle),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = OrbitTheme.typography.title2SemiBold.copy(color = OrbitTheme.colors.white),
                    modifier = Modifier
                        .onSizeChanged { size -> itemHeightPixels.intValue = size.height }
                        .then(textModifier),
                )
            }
        }
    }
}

/**
 * 무한 스크롤과 초기 시작 인덱스를 기반으로 리스트의 시작 인덱스를 계산합니다.
 */
private fun calculateStartIndex(
    infiniteScroll: Boolean,
    itemSize: Int,
    listScrollMiddle: Int,
    visibleItemsMiddle: Int,
    startIndex: Int,
): Int {
    return if (infiniteScroll) {
        listScrollMiddle - listScrollMiddle % itemSize - visibleItemsMiddle + startIndex
    } else {
        startIndex
    }
}

/**
 * 주어진 인덱스에 해당하는 항목을 반환합니다.
 * 무한 스크롤과 보이는 항목의 개수를 고려합니다.
 */
private fun getItemForIndex(index: Int, items: List<String>, infiniteScroll: Boolean, visibleItemsMiddle: Int): String {
    return if (!infiniteScroll && (index < visibleItemsMiddle || index >= items.size + visibleItemsMiddle)) {
        ""
    } else {
        items.getOrNull(index % items.size) ?: ""
    }
}

/**
 * 콘텐츠의 상단과 하단에 페이딩 효과를 그리는 Modifier를 반환합니다.
 */
private fun Modifier.drawFadingEdge(): Modifier = this.then(
    Modifier.drawWithContent {
        drawContent()
        val fadingEdgeGradient = Brush.verticalGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.9f),
                Color.White.copy(alpha = 0.8f),
                Color.Transparent,
                Color.Transparent,
                Color.White.copy(alpha = 0.8f),
                Color.White.copy(alpha = 0.9f),
            ),
        )
        drawRect(fadingEdgeGradient, size = size)
    },
)

@Composable
@Preview
fun OrbitPickerPreview() {
    OrbitTheme {
        OrbitPickerItem(
            items = (0..100).map { it.toString() },
            state = rememberPickerState(),
            visibleItemsCount = 5,
        )
    }
}
