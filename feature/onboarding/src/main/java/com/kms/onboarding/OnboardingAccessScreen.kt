package com.kms.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yapp.common.navigation.OrbitNavigator
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.ui.utils.heightForScreenPercentage
import feature.onboarding.R

@Composable
fun OnboardingAccessRoute(
    navigator: OrbitNavigator,
    viewModel: OnboardingViewModel,
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    OnboardingAccessScreen(
        state = state,
        currentStep = 6,
        totalSteps = 6,
        onNextClick = { viewModel.processAction(OnboardingContract.Action.NextStep) },
        onBackClick = { viewModel.processAction(OnboardingContract.Action.PreviousStep) },
    )
}

@Composable
fun OnboardingAccessScreen(
    state: OnboardingContract.State,
    currentStep: Int,
    totalSteps: Int,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    // TODO: 추후 실제 시스템 알림 Alert 에서 선택한 값에 따른 변경
    val isToggled = remember { mutableStateOf(false) }
    OnboardingScreen(
        currentStep = currentStep,
        totalSteps = totalSteps,
        isButtonEnabled = true,
        onNextClick = onNextClick,
        onBackClick = onBackClick,
        buttonLabel = "네, 알겠어요",
    ) {
        val (text, imageRes) = when (isToggled.value) {
            true -> Pair(
                stringResource(id = R.string.onboarding_step7_text_refuse_title),
                core.designsystem.R.drawable.ic_onboarding_authorization_refusal,
            )

            false -> Pair(
                stringResource(id = R.string.onboarding_step7_text_default_title),
                core.designsystem.R.drawable.ic_onboarding_authorization_guide,
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Spacer(modifier = Modifier.heightForScreenPercentage(0.05f))

            Text(
                text = text,
                style = OrbitTheme.typography.heading1SemiBold,
                color = OrbitTheme.colors.white,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.heightForScreenPercentage(0.123f))

            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.CenterHorizontally),
            )
        }
    }
}

@Composable
@Preview
fun OnboardingAccessScreenPreview() {
    OnboardingAccessScreen(
        state = OnboardingContract.State(),
        currentStep = 1,
        totalSteps = 5,
        onNextClick = {},
        onBackClick = {},
    )
}
