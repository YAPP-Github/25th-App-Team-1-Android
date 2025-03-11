package com.yapp.onboarding

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.onboarding.component.UnknownBirthTimeButton
import com.yapp.ui.component.textfield.OrbitTextField
import com.yapp.ui.extensions.customClickable
import com.yapp.ui.utils.heightForScreenPercentage
import com.yapp.ui.utils.paddingForScreenPercentage
import feature.onboarding.R

@Composable
fun OnboardingTimeOfBirthRoute(
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    val keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }
    BackHandler {
        viewModel.processAction(OnboardingContract.Action.PreviousStep)
    }

    OnboardingTimeOfBirthScreen(
        state = state,
        currentStep = 3,
        totalSteps = 6,
        focusRequester = focusRequester,
        onNextClick = {
            viewModel.processAction(OnboardingContract.Action.Reset)
            viewModel.processAction(OnboardingContract.Action.NextStep)
            keyboardController?.hide()
        },
        onBackClick = {
            viewModel.processAction(OnboardingContract.Action.PreviousStep)
        },
        onTextChange = { value ->
            viewModel.processAction(OnboardingContract.Action.UpdateField(value, OnboardingContract.FieldType.TIME))
        },
    )
}

@Composable
fun OnboardingTimeOfBirthScreen(
    state: OnboardingContract.State,
    currentStep: Int,
    totalSteps: Int,
    focusRequester: FocusRequester,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    onTextChange: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = state.textFieldValue,
                selection = TextRange(state.textFieldValue.length),
            ),
        )
    }

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
                    text = textFieldValue,
                    onTextChange = { value ->
                        val formattedValue = formatTimeInput(value.text, textFieldValue.text)
                        textFieldValue = TextFieldValue(
                            text = formattedValue,
                            selection = TextRange(formattedValue.length),
                        )
                        onTextChange(formattedValue)
                    },
                    hint = "23:59",
                    isValid = state.isValid,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                    ),
                    showWarning = state.showWarning,
                    warningMessage = stringResource(id = R.string.onboarding_step4_textfield_warning),
                    focusRequester = focusRequester,
                    modifier = Modifier
                        .fillMaxWidth()
                        .paddingForScreenPercentage(horizontalPercentage = 0.192f, topPercentage = 0.086f),
                )
            }

            UnknownBirthTimeButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 20.dp),
                onClick = {
                    textFieldValue = TextFieldValue("시간모름")
                    onTextChange("시간모름")
                    onNextClick()
                },
            )
        }
    }
}

fun formatTimeInput(input: String, previousText: String): String {
    val sanitizedValue = input.filter { it.isDigit() }
    val isDeleting = sanitizedValue.length < previousText.filter { it.isDigit() }.length

    return when {
        isDeleting && previousText.endsWith(":") -> sanitizedValue
        sanitizedValue.length > 2 -> {
            val hours = sanitizedValue.take(2)
            val minutes = sanitizedValue.drop(2).take(2)
            "$hours:$minutes"
        }
        sanitizedValue.length == 2 -> {
            if (previousText.length == 3 && previousText.endsWith(":")) {
                sanitizedValue
            } else {
                "$sanitizedValue:"
            }
        }
        else -> sanitizedValue
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
        focusRequester = remember { FocusRequester() },
        onTextChange = {},
    )
}
