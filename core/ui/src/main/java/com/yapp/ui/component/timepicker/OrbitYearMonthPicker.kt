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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme
import kotlinx.coroutines.launch

@Composable
fun OrbitYearMonthPicker(
    modifier: Modifier = Modifier,
    itemSpacing: Dp = 12.dp,
    initialLunar: String = "음력",
    initialYear: String = "1900",
    initialMonth: String = "1",
    initialDay: String = "01",
    onValueChange: (String, Int, Int, Int) -> Unit,
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

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
            val lunarItems = remember { listOf("음력", "양력") }
            val yearItems = remember { (1900..2024).map { it.toString() } }
            val monthItems = remember { (1..12).map { it.toString() } }
            val dayItems = remember { mutableStateListOf<String>().apply { addAll((1..31).map { it.toString().padStart(2, '0') }) } }

            val lunarPickerState = rememberPickerState(
                selectedItem = lunarItems.indexOf(initialLunar).toString(),
                startIndex = lunarItems.indexOf(initialLunar),
            )
            val yearPickerState = rememberPickerState(
                selectedItem = yearItems.indexOf(initialYear).toString(),
                startIndex = yearItems.indexOf(initialYear),
            )
            val monthPickerState = rememberPickerState(
                selectedItem = monthItems.indexOf(initialMonth).toString(),
                startIndex = monthItems.indexOf(initialMonth),
            )
            val dayPickerState = rememberPickerState(
                selectedItem = dayItems.indexOf(initialDay).toString(),
                startIndex = dayItems.indexOf(initialDay),
            )

            val scope = rememberCoroutineScope()

            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                val totalItemHeight = screenWidth * 0.15f
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .padding(horizontal = screenWidth * 0.05f)
                        .height(totalItemHeight)
                        .background(OrbitTheme.colors.gray_700, shape = RoundedCornerShape(12.dp)),
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = screenWidth * 0.1f),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OrbitPickerItem(
                        state = lunarPickerState,
                        items = lunarItems,
                        visibleItemsCount = 3,
                        itemSpacing = itemSpacing,
                        textStyle = OrbitTheme.typography.title2SemiBold,
                        modifier = Modifier.width(screenWidth * 0.2f),
                        textModifier = Modifier.padding(8.dp),
                        infiniteScroll = false,
                        onValueChange = {
                            onPickerValueChange(lunarPickerState, yearPickerState, monthPickerState, dayPickerState, onValueChange)
                        },
                    )
                    OrbitPickerItem(
                        state = yearPickerState,
                        items = yearItems,
                        visibleItemsCount = 5,
                        itemSpacing = itemSpacing,
                        textStyle = OrbitTheme.typography.title2SemiBold,
                        modifier = Modifier.width(screenWidth * 0.28f),
                        textModifier = Modifier.padding(8.dp),
                        infiniteScroll = true,
                        onValueChange = {
                            onPickerValueChange(lunarPickerState, yearPickerState, monthPickerState, dayPickerState, onValueChange)
                        },
                    )
                    OrbitPickerItem(
                        state = monthPickerState,
                        items = monthItems,
                        visibleItemsCount = 5,
                        itemSpacing = itemSpacing,
                        textStyle = OrbitTheme.typography.title2SemiBold,
                        modifier = Modifier.width(screenWidth * 0.16f),
                        textModifier = Modifier.padding(8.dp),
                        infiniteScroll = false,
                        onValueChange = {
                            scope.launch {
                                val month = monthPickerState.selectedItem.toIntOrNull() ?: 1
                                val year = yearPickerState.selectedItem.toIntOrNull() ?: 1900
                                val maxDay = when (month) {
                                    1, 3, 5, 7, 8, 10, 12 -> 31
                                    4, 6, 9, 11 -> 30
                                    2 -> if (isLeapYear(year)) 29 else 28
                                    else -> 31
                                }

                                dayItems.clear()
                                dayItems.addAll((1..maxDay).map { it.toString().padStart(2, '0') })

                                val currentDay = dayPickerState.selectedItem.toIntOrNull() ?: 1
                                if (currentDay > maxDay) {
                                    dayPickerState.lazyListState.animateScrollToItem(maxDay - 1)
                                }

                                onValueChange(
                                    lunarPickerState.selectedItem,
                                    year,
                                    month,
                                    currentDay.coerceAtMost(maxDay),
                                )
                            }
                        },
                    )

                    OrbitPickerItem(
                        state = dayPickerState,
                        items = dayItems,
                        visibleItemsCount = 5,
                        itemSpacing = itemSpacing,
                        textStyle = OrbitTheme.typography.title2SemiBold,
                        modifier = Modifier.width(screenWidth * 0.16f),
                        textModifier = Modifier.padding(8.dp),
                        infiniteScroll = false, // **여기서 무한 스크롤 비활성화**
                        onValueChange = {
                            onPickerValueChange(lunarPickerState, yearPickerState, monthPickerState, dayPickerState, onValueChange)
                        },
                    )
                }
            }
        }
    }
}

private fun onPickerValueChange(
    lunarPickerState: PickerState,
    yearPickerState: PickerState,
    monthPickerState: PickerState,
    dayPickerState: PickerState,
    onValueChange: (String, Int, Int, Int) -> Unit,
) {
    val lunar = lunarPickerState.selectedItem
    val year = yearPickerState.selectedItem.toIntOrNull() ?: 1900
    val month = monthPickerState.selectedItem.toIntOrNull() ?: 1
    val day = dayPickerState.selectedItem.toIntOrNull() ?: 1

    val maxDay = when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (isLeapYear(year)) 29 else 28
        else -> 31
    }

    val adjustedDay = day.coerceAtMost(maxDay)
    onValueChange(lunar, year, month, adjustedDay)
}

private fun isLeapYear(year: Int): Boolean {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}

@Preview(showBackground = true)
@Composable
fun OrbitYearMonthPickerPreview() {
    OrbitYearMonthPicker { lunar, year, month, day ->
        Log.d("OrbitYearMonthPickerPreview", "lunar: $lunar, year: $year, month: $month, day: $day")
    }
}
