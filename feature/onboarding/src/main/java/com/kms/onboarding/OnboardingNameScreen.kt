package com.kms.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.ui.component.textfield.OrbitTextField
import com.yapp.ui.extensions.customClickable
import com.yapp.ui.utils.heightForScreenPercentage
import com.yapp.ui.utils.paddingForScreenPercentage
import feature.onboarding.R

@Composable
fun OnboardingNameRoute(
    viewModel: OnboardingViewModel,
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    OnboardingNameScreen(
        state = state,
        currentStep = 4,
        totalSteps = 6,
        onNextClick = { viewModel.processAction(OnboardingContract.Action.NextStep) },
        onBackClick = { viewModel.processAction(OnboardingContract.Action.PreviousStep) },
        onTextChange = { value ->
            viewModel.processAction(OnboardingContract.Action.UpdateField(value, OnboardingContract.FieldType.NAME))
        },
    )
}

@Composable
fun OnboardingNameScreen(
    state: OnboardingContract.State,
    currentStep: Int,
    totalSteps: Int,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    onTextChange: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    OnboardingScreen(
        currentStep = currentStep,
        totalSteps = totalSteps,
        isButtonEnabled = state.isButtonEnabled,
        onNextClick = onNextClick,
        onBackClick = onBackClick,
        buttonLabel = "다음",
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .customClickable(
                    rippleEnabled = false,
                    onClick = { focusManager.clearFocus() },
                ),
        ) {
            Spacer(modifier = Modifier.heightForScreenPercentage(0.05f))
            Text(
                text = stringResource(id = R.string.onboarding_step5_text_title),
                style = OrbitTheme.typography.heading1SemiBold,
                color = OrbitTheme.colors.white,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            OrbitTextField(
                text = state.textFieldValue,
                onTextChange = { value -> onTextChange(value) },
                hint = "이름 입력",
                showWarning = state.showWarning,
                warningMessage = stringResource(id = R.string.onboarding_step5_textfield_warning),
                modifier = Modifier
                    .fillMaxWidth()
                    .paddingForScreenPercentage(horizontalPercentage = 0.192f, topPercentage = 0.086f),
            )
        }
    }
}

@Composable
@Preview
fun PreviewOnboardingNameScreen() {
    OnboardingNameScreen(
        state = OnboardingContract.State(
            textFieldValue = "김민수",
            isButtonEnabled = true,
            showWarning = false,
        ),
        currentStep = 1,
        totalSteps = 5,
        onNextClick = {},
        onBackClick = {},
        onTextChange = {},
    )
}
