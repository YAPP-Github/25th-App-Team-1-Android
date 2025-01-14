package com.yapp.alarm

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.ui.component.timepicker.OrbitPicker
import com.yapp.ui.lifecycle.LaunchedEffectWithLifecycle

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
            title = "1일 12시간 후에 울려요",
            onBack = { },
        )
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            OrbitPicker(
                selectedAmPm = state.currentAmPm,
                selectedHour = state.currentHour,
                selectedMinute = state.currentMinute,
            ) { amPm, hour, minute ->
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
                    horizontal = 20.dp,
                    vertical = 12.dp,
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
