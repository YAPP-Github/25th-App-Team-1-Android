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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme
import java.util.Locale

@Composable
fun OrbitPicker() {
    Surface(
        modifier = Modifier
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

            val amPmPickerState = rememberPickerState()
            val hourPickerState = rememberPickerState()
            val minutePickerState = rememberPickerState()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .height(48.dp)
                        .background(OrbitTheme.colors.gray_700, shape = RoundedCornerShape(8.dp)),
                )
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    OrbitPickerItem(
                        state = amPmPickerState,
                        items = amPmItems,
                        visibleItemsCount = 3,
                        infiniteScroll = false,
                        modifier = Modifier.weight(1f),
                        textModifier = Modifier.padding(8.dp),
                    )
                    OrbitPickerItem(
                        state = hourPickerState,
                        items = hourItems,
                        visibleItemsCount = 5,
                        infiniteScroll = true,
                        modifier = Modifier.weight(1f),
                        textModifier = Modifier.padding(8.dp),
                    )
                    OrbitPickerItem(
                        state = minutePickerState,
                        items = minuteItems,
                        visibleItemsCount = 5,
                        infiniteScroll = true,
                        modifier = Modifier.weight(1f),
                        textModifier = Modifier.padding(8.dp),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun BottomSheetPickerPreview() {
    OrbitPicker()
}
