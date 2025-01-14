package com.yapp.ui.component.timepicker

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun OrbitPicker(
    modifier: Modifier = Modifier,
    itemSpacing: Dp = 2.dp,
    selectedAmPm: String = "오후",
    selectedHour: Int = 0,
    selectedMinute: Int = 0,
    amPmStartIndex: Int = 0,
    hourStartIndex: Int = 5,
    minuteStartIndex: Int = 0,
    onValueChange: (String, Int, Int) -> Unit,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .wrapContentSize()
                .background(OrbitTheme.colors.gray_900),
        ) {
            val amPmItems = remember { listOf("오후", "오전") }
            val hourItems = remember { (1..12).map { it.toString() } }
            val minuteItems = remember { (0..59).map { String.format(Locale.ROOT, "%02d", it) } }

            val amPmListState = rememberLazyListState()
            val hourListState = rememberLazyListState()
            val minuteListState = rememberLazyListState()

            val scope = rememberCoroutineScope()

            Box(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .padding(horizontal = 20.dp)
                        .height(50.dp)
                        .background(OrbitTheme.colors.gray_700, shape = RoundedCornerShape(12.dp)),
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 50.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OrbitPickerItem(
                        items = amPmItems,
                        listState = amPmListState,
                        visibleItemsCount = 3,
                        itemSpacing = itemSpacing,
                        textStyle = OrbitTheme.typography.title2Medium,
                        modifier = Modifier.weight(1f),
                        textModifier = Modifier.padding(8.dp),
                        startIndex = amPmStartIndex,
                        infiniteScroll = false,
                        selectedItem = selectedAmPm,
                        onSelectedItemChange = { amPm ->
                            onValueChange(amPm, selectedHour, selectedMinute)
                        },
                    )

                    OrbitPickerItem(
                        items = hourItems,
                        listState = hourListState,
                        visibleItemsCount = 5,
                        itemSpacing = itemSpacing,
                        textStyle = OrbitTheme.typography.title2Medium,
                        modifier = Modifier.weight(1f),
                        textModifier = Modifier.padding(8.dp),
                        startIndex = hourStartIndex,
                        infiniteScroll = true,
                        selectedItem = selectedHour.toString(),
                        onSelectedItemChange = { hour ->
                            onValueChange(selectedAmPm, hour.toInt(), selectedMinute)
                        },
                        onScrollCompleted = {
                            scope.launch {
                                val currentIndex = amPmListState.firstVisibleItemIndex % amPmItems.size
                                val nextIndex = (currentIndex + 1) % amPmItems.size
                                amPmListState.animateScrollToItem(nextIndex)
                            }
                        },
                    )

                    OrbitPickerItem(
                        items = minuteItems,
                        listState = minuteListState,
                        visibleItemsCount = 5,
                        itemSpacing = itemSpacing,
                        textStyle = OrbitTheme.typography.title2Medium,
                        modifier = Modifier.weight(1f),
                        textModifier = Modifier.padding(8.dp),
                        startIndex = minuteStartIndex,
                        infiniteScroll = true,
                        selectedItem = selectedMinute.toString(),
                        onSelectedItemChange = { minute ->
                            onValueChange(selectedAmPm, selectedHour, minute.toInt())
                        },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomSheetPickerPreview() {
    OrbitPicker { amPm, hour, minute ->
        Log.d("OrbitPicker", "amPm: $amPm, hour: $hour, minute: $minute")
    }
}
