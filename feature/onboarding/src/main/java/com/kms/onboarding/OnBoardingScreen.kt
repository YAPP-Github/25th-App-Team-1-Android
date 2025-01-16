package com.kms.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kms.onboarding.component.OnBoardingTopAppBar
import com.kms.onboarding.component.OnboardingBottomBar
import com.yapp.designsystem.theme.OrbitTheme

@Composable
fun OnboardingScreen(
    currentStep: Int,
    totalSteps: Int,
    isButtonEnabled: Boolean,
    onNextClick: () -> Unit,
    onBackClick: (() -> Unit)?,
    showTopAppBar: Boolean = true,
    showTopAppBarActions: Boolean = true,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OrbitTheme.colors.gray_900)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding(),
    ) {
        if (showTopAppBar) {
            OnBoardingTopAppBar(
                currentStep = currentStep,
                totalSteps = totalSteps,
                onBackClick = onBackClick,
                showTopAppBarActions = showTopAppBarActions,
            )
        }

        Box(
            modifier = Modifier.weight(1f),
        ) {
            content()
        }

        OnboardingBottomBar(
            currentStep = currentStep,
            isButtonEnabled = isButtonEnabled,
            onNextClick = onNextClick,
        )
    }
}
