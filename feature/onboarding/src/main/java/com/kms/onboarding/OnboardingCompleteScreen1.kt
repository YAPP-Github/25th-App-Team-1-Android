package com.kms.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.kms.onboarding.component.LottieAnimation
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.ui.utils.heightForScreenPercentage
import com.yapp.ui.utils.paddingForScreenPercentage
import feature.onboarding.R

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
            Text(
                text = stringResource(id = R.string.onboarding_completed_step1_title),
                style = OrbitTheme.typography.heading1SemiBold,
                color = OrbitTheme.colors.white,
                modifier = Modifier
                    .fillMaxWidth()
                    .paddingForScreenPercentage(topPercentage = 0.0147f, bottomPercentage = 0.1f),
                textAlign = TextAlign.Center,
            )
            LottieAnimation(
                modifier = Modifier.wrapContentSize(),
                resId = core.designsystem.R.raw.step2,
                contentScale = ContentScale.FillWidth,
            )
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
