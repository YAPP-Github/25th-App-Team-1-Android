package com.kms.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.ui.component.textfield.OrbitTextField
import com.yapp.ui.extensions.customClickable
import com.yapp.ui.utils.heightForScreenPercentage
import com.yapp.ui.utils.paddingForScreenPercentage
import feature.onboarding.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OnboardingTimeOfBirthScreen(
    state: OnboardingContract.State,
    currentStep: Int,
    totalSteps: Int,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    onTextChange: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    var isTextPressed by remember { mutableStateOf(false) }

    OnboardingScreen(
        currentStep = currentStep,
        totalSteps = totalSteps,
        isButtonEnabled = state.isButtonEnabled,
        onNextClick = onNextClick,
        onBackClick = onBackClick,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .customClickable(
                    rippleEnabled = false,
                    onClick = { focusManager.clearFocus() },
                ),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Spacer(modifier = Modifier.heightForScreenPercentage(0.05f))
                Text(
                    text = stringResource(id = R.string.onboarding_step4_text_title),
                    style = OrbitTheme.typography.heading1SemiBold,
                    color = OrbitTheme.colors.white,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
                OrbitTextField(
                    text = state.textFieldValue,
                    onTextChange = { value ->
                        onTextChange(value)
                    },
                    hint = "23:59",
                    showWarning = state.showWarning,
                    warningMessage = stringResource(id = R.string.onboarding_step4_textfield_warning),
                    modifier = Modifier
                        .fillMaxWidth()
                        .paddingForScreenPercentage(horizontalPercentage = 0.192f, topPercentage = 0.086f),
                )
            }

            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .paddingForScreenPercentage(bottomPercentage = 0.017f)
                    .align(Alignment.CenterHorizontally)
                    .pointerInteropFilter { event ->
                        when (event.action) {
                            android.view.MotionEvent.ACTION_DOWN -> {
                                isTextPressed = true
                                true
                            }

                            android.view.MotionEvent.ACTION_UP,
                            android.view.MotionEvent.ACTION_CANCEL,
                            -> {
                                isTextPressed = false
                                true
                            }

                            else -> false
                        }
                    },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = core.designsystem.R.drawable.ic_check),
                    contentDescription = "Check",
                    tint = if (isTextPressed) OrbitTheme.colors.main else OrbitTheme.colors.white,
                )
                Text(
                    text = stringResource(id = R.string.onboarding_step4_text_check),
                    style = OrbitTheme.typography.body1Medium,
                    color = if (isTextPressed) OrbitTheme.colors.main else OrbitTheme.colors.white,
                    modifier = Modifier.padding(start = 4.dp),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
@Preview
fun OnboardingTimeOfBirthScreenPreview() {
    OnboardingTimeOfBirthScreen(
        state = OnboardingContract.State(
            textFieldValue = "23:59",
            showWarning = false,
            isButtonEnabled = true,
        ),
        currentStep = 4,
        totalSteps = 4,
        onNextClick = {},
        onBackClick = {},
        onTextChange = {},
    )
}
