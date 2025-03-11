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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme

@Composable
fun OrbitYearMonthPicker(
    modifier: Modifier = Modifier,
    itemSpacing: Dp = 12.dp,
    initialLunar: String = "양력",
    initialYear: String = "2000",
    initialMonth: String = "1",
    initialDay: String = "1",
    onValueChange: (String, Int, Int, Int) -> Unit,
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val lunarState = remember { mutableStateOf(initialLunar) }
    val yearState = remember { mutableIntStateOf(initialYear.toInt()) }
    val monthState = remember { mutableIntStateOf(initialMonth.toInt()) }

    val maxDay = getMaxDaysInMonth(yearState.intValue, monthState.intValue)
    val dayItems = (1..maxDay).map { it.toString() }

    val startIndexYear = (1900..2024).map { it.toString() }.indexOf(initialYear).takeIf { it >= 0 } ?: 0
    val startIndexMonth = (1..12).map { it.toString() }.indexOf(initialMonth).takeIf { it >= 0 } ?: 0
    val startIndexDay = dayItems.indexOf(initialDay).takeIf { it >= 0 } ?: 0

    val dayState = remember { mutableIntStateOf(initialDay.toInt()) }

    val yearPickerState = rememberPickerState(startIndex = startIndexYear)
    val monthPickerState = rememberPickerState(startIndex = startIndexMonth)
    val dayPickerState = rememberPickerState(startIndex = startIndexDay)

    LaunchedEffect(yearState.intValue, monthState.intValue) {
        val newMaxDay = getMaxDaysInMonth(yearState.intValue, monthState.intValue)
        if (dayState.intValue > newMaxDay) {
            dayState.intValue = newMaxDay
        }
    }

    LaunchedEffect(lunarState.value, yearState.intValue, monthState.intValue, dayState.intValue) {
        onValueChange(lunarState.value, yearState.intValue, monthState.intValue, dayState.intValue)
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.background(OrbitTheme.colors.gray_900),
        ) {
            val lunarItems = listOf("양력", "음력")
            val yearItems = (1900..2024).map { it.toString() }
            val monthItems = (1..12).map { it.toString() }

            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .padding(horizontal = screenWidth * 0.05f)
                        .height(50.dp)
                        .background(OrbitTheme.colors.gray_700, shape = RoundedCornerShape(12.dp)),
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = screenWidth * 0.1f),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OrbitPickerItem(
                        items = lunarItems,
                        visibleItemsCount = 3,
                        itemSpacing = itemSpacing,
                        textStyle = OrbitTheme.typography.title2SemiBold,
                        modifier = Modifier.width(screenWidth * 0.2f),
                        textModifier = Modifier.padding(8.dp),
                        infiniteScroll = false,
                        onValueChange = { lunarState.value = it },
                    )
                    OrbitPickerItem(
                        items = yearItems,
                        state = yearPickerState,
                        visibleItemsCount = 5,
                        itemSpacing = itemSpacing,
                        textStyle = OrbitTheme.typography.title2SemiBold,
                        modifier = Modifier.width(screenWidth * 0.28f),
                        textModifier = Modifier.padding(8.dp),
                        infiniteScroll = false,
                        onValueChange = { yearState.intValue = it.toInt() },
                    )
                    OrbitPickerItem(
                        items = monthItems,
                        state = monthPickerState,
                        visibleItemsCount = 5,
                        itemSpacing = itemSpacing,
                        textStyle = OrbitTheme.typography.title2SemiBold,
                        modifier = Modifier.width(screenWidth * 0.16f),
                        textModifier = Modifier.padding(8.dp),
                        infiniteScroll = false,
                        onValueChange = { monthState.intValue = it.toInt() },
                    )
                    OrbitPickerItem(
                        items = dayItems,
                        state = dayPickerState,
                        visibleItemsCount = 5,
                        itemSpacing = itemSpacing,
                        textStyle = OrbitTheme.typography.title2SemiBold,
                        modifier = Modifier.width(screenWidth * 0.16f),
                        textModifier = Modifier.padding(8.dp),
                        infiniteScroll = false,
                        onValueChange = { dayState.intValue = it.toInt() },
                    )
                }
            }
        }
    }
}

/**
 * 특정 연도와 월에 따른 최대 일 수를 반환.
 */
private fun getMaxDaysInMonth(year: Int, month: Int): Int {
    return when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (isLeapYear(year)) 29 else 28
        else -> 31
    }
}

/**
 * 윤년 계산
 */
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
