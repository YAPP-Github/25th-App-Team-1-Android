package com.yapp.alarm.component.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.ui.component.OrbitBottomSheet
import com.yapp.ui.component.button.OrbitButton
import com.yapp.ui.component.radiobutton.OrbitRadioButton
import com.yapp.ui.component.switch.OrbitSwitch
import feature.home.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AlarmSnoozeBottomSheet(
    isSnoozeEnabled: Boolean,
    snoozeIntervalIndex: Int,
    snoozeIntervals: List<String>,
    onIntervalSelected: (Int) -> Unit,
    snoozeCountIndex: Int,
    snoozeCounts: List<String>,
    onSnoozeToggle: () -> Unit,
    onCountSelected: (Int) -> Unit,
    onComplete: () -> Unit,
    isSheetOpen: Boolean,
    onDismiss: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    OrbitBottomSheet(
        isSheetOpen = isSheetOpen,
        sheetState = sheetState,
        onDismissRequest = {
            scope.launch {
                sheetState.hide()
            }.invokeOnCompletion { onDismiss() }
        },
    ) {
        BottomSheetContent(
            isSnoozeEnabled = isSnoozeEnabled,
            snoozeIntervalIndex = snoozeIntervalIndex,
            snoozeIntervals = snoozeIntervals,
            onIntervalSelected = onIntervalSelected,
            snoozeCountIndex = snoozeCountIndex,
            snoozeCounts = snoozeCounts,
            onSnoozeToggle = onSnoozeToggle,
            onCountSelected = onCountSelected,
            onComplete = {
                scope.launch {
                    sheetState.hide()
                }.invokeOnCompletion { onComplete() }
            },
        )
    }
}

@Composable
private fun BottomSheetContent(
    isSnoozeEnabled: Boolean,
    snoozeIntervalIndex: Int,
    snoozeIntervals: List<String>,
    onIntervalSelected: (Int) -> Unit,
    snoozeCountIndex: Int,
    snoozeCounts: List<String>,
    onSnoozeToggle: () -> Unit,
    onCountSelected: (Int) -> Unit,
    onComplete: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HeaderSection(isSnoozeEnabled, onSnoozeToggle)
        Spacer(modifier = Modifier.height(20.dp))
        SelectorSection(
            title = stringResource(id = R.string.alarm_add_edit_interval),
            selectedIndex = if (isSnoozeEnabled) snoozeIntervalIndex else -1,
            items = snoozeIntervals,
            isEnabled = isSnoozeEnabled,
            onItemSelected = onIntervalSelected,
        )
        Spacer(modifier = Modifier.height(32.dp))
        SelectorSection(
            title = stringResource(id = R.string.alarm_add_edit_repeat_count),
            selectedIndex = if (isSnoozeEnabled) snoozeCountIndex else -1,
            items = snoozeCounts,
            isEnabled = isSnoozeEnabled,
            onItemSelected = onCountSelected,
        )
        Spacer(modifier = Modifier.height(20.dp))
        if (isSnoozeEnabled) {
            AlarmSnoozeMessage(
                interval = snoozeIntervals[snoozeIntervalIndex],
                count = snoozeCounts[snoozeCountIndex],
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        OrbitButton(
            label = stringResource(id = R.string.alarm_add_edit_complete),
            enabled = true,
            containerColor = OrbitTheme.colors.gray_600,
            contentColor = OrbitTheme.colors.white,
            pressedContainerColor = OrbitTheme.colors.gray_500,
            pressedContentColor = OrbitTheme.colors.white.copy(alpha = 0.7f),
            onClick = onComplete,
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun HeaderSection(isSnoozeEnabled: Boolean, onSnoozeToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(id = R.string.alarm_add_edit_alarm_snooze),
            style = OrbitTheme.typography.heading2SemiBold,
            color = OrbitTheme.colors.white,
        )
        Spacer(modifier = Modifier.weight(1f))
        OrbitSwitch(
            isChecked = isSnoozeEnabled,
            isEnabled = true,
            onClick = onSnoozeToggle,
        )
    }
}

@Composable
private fun SelectorSection(
    title: String,
    selectedIndex: Int,
    items: List<String>,
    isEnabled: Boolean,
    onItemSelected: (Int) -> Unit,
) {
    Column {
        Text(
            text = title,
            style = OrbitTheme.typography.headline2Medium,
            color = OrbitTheme.colors.gray_50,
        )
        Spacer(modifier = Modifier.height(16.dp))
        SelectorItems(items, selectedIndex, isEnabled, onItemSelected)
    }
}

@Composable
private fun SelectorItems(
    items: List<String>,
    selectedIndex: Int,
    isEnabled: Boolean,
    onItemSelected: (Int) -> Unit,
) {
    Box {
        Column {
            Spacer(modifier = Modifier.height(7.dp))
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .padding(horizontal = 6.dp)
                    .background(OrbitTheme.colors.gray_600),
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            items.forEachIndexed { index, item ->
                Column(horizontalAlignment = getAlignment(index, items.size)) {
                    OrbitRadioButton(
                        isSelected = (index == selectedIndex && isEnabled),
                        isEnabled = isEnabled,
                        onClick = { if (isEnabled) onItemSelected(index) },
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = item,
                        style = OrbitTheme.typography.body1Medium,
                        color = OrbitTheme.colors.gray_50,
                    )
                }
            }
        }
    }
}

private fun getAlignment(index: Int, size: Int): Alignment.Horizontal =
    when (index) {
        0 -> Alignment.Start
        size - 1 -> Alignment.End
        else -> Alignment.CenterHorizontally
    }

@Composable
private fun AlarmSnoozeMessage(interval: String, count: String) {
    val formattedCount = if (count == stringResource(id = R.string.alarm_add_edit_repeat_count_infinite)) "${count}번" else count

    Box(
        modifier = Modifier
            .background(
                color = OrbitTheme.colors.gray_700,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(id = R.string.alarm_add_edit_alarm_snooze_description, interval, formattedCount),
            style = OrbitTheme.typography.label1Medium,
            color = OrbitTheme.colors.main,
        )
    }
}

@Preview
@Composable
private fun AlarmSnoozeBottomSheetPreview() {
    var isSnoozeEnabled by remember { mutableStateOf(true) }
    var snoozeIntervalIndex by remember { mutableIntStateOf(2) }
    var snoozeCountIndex by remember { mutableIntStateOf(1) }
    var isSheetOpen by remember { mutableStateOf(true) }

    OrbitTheme {
        AlarmSnoozeBottomSheet(
            isSnoozeEnabled = isSnoozeEnabled,
            snoozeIntervalIndex = snoozeIntervalIndex,
            snoozeCountIndex = snoozeCountIndex,
            snoozeIntervals = listOf(1, 3, 5, 10, 15).map {
                stringResource(id = R.string.alarm_add_edit_interval_minute, it)
            },
            snoozeCounts = listOf(
                stringResource(id = R.string.alarm_add_edit_repeat_count_times, 1),
                stringResource(id = R.string.alarm_add_edit_repeat_count_times, 3),
                stringResource(id = R.string.alarm_add_edit_repeat_count_times, 5),
                stringResource(id = R.string.alarm_add_edit_repeat_count_times, 10),
                stringResource(id = R.string.alarm_add_edit_repeat_count_infinite),
            ),
            onSnoozeToggle = { isSnoozeEnabled = !isSnoozeEnabled },
            onIntervalSelected = { index -> snoozeIntervalIndex = index },
            onCountSelected = { index -> snoozeCountIndex = index },
            onComplete = { isSheetOpen = false },
            isSheetOpen = isSheetOpen,
            onDismiss = { isSheetOpen = false },
        )
    }
}
