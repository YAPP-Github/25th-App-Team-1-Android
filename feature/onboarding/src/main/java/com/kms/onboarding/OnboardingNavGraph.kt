package com.kms.onboarding

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.yapp.common.navigation.OrbitNavigator
import com.yapp.common.navigation.destination.OnboardingDestination
import com.yapp.common.navigation.extensions.sharedViewModel

fun NavGraphBuilder.onboardingNavGraph(
    navigator: OrbitNavigator,
    onFinishOnboarding: () -> Unit,
) {
    navigation(
        route = OnboardingDestination.Route.route,
        startDestination = OnboardingDestination.Explain.route,
    ) {
        composable(OnboardingDestination.Explain.route) {
            val viewModel = it.sharedViewModel<OnboardingViewModel>(navigator.navController)

            LaunchedEffect(viewModel) {
                viewModel.container.sideEffectFlow.collect { sideEffect ->
                    handleSideEffect(
                        sideEffect = sideEffect,
                        navigator = navigator,
                        viewModel = viewModel,
                        onFinishOnboarding = onFinishOnboarding,
                    )
                }
            }

            OnboardingExplainRoute(
                viewModel = viewModel,
            )
        }

        composable(OnboardingDestination.AlarmTimeSelection.route) {
            val viewModel = it.sharedViewModel<OnboardingViewModel>(navigator.navController)

            LaunchedEffect(viewModel) {
                viewModel.container.sideEffectFlow.collect { sideEffect ->
                    handleSideEffect(
                        sideEffect = sideEffect,
                        navigator = navigator,
                        viewModel = viewModel,
                        onFinishOnboarding = onFinishOnboarding,
                    )
                }
            }

            OnboardingAlarmTimeSelectionRoute(
                viewModel = viewModel,
            )
        }

        composable(OnboardingDestination.Birthday.route) {
            val viewModel = it.sharedViewModel<OnboardingViewModel>(navigator.navController)

            LaunchedEffect(viewModel) {
                viewModel.container.sideEffectFlow.collect { sideEffect ->
                    handleSideEffect(
                        sideEffect = sideEffect,
                        navigator = navigator,
                        viewModel = viewModel,
                        onFinishOnboarding = onFinishOnboarding,
                    )
                }
            }

            OnboardingBirthdayRoute(
                viewModel = viewModel,
            )
        }

        composable(OnboardingDestination.TimeOfBirth.route) {
            val viewModel = it.sharedViewModel<OnboardingViewModel>(navigator.navController)

            LaunchedEffect(viewModel) {
                viewModel.container.sideEffectFlow.collect { sideEffect ->
                    handleSideEffect(
                        sideEffect = sideEffect,
                        navigator = navigator,
                        viewModel = viewModel,
                        onFinishOnboarding = onFinishOnboarding,
                    )
                }
            }

            OnboardingTimeOfBirthRoute(
                viewModel = viewModel,
            )
        }

        composable(OnboardingDestination.Name.route) {
            val viewModel = it.sharedViewModel<OnboardingViewModel>(navigator.navController)

            LaunchedEffect(viewModel) {
                viewModel.container.sideEffectFlow.collect { sideEffect ->
                    handleSideEffect(
                        sideEffect = sideEffect,
                        navigator = navigator,
                        viewModel = viewModel,
                        onFinishOnboarding = onFinishOnboarding,
                    )
                }
            }

            OnboardingNameRoute(
                viewModel = viewModel,
            )
        }

        composable(OnboardingDestination.Gender.route) {
            val viewModel = it.sharedViewModel<OnboardingViewModel>(navigator.navController)

            LaunchedEffect(viewModel) {
                viewModel.container.sideEffectFlow.collect { sideEffect ->
                    handleSideEffect(
                        sideEffect = sideEffect,
                        navigator = navigator,
                        viewModel = viewModel,
                        onFinishOnboarding = onFinishOnboarding,
                    )
                }
            }

            OnboardingGenderRoute(
                viewModel = viewModel,
            )
        }

        composable(OnboardingDestination.Access.route) {
            val viewModel = it.sharedViewModel<OnboardingViewModel>(navigator.navController)

            LaunchedEffect(viewModel) {
                viewModel.container.sideEffectFlow.collect { sideEffect ->
                    handleSideEffect(
                        sideEffect = sideEffect,
                        navigator = navigator,
                        viewModel = viewModel,
                        onFinishOnboarding = onFinishOnboarding,
                    )
                }
            }

            OnboardingAccessRoute(
                navigator = navigator,
                viewModel = viewModel,
            )
        }

        composable(OnboardingDestination.Complete1.route) {
            val viewModel = it.sharedViewModel<OnboardingViewModel>(navigator.navController)

            LaunchedEffect(viewModel) {
                viewModel.container.sideEffectFlow.collect { sideEffect ->
                    handleSideEffect(
                        sideEffect = sideEffect,
                        navigator = navigator,
                        viewModel = viewModel,
                        onFinishOnboarding = onFinishOnboarding,
                    )
                }
            }

            OnboardingCompleteRoute(
                viewModel = viewModel,
            )
        }

        composable(OnboardingDestination.Complete2.route) {
            val viewModel = it.sharedViewModel<OnboardingViewModel>(navigator.navController)

            LaunchedEffect(viewModel) {
                viewModel.container.sideEffectFlow.collect { sideEffect ->
                    handleSideEffect(
                        sideEffect = sideEffect,
                        navigator = navigator,
                        viewModel = viewModel,
                        onFinishOnboarding = onFinishOnboarding,
                    )
                }
            }

            OnboardingCompleteRoute2(
                viewModel = viewModel,
                onFinishOnboarding = onFinishOnboarding,
            )
        }
    }
}

private fun handleSideEffect(
    sideEffect: OnboardingContract.SideEffect,
    navigator: OrbitNavigator,
    viewModel: OnboardingViewModel,
    onFinishOnboarding: () -> Unit,
) {
    when (sideEffect) {
        is OnboardingContract.SideEffect.Navigate -> navigator.navigateTo(
            route = sideEffect.route,
            popUpTo = sideEffect.popUpTo,
            inclusive = sideEffect.inclusive,
        )

        OnboardingContract.SideEffect.NavigateBack -> navigator.navigateBack()
        OnboardingContract.SideEffect.OnboardingCompleted -> onFinishOnboarding()
        OnboardingContract.SideEffect.ResetField -> viewModel.processAction(
            OnboardingContract.Action.Reset,
        )
    }
}
