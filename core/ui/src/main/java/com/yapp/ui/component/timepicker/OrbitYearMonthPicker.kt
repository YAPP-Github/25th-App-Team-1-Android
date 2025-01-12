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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme
import java.util.Locale

@Composable
fun OrbitYearMonthPicker(
    modifier: Modifier = Modifier,
    itemSpacing: Dp = 12.dp,
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
            val yearItems = remember { (1900..2025).map { it.toString() } }
            val monthItems = remember { (1..12).map { it.toString() } }
            val dayItems = remember { (1..31).map { String.format(Locale.ROOT, "%02d", it) } }

            val lunarPickerState = rememberPickerState()
            val yearPickerState = rememberPickerState()
            val monthPickerState = rememberPickerState()
            val dayPickerState = rememberPickerState()

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
                        onValueChange = { onPickerValueChange(lunarPickerState, yearPickerState, monthPickerState, dayPickerState, onValueChange) },
                    )
                    OrbitPickerItem(
                        state = yearPickerState,
                        items = yearItems,
                        visibleItemsCount = 5,
                        startIndex = 90,
                        itemSpacing = itemSpacing,
                        textStyle = OrbitTheme.typography.title2SemiBold,
                        modifier = Modifier.width(screenWidth * 0.25f),
                        textModifier = Modifier.padding(8.dp),
                        infiniteScroll = true,
                        onValueChange = { onPickerValueChange(lunarPickerState, yearPickerState, monthPickerState, dayPickerState, onValueChange) },
                    )
                    OrbitPickerItem(
                        state = monthPickerState,
                        items = monthItems,
                        visibleItemsCount = 5,
                        itemSpacing = itemSpacing,
                        textStyle = OrbitTheme.typography.title2SemiBold,
                        modifier = Modifier.width(screenWidth * 0.16f),
                        textModifier = Modifier.padding(8.dp),
                        infiniteScroll = true,
                        onValueChange = { onPickerValueChange(lunarPickerState, yearPickerState, monthPickerState, dayPickerState, onValueChange) },
                    )
                    OrbitPickerItem(
                        state = dayPickerState,
                        items = dayItems,
                        visibleItemsCount = 5,
                        itemSpacing = itemSpacing,
                        textStyle = OrbitTheme.typography.title2SemiBold,
                        modifier = Modifier.width(screenWidth * 0.16f),
                        textModifier = Modifier.padding(8.dp),
                        infiniteScroll = true,
                        onValueChange = { onPickerValueChange(lunarPickerState, yearPickerState, monthPickerState, dayPickerState, onValueChange) },
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

    onValueChange(lunar, year, month, day)
}

@Preview(showBackground = true)
@Composable
fun OrbitYearMonthPickerPreview() {
    OrbitYearMonthPicker { lunar, year, month, day ->
        Log.d("OrbitYearMonthPicker", "lunar: $lunar, year: $year, month: $month, day: $day")
    }
}
