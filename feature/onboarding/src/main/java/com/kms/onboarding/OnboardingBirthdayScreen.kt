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
import com.yapp.ui.component.timepicker.OrbitYearMonthPicker
import com.yapp.ui.utils.heightForScreenPercentage
import feature.onboarding.R

@Composable
fun OnboardingBirthdayRoute(
    viewModel: OnboardingViewModel,
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    OnboardingBirthdayScreen(
        state = state,
        currentStep = 2,
        totalSteps = 6,
        onNextClick = { viewModel.processAction(OnboardingContract.Action.NextStep) },
        onBackClick = { viewModel.processAction(OnboardingContract.Action.PreviousStep) },
        onBirthDateChange = { lunar, year, month, day ->
            viewModel.processAction(OnboardingContract.Action.UpdateBirthDate(lunar, year, month, day)) // ✅ 생년월일 저장
        },
    )
}

@Composable
fun OnboardingBirthdayScreen(
    state: OnboardingContract.State,
    currentStep: Int,
    totalSteps: Int,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    onBirthDateChange: (String, Int, Int, Int) -> Unit,
) {
    OnboardingScreen(
        currentStep = currentStep,
        totalSteps = totalSteps,
        isButtonEnabled = true,
        onNextClick = onNextClick,
        onBackClick = onBackClick,
        buttonLabel = "다음",
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.heightForScreenPercentage(0.05f))
            Text(
                text = stringResource(id = R.string.onboarding_step3_text_title),
                style = OrbitTheme.typography.heading1SemiBold,
                color = OrbitTheme.colors.white,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )

            OrbitYearMonthPicker(
                modifier = Modifier.padding(top = 60.dp),
                onValueChange = onBirthDateChange,
            )
        }
    }
}

@Composable
@Preview
fun OnboardingBirthdayScreenPreview() {
    OrbitTheme {
        OnboardingBirthdayScreen(
            state = OnboardingContract.State(),
            currentStep = 3,
            totalSteps = 3,
            onNextClick = {},
            onBackClick = {},
            onBirthDateChange = { _, _, _, _ -> },
        )
    }
}
