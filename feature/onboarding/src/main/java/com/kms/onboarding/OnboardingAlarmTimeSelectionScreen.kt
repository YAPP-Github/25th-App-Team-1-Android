package com.kms.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.ui.component.timepicker.OrbitPicker
import com.yapp.ui.utils.heightForScreenPercentage
import feature.onboarding.R

@Composable
fun OnboardingAlarmTimeSelectionRoute(
    viewModel: OnboardingViewModel,
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    OnboardingAlarmTimeSelectionScreen(
        state = state,
        currentStep = 1,
        totalSteps = 6,
        onNextClick = { viewModel.processAction(OnboardingContract.Action.NextStep) },
        onBackClick = { viewModel.processAction(OnboardingContract.Action.PreviousStep) },
        setAlarmTime = { isAm, hour, minute ->
            viewModel.processAction(OnboardingContract.Action.SetAlarmTime(isAm, hour, minute))
        },
    )
}

@Composable
fun OnboardingAlarmTimeSelectionScreen(
    state: OnboardingContract.State,
    currentStep: Int,
    totalSteps: Int,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    setAlarmTime: (String, Int, Int) -> Unit,
) {
    OnboardingScreen(
        currentStep = currentStep,
        totalSteps = totalSteps,
        isButtonEnabled = true,
        onNextClick = onNextClick,
        onBackClick = onBackClick,
        buttonLabel = "만들기",
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.heightForScreenPercentage(0.05f))
            Text(
                text = stringResource(id = R.string.onboarding_step2_text_title),
                style = OrbitTheme.typography.heading1SemiBold,
                color = OrbitTheme.colors.white,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(id = R.string.onboarding_step2_text_subtitle),
                style = OrbitTheme.typography.body1Regular,
                color = OrbitTheme.colors.gray_100,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                textAlign = TextAlign.Center,
            )

            OrbitPicker(
                modifier = Modifier.padding(top = 90.dp),
            ) { amPm, hour, minute ->
                setAlarmTime(amPm, hour, minute)
            }
        }
    }
}

@Composable
@Preview
fun OnboardingAlarmTimeSelectionScreenPreview() {
    OrbitTheme {
        OnboardingAlarmTimeSelectionScreen(
            state = OnboardingContract.State(),
            currentStep = 0,
            totalSteps = 0,
            onNextClick = {},
            onBackClick = {},
            setAlarmTime = { _, _, _ -> },
        )
    }
}
