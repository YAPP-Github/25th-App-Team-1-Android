package com.yapp.ui.component.timepicker

import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.ui.utils.toPx
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlin.math.abs

@Composable
fun OrbitPickerItem(
    modifier: Modifier = Modifier,
    items: List<String>,
    state: PickerState = rememberPickerState(),
    startIndex: Int = 0,
    visibleItemsCount: Int,
    textModifier: Modifier = Modifier,
    infiniteScroll: Boolean = true,
    textStyle: TextStyle,
    itemSpacing: Dp,
    onValueChange: (String) -> Unit,
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
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = listStartIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val itemHeightPixels = remember { mutableIntStateOf(0) }
    val itemHeightDp = with(LocalDensity.current) { itemHeightPixels.intValue.toDp() }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .map { layoutInfo ->
                val centerOffset = layoutInfo.viewportStartOffset + (layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset) / 2

                layoutInfo.visibleItemsInfo.minByOrNull { item ->
                    val itemCenter = item.offset + (item.size / 2)
                    abs(itemCenter - centerOffset)
                }?.index
            }
            .distinctUntilChanged()
            .collect { centerIndex ->
                if (centerIndex != null) {
                    val adjustedIndex = centerIndex % items.size
                    val newValue = items[adjustedIndex]
                    if (newValue != state.selectedItem) {
                        state.selectedItem = newValue
                        onValueChange(newValue)
                    }
                }
            }
    }

    val totalItemHeight = itemHeightDp + itemSpacing

    Box(modifier = modifier) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .height(totalItemHeight * visibleItemsCount)
                .pointerInput(Unit) { detectVerticalDragGestures { change, _ -> change.consume() } },
        ) {
            items(listScrollCount) { index ->
                val layoutInfo = listState.layoutInfo
                val viewportCenterOffset = layoutInfo.viewportStartOffset +
                    (layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset) / 2

                val itemInfo = layoutInfo.visibleItemsInfo.find { it.index == index }
                val itemCenterOffset = itemInfo?.offset?.let { it + (itemInfo.size / 2) } ?: 0

                val distanceFromCenter = abs(viewportCenterOffset - itemCenterOffset)
                val maxDistance = totalItemHeight.toPx() * visibleItemsMiddle
                val alpha = ((maxDistance - distanceFromCenter) / maxDistance).coerceIn(0.2f, 1f)
                val scaleY = 1f - (0.4f * (distanceFromCenter / maxDistance)).coerceIn(0f, 0.4f)
                val isSelected = getItemForIndex(index, items, infiniteScroll, visibleItemsMiddle) == state.selectedItem

                Text(
                    text = getItemForIndex(index, items, infiniteScroll, visibleItemsMiddle),
                    maxLines = 1,
                    style = textStyle,
                    color = if (isSelected) OrbitTheme.colors.white else OrbitTheme.colors.white.copy(alpha = alpha),
                    modifier = Modifier
                        .padding(vertical = itemSpacing / 2)
                        .graphicsLayer(scaleY = scaleY)
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

@Composable
@Preview
fun OrbitPickerPreview() {
    OrbitTheme {
        OrbitPickerItem(
            items = (0..100).map { it.toString() },
            state = rememberPickerState(),
            visibleItemsCount = 5,
            textStyle = TextStyle.Default,
            itemSpacing = 8.dp,
            onValueChange = {},
        )
    }
}
