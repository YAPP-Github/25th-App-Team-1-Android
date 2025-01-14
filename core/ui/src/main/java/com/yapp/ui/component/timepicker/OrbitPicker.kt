package com.yapp.ui.component.timepicker

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme
import java.util.Locale

@Composable
fun OrbitPicker(
    modifier: Modifier = Modifier,
    itemSpacing: Dp = 2.dp,
    initialAmPm: String = "오전",
    initialHour: String = "1",
    initialMinute: String = "00",
    selectedAmPm: String,
    selectedHour: Int,
    selectedMinute: Int,
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

            val amPmPickerState = rememberPickerState(
                selectedItem = selectedAmPm,
                startIndex = amPmItems.indexOf(initialAmPm),
            )
            val hourPickerState = rememberPickerState(
                selectedItem = selectedHour.toString(),
                startIndex = hourItems.indexOf(initialHour),
            )
            val minutePickerState = rememberPickerState(
                selectedItem = selectedMinute.toString(),
                startIndex = minuteItems.indexOf(initialMinute),
            )

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
                        state = amPmPickerState,
                        items = amPmItems,
                        visibleItemsCount = 3,
                        itemSpacing = itemSpacing,
                        textStyle = OrbitTheme.typography.title2Medium,
                        modifier = Modifier.weight(1f),
                        textModifier = Modifier.padding(8.dp),
                        infiniteScroll = false,
                        onValueChange = {
                            onPickerValueChange(
                                amPmPickerState,
                                hourPickerState,
                                minutePickerState,
                                onValueChange,
                            )
                        },
                    )

                    OrbitPickerItem(
                        state = hourPickerState,
                        items = hourItems,
                        visibleItemsCount = 5,
                        itemSpacing = itemSpacing,
                        textStyle = OrbitTheme.typography.title2Medium,
                        modifier = Modifier.weight(1f),
                        textModifier = Modifier.padding(8.dp),
                        infiniteScroll = true,
                        onValueChange = {
                            onPickerValueChange(
                                amPmPickerState,
                                hourPickerState,
                                minutePickerState,
                                onValueChange,
                            )
                        },
                    )

                    OrbitPickerItem(
                        state = minutePickerState,
                        items = minuteItems,
                        visibleItemsCount = 5,
                        itemSpacing = itemSpacing,
                        textStyle = OrbitTheme.typography.title2Medium,
                        modifier = Modifier.weight(1f),
                        textModifier = Modifier.padding(8.dp),
                        infiniteScroll = true,
                        onValueChange = {
                            onPickerValueChange(
                                amPmPickerState,
                                hourPickerState,
                                minutePickerState,
                                onValueChange,
                            )
                        },
                    )
                }
            }
        }
    }
}

private fun onPickerValueChange(
    amPmState: PickerState,
    hourState: PickerState,
    minuteState: PickerState,
    onValueChange: (String, Int, Int) -> Unit,
) {
    val amPm = amPmState.selectedItem
    val hour = hourState.selectedItem.toIntOrNull() ?: 0
    val minute = minuteState.selectedItem.toIntOrNull() ?: 0
    onValueChange(amPm, hour, minute)
}

@Preview(showBackground = true)
@Composable
fun BottomSheetPickerPreview() {
    var selectedAmPm by remember { mutableStateOf("오전") }
    var selectedHour by remember { mutableIntStateOf(6) }
    var selectedMinute by remember { mutableIntStateOf(0) }

    OrbitPicker(
        selectedAmPm = selectedAmPm,
        selectedHour = selectedHour,
        selectedMinute = selectedMinute,
    ) { amPm, hour, minute ->
        selectedAmPm = amPm
        selectedHour = hour
        selectedMinute = minute
    }
}
