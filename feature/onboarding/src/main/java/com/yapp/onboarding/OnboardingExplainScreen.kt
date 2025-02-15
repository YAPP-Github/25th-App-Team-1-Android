package com.yapp.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.yapp.onboarding.component.GifImage
import com.yapp.ui.utils.heightForScreenPercentage
import com.yapp.ui.utils.paddingForScreenPercentage
import feature.onboarding.R

@Composable
fun OnboardingExplainRoute(
    viewModel: OnboardingViewModel,
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    OnboardingExplainScreen(
        state = state,
        onNextClick = { viewModel.processAction(OnboardingContract.Action.NextStep) },
    )
}

@Composable
fun OnboardingExplainScreen(
    state: OnboardingContract.State,
    onNextClick: () -> Unit,
) {
    OnboardingScreen(
        currentStep = 0,
        totalSteps = 0,
        isButtonEnabled = true,
        onNextClick = onNextClick,
        onBackClick = null,
        showTopAppBar = false,
        buttonLabel = "다음",
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.heightForScreenPercentage(0.105f))
            Text(
                text = stringResource(id = R.string.onboarding_step1_text_title),
                style = OrbitTheme.typography.body1Regular,
                color = OrbitTheme.colors.gray_100,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(id = R.string.onboarding_step1_text_subtitle),
                style = OrbitTheme.typography.heading1SemiBold,
                color = OrbitTheme.colors.white,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .paddingForScreenPercentage(bottomPercentage = 0.037f),
                textAlign = TextAlign.Center,
            )
            GifImage(
                modifier = Modifier.fillMaxSize(),
                gifResId = core.designsystem.R.raw.step1,
            )
        }
    }
}

@Composable
@Preview
fun OnboardingExplainScreenPreview() {
    OrbitTheme {
        OnboardingExplainScreen(
            state = OnboardingContract.State(),
            onNextClick = {},
        )
    }
}
