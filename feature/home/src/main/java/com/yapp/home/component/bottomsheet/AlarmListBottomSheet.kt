package com.yapp.home.component.bottomsheet

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yapp.alarm.component.AlarmListItem
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.domain.model.Alarm
import com.yapp.home.component.AlarmListDropDownMenu
import com.yapp.ui.component.checkbox.OrbitCheckBox
import feature.home.R

enum class BottomSheetExpandState {
    EXPANDED, HALF_EXPANDED
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AlarmListBottomSheet(
    alarms: List<Alarm>,
    menuExpanded: Boolean = false,
    isAllSelected: Boolean,
    isSelectionMode: Boolean,
    selectedAlarmIds: Set<Long>,
    halfExpandedHeight: Dp = 0.dp,
    onClickAdd: () -> Unit,
    onClickMore: () -> Unit,
    onClickCheckAll: () -> Unit,
    onClickClose: () -> Unit,
    onClickEdit: () -> Unit,
    onDismissRequest: () -> Unit,
    onToggleSelect: (Long) -> Unit,
    onToggleActive: (Long) -> Unit,
    content: @Composable () -> Unit,
) {
    var expandedType by remember { mutableStateOf(BottomSheetExpandState.HALF_EXPANDED) }

    val sheetState = rememberStandardBottomSheetState(
        confirmValueChange = {
            expandedType = when (it) {
                SheetValue.Expanded -> BottomSheetExpandState.EXPANDED
                else -> BottomSheetExpandState.HALF_EXPANDED
            }
            true
        },
    )

    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)

    LaunchedEffect(sheetState.currentValue) {
        expandedType = when (sheetState.currentValue) {
            SheetValue.Expanded -> BottomSheetExpandState.EXPANDED
            else -> BottomSheetExpandState.HALF_EXPANDED
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            AlarmBottomSheetContent(
                modifier = Modifier.fillMaxHeight(),
                alarms = alarms,
                menuExpanded = menuExpanded,
                isSelectionMode = isSelectionMode,
                isAllSelected = isAllSelected,
                selectedAlarmIds = selectedAlarmIds,
                onClickAdd = onClickAdd,
                onClickMore = onClickMore,
                onClickCheckAll = onClickCheckAll,
                onClickClose = onClickClose,
                onClickEdit = onClickEdit,
                expandedType = expandedType,
                onDismissRequest = onDismissRequest,
                onToggleSelect = onToggleSelect,
                onToggleActive = onToggleActive,
            )
        },
        sheetShadowElevation = 0.dp,
        sheetDragHandle = {
            if (expandedType == BottomSheetExpandState.HALF_EXPANDED) {
                CustomDragHandle()
            }
        },
        sheetShape = RectangleShape,
        sheetPeekHeight = halfExpandedHeight,
        sheetContainerColor = Color.Transparent,
    ) {
        content()
    }
}

@Composable
internal fun AlarmBottomSheetContent(
    modifier: Modifier = Modifier,
    alarms: List<Alarm>,
    menuExpanded: Boolean,
    isSelectionMode: Boolean,
    isAllSelected: Boolean,
    selectedAlarmIds: Set<Long>,
    onClickAdd: () -> Unit,
    onClickMore: () -> Unit,
    onClickCheckAll: () -> Unit,
    onClickClose: () -> Unit,
    onClickEdit: () -> Unit,
    onDismissRequest: () -> Unit,
    onToggleSelect: (Long) -> Unit,
    onToggleActive: (Long) -> Unit,
    expandedType: BottomSheetExpandState,
) {
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    val cornerRadius = if (expandedType == BottomSheetExpandState.HALF_EXPANDED) 16.dp else 0.dp
    val topPadding = if (expandedType == BottomSheetExpandState.HALF_EXPANDED) 14.dp else 14.dp + statusBarHeight

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = OrbitTheme.colors.gray_900,
                shape = RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius),
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(topPadding))

        if (isSelectionMode) {
            AlarmSelectionTopBar(
                checked = isAllSelected,
                onClickCheckAll = onClickCheckAll,
                onClickClose = onClickClose,
            )
        } else {
            AlarmListTopBar(
                menuExpanded = menuExpanded,
                onClickAdd = onClickAdd,
                onClickMore = onClickMore,
                onDismissRequest = onDismissRequest,
                onClickEdit = onClickEdit,
            )
        }

        LazyColumn {
            itemsIndexed(alarms) { index, alarm ->
                AlarmListItem(
                    id = alarm.id,
                    repeatDays = alarm.repeatDays,
                    isHolidayAlarmOff = alarm.isHolidayAlarmOff,
                    selectable = isSelectionMode,
                    selected = selectedAlarmIds.contains(alarm.id),
                    onToggleSelect = onToggleSelect,
                    isAm = alarm.isAm,
                    hour = alarm.hour,
                    minute = alarm.minute,
                    isActive = alarm.isAlarmActive,
                    onToggleActive = onToggleActive,
                )
                if (index != alarms.size - 1) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(OrbitTheme.colors.gray_800)
                            .padding(horizontal = 24.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun AlarmListTopBar(
    modifier: Modifier = Modifier,
    menuExpanded: Boolean,
    onClickAdd: () -> Unit,
    onClickMore: () -> Unit,
    onDismissRequest: () -> Unit,
    onClickEdit: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp, start = 20.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(id = R.string.alarm_list_bottom_sheet_title),
            style = OrbitTheme.typography.heading2SemiBold,
            color = OrbitTheme.colors.white,
        )

        Spacer(modifier = Modifier.weight(1f))

        CircleIconButton(
            iconRes = core.designsystem.R.drawable.ic_plus,
            contentDescription = "Plus",
            onClick = onClickAdd,
        )

        Spacer(modifier = Modifier.width(4.dp))

        Box {
            CircleIconButton(
                iconRes = core.designsystem.R.drawable.ic_more,
                contentDescription = "More",
                onClick = onClickMore,
            )

            if (menuExpanded) {
                AlarmListDropDownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = onDismissRequest,
                    onClickEdit = onClickEdit,
                )
            }
        }
    }
}

@Composable
private fun AlarmSelectionTopBar(
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    onClickCheckAll: () -> Unit,
    onClickClose: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp, start = 24.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OrbitCheckBox(
            checked = checked,
            onCheckedChange = { onClickCheckAll() },
        )

        Spacer(modifier = Modifier.width(22.dp))

        Text(
            text = stringResource(id = R.string.alarm_list_bottom_sheet_selection_title),
            style = OrbitTheme.typography.heading2SemiBold,
            color = OrbitTheme.colors.white,
        )

        Spacer(modifier = Modifier.weight(1f))

        CircleIconButton(
            iconRes = core.designsystem.R.drawable.ic_close,
            contentDescription = "Close",
            onClick = onClickClose,
        )

        Spacer(modifier = Modifier.width(4.dp))
    }
}

@Composable
private fun CircleIconButton(
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int,
    contentDescription: String?,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .size(32.dp)
            .clip(CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription,
            tint = OrbitTheme.colors.white,
        )
    }
}

@Preview
@Composable
private fun AlarmSelectionTopBarPreview() {
    OrbitTheme {
        AlarmSelectionTopBar(
            checked = true,
            onClickCheckAll = { },
            onClickClose = { },
        )
    }
}

@Composable
private fun CustomDragHandle() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(vertical = 7.dp),
        contentAlignment = Alignment.Center,
    ) {
        Spacer(
            modifier = Modifier
                .size(width = 36.dp, height = 5.dp)
                .background(
                    color = OrbitTheme.colors.white.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(2.dp),
                ),
        )
    }
}
