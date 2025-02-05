package com.yapp.alarm

import android.net.Uri
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yapp.alarm.component.AlarmCheckItem
import com.yapp.alarm.component.AlarmDayButton
import com.yapp.alarm.component.bottomsheet.AlarmSnoozeBottomSheet
import com.yapp.alarm.component.bottomsheet.AlarmSoundBottomSheet
import com.yapp.common.navigation.OrbitNavigator
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.domain.model.AlarmDay
import com.yapp.domain.model.AlarmSound
import com.yapp.domain.model.toJson
import com.yapp.home.ALARM_RESULT_KEY
import com.yapp.ui.component.button.OrbitButton
import com.yapp.ui.component.switch.OrbitSwitch
import com.yapp.ui.component.timepicker.OrbitPicker
import com.yapp.ui.lifecycle.LaunchedEffectWithLifecycle
import feature.home.R
import kotlinx.coroutines.launch

@Composable
fun AlarmAddEditRoute(
    viewModel: AlarmAddEditViewModel = hiltViewModel(),
    navigator: OrbitNavigator,
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val sideEffect = viewModel.container.sideEffectFlow

    LaunchedEffectWithLifecycle(sideEffect) {
        sideEffect.collect { effect ->
            when (effect) {
                is AlarmAddEditContract.SideEffect.NavigateBack -> {
                    navigator.navigateBack()
                }
                is AlarmAddEditContract.SideEffect.Navigate -> {
                    navigator.navigateTo(
                        route = effect.route,
                        popUpTo = effect.popUpTo,
                        inclusive = effect.inclusive,
                    )
                }
                is AlarmAddEditContract.SideEffect.SaveAlarm -> {
                    navigator.navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(ALARM_RESULT_KEY, effect.alarm.toJson())
                    navigator.navController.popBackStack()
                }
            }
        }
    }

    AlarmAddEditScreen(
        stateProvider = { state },
        eventDispatcher = viewModel::processAction,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmAddEditScreen(
    stateProvider: () -> AlarmAddEditContract.State,
    eventDispatcher: (AlarmAddEditContract.Action) -> Unit,
) {
    val state = stateProvider()
    val snoozeState = state.snoozeState
    val snoozeBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val soundBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AlarmAddEditTopBar(
            title = state.timeState.alarmMessage,
            onBack = { eventDispatcher(AlarmAddEditContract.Action.ClickBack) },
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
            state = state,
            processAction = eventDispatcher,
        )
        Spacer(modifier = Modifier.height(24.dp))
        OrbitButton(
            label = stringResource(R.string.alarm_add_edit_save),
            onClick = { eventDispatcher(AlarmAddEditContract.Action.ClickSave) },
            enabled = true,
            modifier = Modifier
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 12.dp,
                ),
        )
    }

    AlarmSnoozeBottomSheet(
        snoozeEnabled = snoozeState.isSnoozeEnabled,
        snoozeIntervalIndex = snoozeState.snoozeIntervalIndex,
        snoozeCountIndex = snoozeState.snoozeCountIndex,
        snoozeIntervals = snoozeState.snoozeIntervals,
        snoozeCounts = snoozeState.snoozeCounts,
        onSnoozeToggle = { eventDispatcher(AlarmAddEditContract.Action.ToggleSnoozeEnabled) },
        onIntervalSelected = { index -> eventDispatcher(AlarmAddEditContract.Action.UpdateSnoozeInterval(index)) },
        onCountSelected = { index -> eventDispatcher(AlarmAddEditContract.Action.UpdateSnoozeCount(index)) },
        onComplete = {
            scope.launch {
                snoozeBottomSheetState.hide()
            }.invokeOnCompletion {
                eventDispatcher(AlarmAddEditContract.Action.ToggleBottomSheetOpen(AlarmAddEditContract.BottomSheetType.SnoozeSetting))
            }
        },
        isSheetOpen = state.bottomSheetState == AlarmAddEditContract.BottomSheetType.SnoozeSetting,
        onDismiss = {
            scope.launch {
                snoozeBottomSheetState.hide()
            }.invokeOnCompletion {
                eventDispatcher(AlarmAddEditContract.Action.ToggleBottomSheetOpen(AlarmAddEditContract.BottomSheetType.SnoozeSetting))
            }
        },
    )

    AlarmSoundBottomSheet(
        vibrationEnabled = state.soundState.isVibrationEnabled,
        soundEnabled = state.soundState.isSoundEnabled,
        soundVolume = state.soundState.soundVolume,
        soundIndex = state.soundState.soundIndex,
        sounds = state.soundState.sounds,
        onVibrationToggle = { eventDispatcher(AlarmAddEditContract.Action.ToggleVibrationEnabled) },
        onSoundToggle = { eventDispatcher(AlarmAddEditContract.Action.ToggleSoundEnabled) },
        onVolumeChanged = { eventDispatcher(AlarmAddEditContract.Action.UpdateSoundVolume(it)) },
        onSoundSelected = { eventDispatcher(AlarmAddEditContract.Action.UpdateSoundIndex(it)) },
        onComplete = {
            scope.launch {
                soundBottomSheetState.hide()
            }.invokeOnCompletion {
                eventDispatcher(AlarmAddEditContract.Action.ToggleBottomSheetOpen(AlarmAddEditContract.BottomSheetType.SoundSetting))
            }
        },
        isSheetOpen = state.bottomSheetState == AlarmAddEditContract.BottomSheetType.SoundSetting,
        onDismiss = {
            scope.launch {
                soundBottomSheetState.hide()
            }.invokeOnCompletion {
                eventDispatcher(AlarmAddEditContract.Action.ToggleBottomSheetOpen(AlarmAddEditContract.BottomSheetType.SoundSetting))
            }
        },
    )
}

@Composable
private fun AlarmAddEditTopBar(
    title: String,
    onBack: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(56.dp),
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
    state: AlarmAddEditContract.State,
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
            state = state.daySelectionState,
            processAction = processAction,
        )
        AlarmAddEditDisableHolidaySwitch(
            state = state.holidayState,
            processAction = processAction,
        )
        Spacer(
            modifier = Modifier.fillMaxWidth()
                .height(1.dp)
                .padding(horizontal = 20.dp)
                .background(OrbitTheme.colors.gray_700),
        )

        AlarmAddEditSettingItem(
            label = stringResource(id = R.string.alarm_add_edit_alarm_snooze),
            description = if (state.snoozeState.isSnoozeEnabled) {
                stringResource(
                    id = R.string.alarm_add_edit_alarm_selected_option,
                    state.snoozeState.snoozeIntervals[state.snoozeState.snoozeIntervalIndex],
                    state.snoozeState.snoozeCounts[state.snoozeState.snoozeCountIndex],
                )
            } else {
                stringResource(id = R.string.alarm_add_edit_alarm_selected_option_none)
            },
            onClick = { processAction(AlarmAddEditContract.Action.ToggleBottomSheetOpen(AlarmAddEditContract.BottomSheetType.SnoozeSetting)) },
        )
        Spacer(
            modifier = Modifier.fillMaxWidth()
                .height(1.dp)
                .padding(horizontal = 20.dp)
                .background(OrbitTheme.colors.gray_700),
        )
        AlarmAddEditSettingItem(
            label = stringResource(id = R.string.alarm_add_edit_sound),
            description = when {
                state.soundState.isSoundEnabled && state.soundState.isVibrationEnabled -> {
                    "${stringResource(id = R.string.alarm_add_edit_vibration)}, ${
                    state.soundState.sounds.getOrElse(state.soundState.soundIndex) {
                        AlarmSound("", Uri.EMPTY)
                    }.title
                    }"
                }
                state.soundState.isSoundEnabled -> state.soundState.sounds.getOrElse(state.soundState.soundIndex) { AlarmSound("", Uri.EMPTY) }.title
                state.soundState.isVibrationEnabled -> stringResource(id = R.string.alarm_add_edit_vibration)
                else -> stringResource(id = R.string.alarm_add_edit_alarm_selected_option_none)
            },
            onClick = { processAction(AlarmAddEditContract.Action.ToggleBottomSheetOpen(AlarmAddEditContract.BottomSheetType.SoundSetting)) },
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
            modifier = Modifier.width(80.dp),
            style = OrbitTheme.typography.body1SemiBold,
            color = OrbitTheme.colors.white,
        )
        Text(
            description,
            modifier = Modifier.weight(1f),
            style = OrbitTheme.typography.body2Regular,
            color = OrbitTheme.colors.gray_50,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.End,
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
    state: AlarmAddEditContract.AlarmDaySelectionState,
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
                isPressed = state.isWeekdaysChecked,
                onClick = {
                    processAction(AlarmAddEditContract.Action.ToggleWeekdaysChecked)
                },
            )
            Spacer(modifier = Modifier.width(2.dp))
            AlarmCheckItem(
                label = stringResource(id = R.string.alarm_add_edit_weekends),
                isPressed = state.isWeekendsChecked,
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
            state.days.forEach { day ->
                AlarmDayButton(
                    label = stringResource(id = day.getLabelStringRes()),
                    isPressed = state.selectedDays.contains(day),
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
    state: AlarmAddEditContract.AlarmHolidayState,
    processAction: (AlarmAddEditContract.Action) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                end = 20.dp,
                bottom = 16.dp,
            ),
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
            isChecked = state.isDisableHolidayChecked,
            isEnabled = state.isDisableHolidayEnabled,
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
        state = AlarmAddEditContract.State(
            timeState = AlarmAddEditContract.AlarmTimeState(
                currentAmPm = "AM",
                currentHour = 9,
                currentMinute = 30,
            ),
            daySelectionState = AlarmAddEditContract.AlarmDaySelectionState(
                isWeekdaysChecked = true,
                isWeekendsChecked = false,
                selectedDays = setOf(AlarmDay.MON, AlarmDay.TUE),
                days = AlarmDay.entries.toSet(),
            ),
            holidayState = AlarmAddEditContract.AlarmHolidayState(
                isDisableHolidayChecked = false,
            ),
        ),
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
                timeState = AlarmAddEditContract.AlarmTimeState(
                    currentAmPm = "AM",
                    currentHour = 9,
                    currentMinute = 30,
                ),
                daySelectionState = AlarmAddEditContract.AlarmDaySelectionState(
                    isWeekdaysChecked = true,
                    isWeekendsChecked = false,
                    selectedDays = setOf(AlarmDay.MON, AlarmDay.TUE),
                    days = AlarmDay.entries.toSet(),
                ),
                holidayState = AlarmAddEditContract.AlarmHolidayState(
                    isDisableHolidayChecked = false,
                ),
            )
        },
        eventDispatcher = { },
    )
}
