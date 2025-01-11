package com.kms.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import com.kms.onboarding.component.OnBoardingTopAppBar
import com.kms.onboarding.component.OnboardingBottomBar
import com.kms.onboarding.navigation.onboardingNavGraph
import com.kms.onboarding.navigation.rememberOnboardingAppState
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.ui.component.timepicker.OrbitPicker
import com.yapp.ui.utils.heightForScreenPercentage
import feature.onboarding.R

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
    Scaffold(
        topBar = {
            if (showTopAppBar) {
                OnBoardingTopAppBar(
                    currentStep = currentStep,
                    totalSteps = totalSteps,
                    onBackClick = onBackClick,
                )
            }
        },
        bottomBar = {
            OnboardingBottomBar(
                isButtonEnabled = isButtonEnabled,
                onNextClick = onNextClick,
            )
        },
        containerColor = OrbitTheme.colors.gray_900,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter,
        ) {
            content()
        }
    }
}

@Composable
fun ExplainScreen(
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
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.heightForScreenPercentage(0.105f))
            Text(
                text = stringResource(id = R.string.onboarding_step1_text_title),
                style = OrbitTheme.typography.body1Regular,
                color = OrbitTheme.colors.gray_100,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(id = R.string.onboarding_step1_text_subtitle),
                style = OrbitTheme.typography.heading1SemiBold,
                color = OrbitTheme.colors.white,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 12.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun AlarmTimeSelectionScreen(
    state: OnboardingContract.State,
    currentStep: Int,
    totalSteps: Int,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    OnboardingScreen(
        currentStep = currentStep,
        totalSteps = totalSteps,
        isButtonEnabled = true,
        onNextClick = onNextClick,
        onBackClick = onBackClick,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.heightForScreenPercentage(0.05f))
            Text(
                text = stringResource(id = R.string.onboarding_step2_text_title),
                style = OrbitTheme.typography.heading1SemiBold,
                color = OrbitTheme.colors.white,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(id = R.string.onboarding_step2_text_subtitle),
                style = OrbitTheme.typography.body1Regular,
                color = OrbitTheme.colors.gray_100,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            OrbitPicker(
                modifier = Modifier.padding(top = 90.dp),
            )
        }
    }
}

@Composable
fun BirthdayScreen(
    state: OnboardingContract.State,
    currentStep: Int,
    totalSteps: Int,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    OnboardingScreen(
        currentStep = currentStep,
        totalSteps = totalSteps,
        isButtonEnabled = true,
        onNextClick = onNextClick,
        onBackClick = onBackClick,
    ) {
        Column { }
    }
}

@Composable
@Preview
fun OnboardingPreview() {
    OrbitTheme {
        AlarmTimeSelectionScreen(
            state = OnboardingContract.State(),
            currentStep = 1,
            totalSteps = 2,
            onNextClick = {},
            onBackClick = {},
        )
    }
}
