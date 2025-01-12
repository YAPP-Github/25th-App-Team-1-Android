package com.kms.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import com.kms.onboarding.component.OnBoardingTopAppBar
import com.kms.onboarding.component.OnboardingBottomBar
import com.kms.onboarding.navigation.onboardingNavGraph
import com.kms.onboarding.navigation.rememberOnboardingAppState
import com.yapp.designsystem.theme.OrbitTheme

@Composable
fun OnboardingRoute(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onFinishOnboarding: () -> Unit,
) {
    val appState = rememberOnboardingAppState()
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            when (sideEffect) {
                is OnboardingContract.SideEffect.Navigate -> appState.navigateTo(
                    route = sideEffect.route,
                    popUpTo = sideEffect.popUpTo,
                    inclusive = sideEffect.inclusive,
                )

                OnboardingContract.SideEffect.NavigateBack -> appState.navigateBack()
                OnboardingContract.SideEffect.OnboardingCompleted -> onFinishOnboarding()
                OnboardingContract.SideEffect.ResetField -> viewModel.processAction(
                    OnboardingContract.Action.Reset,
                )
            }
        }
    }

    NavHost(
        navController = appState.navController,
        startDestination = appState.startDestination,
    ) {
        onboardingNavGraph(
            stateProvider = { state },
            eventDispatcher = { viewModel.processAction(it) },
            onFinishOnboarding = onFinishOnboarding,
        )
    }
}

@Composable
fun OnboardingScreen(
    currentStep: Int,
    totalSteps: Int,
    isButtonEnabled: Boolean,
    onNextClick: () -> Unit,
    onBackClick: (() -> Unit)?,
    showTopAppBar: Boolean = true,
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
            )
        }

        Box(
            modifier = Modifier.weight(1f),
        ) {
            content()
        }

        OnboardingBottomBar(
            isButtonEnabled = isButtonEnabled,
            onNextClick = onNextClick,
        )
    }
}
