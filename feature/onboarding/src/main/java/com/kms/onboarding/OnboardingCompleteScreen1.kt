package com.kms.onboarding

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.ui.component.lottie.LottieAnimation
import com.yapp.ui.utils.heightForScreenPercentage
import feature.onboarding.R

@Composable
fun OnboardingCompleteRoute(
    viewModel: OnboardingViewModel,
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    BackHandler {
        viewModel.processAction(OnboardingContract.Action.PreviousStep) // ✅ ViewModel에서 처리
    }
    OnboardingCompleteScreen1(
        state = state,
        onNextClick = { viewModel.processAction(OnboardingContract.Action.NextStep) },
        onBackClick = { viewModel.processAction(OnboardingContract.Action.PreviousStep) },
    )
}

@Composable
fun OnboardingCompleteScreen1(
    state: OnboardingContract.State,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    OnboardingScreen(
        currentStep = 0,
        totalSteps = 0,
        isButtonEnabled = true,
        onNextClick = onNextClick,
        onBackClick = onBackClick,
        showTopAppBar = true,
        showTopAppBarActions = false,
        buttonLabel = "다음",
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.heightForScreenPercentage(0.05f))
            Text(
                text = stringResource(id = R.string.onboarding_completed_step1_subtitle),
                style = OrbitTheme.typography.body2Regular,
                color = OrbitTheme.colors.main,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(id = R.string.onboarding_completed_step1_title),
                style = OrbitTheme.typography.heading1SemiBold,
                color = OrbitTheme.colors.white,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                LottieAnimation(
                    modifier = Modifier
                        .scale(1.4f)
                        .offset(y = (-50).dp),
                    resId = core.designsystem.R.raw.step2,
                )
            }
        }
    }
}

@Composable
@Preview
fun OnboardingCompleteScreen1Preview() {
    OrbitTheme {
        OnboardingCompleteScreen1(
            state = OnboardingContract.State(),
            onNextClick = {},
            onBackClick = {},
        )
    }
}
