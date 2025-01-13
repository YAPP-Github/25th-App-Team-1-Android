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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    viewModel: AlarmAddEditViewModel = hiltViewModel()
) {
    val uiState by viewModel.container.stateFlow.collectAsStateWithLifecycle()
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
        uiState = uiState,
        processAction = viewModel::processAction
    )
}

@Composable
fun AlarmAddEditScreen(
    uiState: AlarmAddEditContract.State,
    processAction: (AlarmAddEditContract.Action) -> Unit
) {
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
            contentAlignment = Alignment.Center
        ) {
            OrbitPicker(
                selectedAmPm = uiState.currentAmPm,
                selectedHour = uiState.currentHour,
                selectedMinute = uiState.currentMinute,
            ) { amPm, hour, minute ->
                processAction(AlarmAddEditContract.Action.UpdateAlarmTime(amPm, hour, minute))
            }
        }
        AlarmAddEditSettingsSection(
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        OrbitButton(
            label = stringResource(R.string.alarm_add_edit_save),
            onClick = { },
            enabled = true,
            modifier = Modifier
                .padding(
                    horizontal = 20.dp,
                    vertical = 12.dp
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
    modifier: Modifier = Modifier
) {
    var isWeekdaysPressed by remember { mutableStateOf(false) }
    var isWeekendsPressed by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = OrbitTheme.colors.gray_800,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        AlarmAddEditSelectDaysSection(
            isWeekdaysPressed = isWeekdaysPressed,
            isWeekendsPressed = isWeekendsPressed,
            onWeekdaysClick = { isWeekdaysPressed = !isWeekdaysPressed },
            onWeekendsClick = { isWeekendsPressed = !isWeekendsPressed },
        )
    }
}

@Composable
private fun AlarmAddEditSelectDaysSection(
    isWeekdaysPressed: Boolean,
    isWeekendsPressed: Boolean,
    onWeekdaysClick: () -> Unit,
    onWeekendsClick: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(
            horizontal = 20.dp,
            vertical = 16.dp
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                stringResource(id = R.string.alarm_add_edit_repeat),
                style = OrbitTheme.typography.body1SemiBold,
                color = OrbitTheme.colors.white
            )

            Spacer(modifier = Modifier.weight(1f))

            AlarmCheckItem(
                label = stringResource(id = R.string.alarm_add_edit_weekdays),
                isPressed = isWeekdaysPressed,
                onClick = onWeekdaysClick
            )
            Spacer(modifier = Modifier.width(2.dp))
            AlarmCheckItem(
                label = stringResource(id = R.string.alarm_add_edit_weekends),
                isPressed = isWeekendsPressed,
                onClick = onWeekendsClick
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val weeks = listOf(
                stringResource(id = R.string.alarm_add_edit_sunday),
                stringResource(id = R.string.alarm_add_edit_monday),
                stringResource(id = R.string.alarm_add_edit_tuesday),
                stringResource(id = R.string.alarm_add_edit_wednesday),
                stringResource(id = R.string.alarm_add_edit_thursday),
                stringResource(id = R.string.alarm_add_edit_friday),
                stringResource(id = R.string.alarm_add_edit_saturday),
            )
            weeks.forEachIndexed { index, week ->
                AlarmDayButton(
                    label = week,
                    isPressed = false,
                    onClick = { }
                )
            }
        }
        Spacer(modifier = Modifier.height(18.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = core.designsystem.R.drawable.ic_holiday),
                contentDescription = "Holiday",
                tint = OrbitTheme.colors.gray_400,
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(
                stringResource(id = R.string.alarm_add_edit_disable_holiday),
                style = OrbitTheme.typography.label1Medium,
                color = OrbitTheme.colors.gray_400
            )

            Spacer(modifier = Modifier.weight(1f))

            OrbitSwitch(
                isSelected = false,
                onClick = { }
            )
        }
    }
}

@Preview
@Composable
fun AlarmAddEditSettingsSectionPreview() {
    AlarmAddEditSettingsSection()
}

@Preview
@Composable
fun AlarmAddEditScreenPreview() {
    AlarmAddEditScreen(
        uiState = AlarmAddEditContract.State(),
        processAction = { }
    )
}
