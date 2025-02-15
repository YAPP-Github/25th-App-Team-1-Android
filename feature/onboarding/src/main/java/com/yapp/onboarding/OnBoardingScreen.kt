package com.yapp.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.onboarding.component.OnBoardingTopAppBar
import com.yapp.onboarding.component.OnboardingBottomBar

@Composable
fun OnboardingScreen(
    currentStep: Int,
    totalSteps: Int,
    isButtonEnabled: Boolean,
    onNextClick: () -> Unit,
    onBackClick: (() -> Unit)?,
    buttonLabel: String,
    showTopAppBar: Boolean = true,
    showTopAppBarActions: Boolean = true,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OrbitTheme.colors.gray_900)
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
            buttonLabel = buttonLabel,
        )
    }
}
