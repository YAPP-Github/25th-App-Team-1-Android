package com.kms.onboarding.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kms.onboarding.OnboardingAlarmTimeSelectionScreen
import com.kms.onboarding.OnboardingBirthdayScreen
import com.kms.onboarding.OnboardingContract
import com.kms.onboarding.OnboardingExplainScreen
import com.kms.onboarding.OnboardingNameScreen
import com.kms.onboarding.OnboardingTimeOfBirthScreen

fun NavGraphBuilder.onboardingNavGraph(
    stateProvider: () -> OnboardingContract.State,
    eventDispatcher: (OnboardingContract.Action) -> Unit,
    onFinishOnboarding: () -> Unit,
) {
    composable(OnboardingDestination.Explain.route) {
        OnboardingExplainScreen(
            state = stateProvider(),
            onNextClick = {
                eventDispatcher(OnboardingContract.Action.NextStep)
            },
        )
    }

    composable(OnboardingDestination.AlarmTimeSelection.route) {
        OnboardingAlarmTimeSelectionScreen(
            state = stateProvider(),
            currentStep = 1,
            totalSteps = 4,
            onNextClick = {
                eventDispatcher(OnboardingContract.Action.NextStep)
            },
            onBackClick = {
                eventDispatcher(OnboardingContract.Action.PreviousStep)
            },
        )
    }

    composable(OnboardingDestination.Birthday.route) {
        OnboardingBirthdayScreen(
            state = stateProvider(),
            currentStep = 2,
            totalSteps = 4,
            onNextClick = {
                eventDispatcher(OnboardingContract.Action.NextStep)
            },
            onBackClick = {
                eventDispatcher(OnboardingContract.Action.PreviousStep)
            },
        )
    }

    composable(OnboardingDestination.TimeOfBirth.route) {
        OnboardingTimeOfBirthScreen(
            state = stateProvider(),
            currentStep = 3,
            totalSteps = 4,
            onNextClick = {
                eventDispatcher(OnboardingContract.Action.NextStep)
                eventDispatcher(OnboardingContract.Action.Reset)
            },
            onBackClick = {
                eventDispatcher(OnboardingContract.Action.PreviousStep)
            },
            onTextChange = { value ->
                eventDispatcher(OnboardingContract.Action.UpdateField(value, OnboardingContract.FieldType.TIME))
            },
        )
    }
    composable(OnboardingDestination.Name.route) {
        OnboardingNameScreen(
            state = stateProvider(),
            currentStep = 4,
            totalSteps = 4,
            onNextClick = {
                eventDispatcher(OnboardingContract.Action.NextStep)
            },
            onBackClick = {
                eventDispatcher(OnboardingContract.Action.PreviousStep)
            },
            onTextChange = { value ->
                eventDispatcher(OnboardingContract.Action.UpdateField(value, OnboardingContract.FieldType.NAME))
            },
        )
    }
}
