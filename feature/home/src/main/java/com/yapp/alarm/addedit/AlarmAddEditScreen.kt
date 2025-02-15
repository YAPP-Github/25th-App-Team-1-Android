package com.yapp.alarm.addedit

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
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
import com.yapp.alarm.getLabelStringRes
import com.yapp.common.navigation.OrbitNavigator
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.domain.model.AlarmDay
import com.yapp.domain.model.AlarmSound
import com.yapp.home.ADD_ALARM_RESULT_KEY
import com.yapp.home.DELETE_ALARM_RESULT_KEY
import com.yapp.home.UPDATE_ALARM_RESULT_KEY
import com.yapp.ui.component.button.OrbitButton
import com.yapp.ui.component.dialog.OrbitDialog
import com.yapp.ui.component.lottie.LottieAnimation
import com.yapp.ui.component.snackbar.showCustomSnackBar
import com.yapp.ui.component.switch.OrbitSwitch
import com.yapp.ui.component.timepicker.OrbitPicker
import feature.home.R
import kotlinx.coroutines.launch

@Composable
fun AlarmAddEditRoute(
    viewModel: AlarmAddEditViewModel = hiltViewModel(),
    navigator: OrbitNavigator,
    snackBarHostState: SnackbarHostState,
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val sideEffect = viewModel.container.sideEffectFlow

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(sideEffect) {
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
                        ?.set(ADD_ALARM_RESULT_KEY, effect.id)
                    navigator.navController.popBackStack()
                }
                is AlarmAddEditContract.SideEffect.UpdateAlarm -> {
                    navigator.navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(UPDATE_ALARM_RESULT_KEY, effect.id)
                    navigator.navigateBack()
                }
                is AlarmAddEditContract.SideEffect.DeleteAlarm -> {
                    navigator.navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(DELETE_ALARM_RESULT_KEY, effect.id)
                    navigator.navigateBack()
                }
                is AlarmAddEditContract.SideEffect.ShowSnackBar -> {
                    val result = showCustomSnackBar(
                        scope = coroutineScope,
                        snackBarHostState = snackBarHostState,
                        message = effect.message,
                        actionLabel = effect.label,
                        iconRes = effect.iconRes,
                        bottomPadding = effect.bottomPadding,
                        durationMillis = effect.durationMillis,
                    )

                    when (result) {
                        SnackbarResult.ActionPerformed -> effect.onAction()
                        SnackbarResult.Dismissed -> effect.onDismiss()
                    }
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

    if (state.initialLoading) {
        AlarmAddEditLoadingScreen()
    } else {
        AlarmAddEditContent(
            state = state,
            eventDispatcher = eventDispatcher,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmAddEditContent(
    state: AlarmAddEditContract.State,
    eventDispatcher: (AlarmAddEditContract.Action) -> Unit,
) {
    BackHandler {
        eventDispatcher(AlarmAddEditContract.Action.CheckUnsavedChangesBeforeExit)
    }

    val snoozeState = state.snoozeState
    val snoozeBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val soundBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AlarmAddEditTopBar(
            mode = state.mode,
            title = state.timeState.alarmMessage,
            onBack = { eventDispatcher(AlarmAddEditContract.Action.CheckUnsavedChangesBeforeExit) },
            onDelete = { eventDispatcher(AlarmAddEditContract.Action.ShowDeleteDialog) },
        )
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            OrbitPicker(
                initialAmPm = state.timeState.initialAmPm,
                initialHour = state.timeState.initialHour,
                initialMinute = state.timeState.initialMinute,
            ) { amPm, hour, minute ->
                eventDispatcher(AlarmAddEditContract.Action.SetAlarmTime(amPm, hour, minute))
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
            onClick = { eventDispatcher(AlarmAddEditContract.Action.SaveAlarm) },
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
        onSnoozeToggle = { eventDispatcher(AlarmAddEditContract.Action.ToggleSnoozeOption) },
        onIntervalSelected = { index ->
            eventDispatcher(
                AlarmAddEditContract.Action.SetSnoozeInterval(
                    index,
                ),
            )
        },
        onCountSelected = { index ->
            eventDispatcher(
                AlarmAddEditContract.Action.SetSnoozeRepeatCount(
                    index,
                ),
            )
        },
        onComplete = {
            scope.launch {
                snoozeBottomSheetState.hide()
            }.invokeOnCompletion {
                eventDispatcher(AlarmAddEditContract.Action.ToggleBottomSheet(AlarmAddEditContract.BottomSheetType.SnoozeSetting))
            }
        },
        isSheetOpen = state.bottomSheetState == AlarmAddEditContract.BottomSheetType.SnoozeSetting,
        onDismiss = {
            scope.launch {
                snoozeBottomSheetState.hide()
            }.invokeOnCompletion {
                eventDispatcher(AlarmAddEditContract.Action.ToggleBottomSheet(AlarmAddEditContract.BottomSheetType.SnoozeSetting))
            }
        },
    )

    AlarmSoundBottomSheet(
        vibrationEnabled = state.soundState.isVibrationEnabled,
        soundEnabled = state.soundState.isSoundEnabled,
        soundVolume = state.soundState.soundVolume,
        soundIndex = state.soundState.soundIndex,
        sounds = state.soundState.sounds,
        onVibrationToggle = { eventDispatcher(AlarmAddEditContract.Action.ToggleVibrationOption) },
        onSoundToggle = { eventDispatcher(AlarmAddEditContract.Action.ToggleSoundOption) },
        onVolumeChanged = { eventDispatcher(AlarmAddEditContract.Action.AdjustSoundVolume(it)) },
        onSoundSelected = { eventDispatcher(AlarmAddEditContract.Action.SelectAlarmSound(it)) },
        onComplete = {
            scope.launch {
                soundBottomSheetState.hide()
            }.invokeOnCompletion {
                eventDispatcher(AlarmAddEditContract.Action.ToggleBottomSheet(AlarmAddEditContract.BottomSheetType.SoundSetting))
            }
        },
        isSheetOpen = state.bottomSheetState == AlarmAddEditContract.BottomSheetType.SoundSetting,
        onDismiss = {
            scope.launch {
                soundBottomSheetState.hide()
            }.invokeOnCompletion {
                eventDispatcher(AlarmAddEditContract.Action.ToggleBottomSheet(AlarmAddEditContract.BottomSheetType.SoundSetting))
            }
        },
    )

    if (state.isDeleteDialogVisible) {
        OrbitDialog(
            title = stringResource(id = R.string.alarm_delete_dialog_title),
            message = stringResource(id = R.string.alarm_delete_dialog_message),
            confirmText = stringResource(id = R.string.alarm_delete_dialog_btn_delete),
            cancelText = stringResource(id = R.string.alarm_delete_dialog_btn_cancel),
            onConfirm = {
                eventDispatcher(AlarmAddEditContract.Action.DeleteAlarm)
            },
            onCancel = {
                eventDispatcher(AlarmAddEditContract.Action.HideDeleteDialog)
            },
        )
    }

    if (state.isUnsavedChangesDialogVisible) {
        OrbitDialog(
            title = stringResource(id = R.string.alarm_unsaved_changes_dialog_title),
            message = stringResource(id = R.string.alarm_unsaved_changes_dialog_message),
            confirmText = stringResource(id = R.string.alarm_unsaved_changes_dialog_btn_discard),
            cancelText = stringResource(id = R.string.alarm_unsaved_changes_dialog_btn_cancel),
            onConfirm = {
                eventDispatcher(AlarmAddEditContract.Action.NavigateBack)
            },
            onCancel = {
                eventDispatcher(AlarmAddEditContract.Action.HideUnsavedChangesDialog)
            },
        )
    }
}

@Composable
private fun AlarmAddEditLoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        LottieAnimation(
            modifier = Modifier
                .size(70.dp)
                .align(Alignment.Center),
            resId = core.designsystem.R.raw.star_loading,
        )
    }
}

@Composable
private fun AlarmAddEditTopBar(
    mode: AlarmAddEditContract.EditMode = AlarmAddEditContract.EditMode.ADD,
    title: String,
    onBack: () -> Unit,
    onDelete: () -> Unit,
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

        if (mode == AlarmAddEditContract.EditMode.EDIT) {
            DeleteAlarmButton(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 20.dp),
            ) {
                onDelete()
            }
        }
    }
}

@Composable
private fun DeleteAlarmButton(
    modifier: Modifier = Modifier,
    onDelete: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState().value

    Surface(
        onClick = onDelete,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        interactionSource = interactionSource,
        color = if (isPressed) OrbitTheme.colors.gray_800 else Color.Transparent,
    ) {
        Text(
            text = stringResource(id = R.string.alarm_add_edit_delete),
            style = OrbitTheme.typography.body1Medium,
            color = OrbitTheme.colors.alert,
            modifier = Modifier
                .padding(
                    horizontal = 8.dp,
                    vertical = 4.dp,
                ),
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
            modifier = Modifier
                .fillMaxWidth()
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
            onClick = {
                processAction(
                    AlarmAddEditContract.Action.ToggleBottomSheet(
                        AlarmAddEditContract.BottomSheetType.SnoozeSetting,
                    ),
                )
            },
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
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
            onClick = {
                processAction(
                    AlarmAddEditContract.Action.ToggleBottomSheet(
                        AlarmAddEditContract.BottomSheetType.SoundSetting,
                    ),
                )
            },
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
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp

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
                    processAction(AlarmAddEditContract.Action.ToggleWeekdaysSelection)
                },
            )
            Spacer(modifier = Modifier.width(2.dp))
            AlarmCheckItem(
                label = stringResource(id = R.string.alarm_add_edit_weekends),
                isPressed = state.isWeekendsChecked,
                onClick = {
                    processAction(AlarmAddEditContract.Action.ToggleWeekendsSelection)
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
                    modifier = Modifier.size(
                        if (screenWidthDp > 360.dp) 36.dp else 34.dp,
                    ),
                    label = stringResource(id = day.getLabelStringRes()),
                    isPressed = state.selectedDays.contains(day),
                    onClick = {
                        processAction(AlarmAddEditContract.Action.ToggleSpecificDaySelection(day))
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
                processAction(AlarmAddEditContract.Action.ToggleHolidaySkipOption)
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
