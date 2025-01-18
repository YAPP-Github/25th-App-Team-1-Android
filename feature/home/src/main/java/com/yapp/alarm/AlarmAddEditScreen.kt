package com.yapp.alarm

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yapp.alarm.component.AlarmCheckItem
import com.yapp.alarm.component.AlarmDayButton
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.ui.component.button.OrbitButton
import com.yapp.ui.component.switch.OrbitSwitch
import com.yapp.ui.component.timepicker.OrbitPicker
import com.yapp.ui.lifecycle.LaunchedEffectWithLifecycle
import feature.home.R

@Composable
fun AlarmAddEditRoute(
    viewModel: AlarmAddEditViewModel = hiltViewModel(),
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val sideEffect = viewModel.container.sideEffectFlow

    LaunchedEffectWithLifecycle(sideEffect) {
        sideEffect.collect { effect ->
            when (effect) {
                is AlarmAddEditContract.SideEffect.NavigateBack -> {
                }

                is AlarmAddEditContract.SideEffect.Navigate -> {
                }
            }
        }
    }

    AlarmAddEditScreen(
        stateProvider = { state },
        eventDispatcher = viewModel::processAction,
    )
}

@Composable
fun AlarmAddEditScreen(
    stateProvider: () -> AlarmAddEditContract.State,
    eventDispatcher: (AlarmAddEditContract.Action) -> Unit,
) {
    val state = stateProvider()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AlarmAddEditTopBar(
            title = state.alarmMessage,
            onBack = { },
        )
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            OrbitPicker { amPm, hour, minute ->
                eventDispatcher(AlarmAddEditContract.Action.UpdateAlarmTime(amPm, hour, minute))
            }
        }
        AlarmAddEditSettingsSection(
            modifier = Modifier.padding(horizontal = 20.dp),
            isWeekdaysChecked = state.isWeekdaysChecked,
            isWeekendsChecked = state.isWeekendsChecked,
            selectedDays = state.selectedDays,
            isDisableHolidayChecked = state.isDisableHolidayChecked,
            days = state.days,
            processAction = eventDispatcher,
        )
        Spacer(modifier = Modifier.height(24.dp))
        OrbitButton(
            label = stringResource(R.string.alarm_add_edit_save),
            onClick = { },
            enabled = true,
            modifier = Modifier
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 12.dp,
                )
                .height(56.dp)
                .fillMaxWidth(),
        )
    }
}

@Composable
private fun AlarmAddEditTopBar(
    title: String,
    onBack: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .statusBarsPadding(),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(id = core.designsystem.R.drawable.ic_back),
            contentDescription = "Back",
            tint = OrbitTheme.colors.white,
            modifier = Modifier
                .clickable(onClick = onBack)
                .padding(start = 20.dp)
                .align(Alignment.CenterStart),
        )

        Text(
            title,
            style = OrbitTheme.typography.body1SemiBold,
            color = OrbitTheme.colors.white,
        )
    }
}

@Composable
private fun AlarmAddEditSettingsSection(
    modifier: Modifier = Modifier,
    isWeekdaysChecked: Boolean,
    isWeekendsChecked: Boolean,
    selectedDays: Set<AlarmDay>,
    isDisableHolidayChecked: Boolean,
    days: Set<AlarmDay>,
    processAction: (AlarmAddEditContract.Action) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = OrbitTheme.colors.gray_800,
                shape = RoundedCornerShape(12.dp),
            )
            .clip(
                shape = RoundedCornerShape(12.dp),
            ),
    ) {
        AlarmAddEditSelectDaysSection(
            isWeekdaysChecked = isWeekdaysChecked,
            isWeekendsChecked = isWeekendsChecked,
            selectedDays = selectedDays,
            days = days,
            processAction = processAction,
        )
        AlarmAddEditDisableHolidaySwitch(
            isDisableHolidayChecked = isDisableHolidayChecked,
            processAction = processAction,
        )
        Spacer(
            modifier = Modifier.fillMaxWidth()
                .height(1.dp)
                .padding(horizontal = 20.dp)
                .background(OrbitTheme.colors.gray_700),
        )
        AlarmAddEditSettingItem(
            label = "알람 미루기",
            description = "5분, 무한",
            onClick = { },
        )
        Spacer(
            modifier = Modifier.fillMaxWidth()
                .height(1.dp)
                .padding(horizontal = 20.dp)
                .background(OrbitTheme.colors.gray_700),
        )
        AlarmAddEditSettingItem(
            label = "사운드",
            description = "진동, 알림음1",
            onClick = { },
        )
    }
}

@Composable
private fun AlarmAddEditSettingItem(
    label: String,
    description: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 20.dp,
                vertical = 14.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            label,
            style = OrbitTheme.typography.body1SemiBold,
            color = OrbitTheme.colors.white,
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            description,
            style = OrbitTheme.typography.body2Regular,
            color = OrbitTheme.colors.gray_50,
        )
        Icon(
            painter = painterResource(id = core.designsystem.R.drawable.ic_arrow_right),
            contentDescription = "Arrow",
            tint = OrbitTheme.colors.gray_300,
        )
    }
}

@Composable
private fun AlarmAddEditSelectDaysSection(
    isWeekdaysChecked: Boolean,
    isWeekendsChecked: Boolean,
    selectedDays: Set<AlarmDay>,
    days: Set<AlarmDay>,
    processAction: (AlarmAddEditContract.Action) -> Unit,
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(id = R.string.alarm_add_edit_repeat),
                style = OrbitTheme.typography.body1SemiBold,
                color = OrbitTheme.colors.white,
            )

            Spacer(modifier = Modifier.weight(1f))

            AlarmCheckItem(
                label = stringResource(id = R.string.alarm_add_edit_weekdays),
                isPressed = isWeekdaysChecked,
                onClick = {
                    processAction(AlarmAddEditContract.Action.ToggleWeekdaysChecked)
                },
            )
            Spacer(modifier = Modifier.width(2.dp))
            AlarmCheckItem(
                label = stringResource(id = R.string.alarm_add_edit_weekends),
                isPressed = isWeekendsChecked,
                onClick = {
                    processAction(AlarmAddEditContract.Action.ToggleWeekendsChecked)
                },
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            days.forEach { day ->
                AlarmDayButton(
                    label = stringResource(id = day.label),
                    isPressed = selectedDays.contains(day),
                    onClick = {
                        processAction(AlarmAddEditContract.Action.ToggleDaySelection(day))
                    },
                )
            }
        }
    }
}

@Composable
private fun AlarmAddEditDisableHolidaySwitch(
    isDisableHolidayChecked: Boolean,
    processAction: (AlarmAddEditContract.Action) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = core.designsystem.R.drawable.ic_holiday),
            contentDescription = "Holiday",
            tint = OrbitTheme.colors.gray_400,
            modifier = Modifier.padding(end = 4.dp),
        )
        Text(
            text = stringResource(id = R.string.alarm_add_edit_disable_holiday),
            style = OrbitTheme.typography.label1Medium,
            color = OrbitTheme.colors.gray_400,
        )

        Spacer(modifier = Modifier.weight(1f))

        OrbitSwitch(
            isSelected = isDisableHolidayChecked,
            onClick = {
                processAction(AlarmAddEditContract.Action.ToggleDisableHolidayChecked)
            },
        )
    }
}

@Preview
@Composable
fun AlarmAddEditSettingsSectionPreview() {
    AlarmAddEditSettingsSection(
        isWeekdaysChecked = true,
        isWeekendsChecked = false,
        selectedDays = setOf(AlarmDay.MON, AlarmDay.TUE),
        isDisableHolidayChecked = false,
        days = AlarmDay.entries.toSet(),
        processAction = { },
    )
}

@Preview
@Composable
fun AlarmAddEditSettingItemPreview() {
    AlarmAddEditSettingItem(
        label = "알람 미루기",
        description = "5분, 무한",
        onClick = { },
    )
}

@Preview
@Composable
fun AlarmAddEditScreenPreview() {
    AlarmAddEditScreen(
        stateProvider = {
            AlarmAddEditContract.State(
                currentAmPm = "AM",
                currentHour = 9,
                currentMinute = 30,
                isWeekdaysChecked = true,
                isWeekendsChecked = false,
                selectedDays = setOf(AlarmDay.MON, AlarmDay.TUE),
                isDisableHolidayChecked = false,
                days = AlarmDay.entries.toSet(),
            )
        },
        eventDispatcher = { },
    )
}
