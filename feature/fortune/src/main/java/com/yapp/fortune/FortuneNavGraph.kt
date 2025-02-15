package com.yapp.fortune

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.yapp.common.navigation.OrbitNavigator
import com.yapp.common.navigation.destination.FortuneDestination
import com.yapp.common.navigation.extensions.sharedHiltViewModel
import com.yapp.ui.component.snackbar.showCustomSnackBar

fun NavGraphBuilder.fortuneNavGraph(
    navigator: OrbitNavigator,
    snackBarHostState: SnackbarHostState,
) {
    navigation(
        route = FortuneDestination.Route.route,
        startDestination = FortuneDestination.Fortune.route,
    ) {
        FortuneDestination.routes.forEach { destination ->
            composable(destination.route) { backStackEntry ->
                val viewModel = backStackEntry.sharedHiltViewModel<FortuneViewModel>(navigator.navController)
                val coroutineScope = rememberCoroutineScope()

                LaunchedEffect(viewModel) {
                    viewModel.container.sideEffectFlow.collect { sideEffect ->
                        when (sideEffect) {
                            is FortuneContract.SideEffect.Navigate -> navigator.navigateTo(
                                route = sideEffect.route,
                                popUpTo = sideEffect.popUpTo,
                                inclusive = sideEffect.inclusive,
                            )

                            FortuneContract.SideEffect.NavigateBack -> navigator.navigateBack()

                            is FortuneContract.SideEffect.ShowSnackBar -> showCustomSnackBar(
                                scope = coroutineScope,
                                snackBarHostState = snackBarHostState,
                                message = sideEffect.message,
                                actionLabel = sideEffect.label,
                                iconRes = sideEffect.iconRes,
                                bottomPadding = sideEffect.bottomPadding,
                                durationMillis = sideEffect.durationMillis,
                                onDismiss = sideEffect.onDismiss,
                                onAction = sideEffect.onAction,
                            )
                        }
                    }
                }

                when (destination) {
                    FortuneDestination.Fortune -> FortuneRoute(viewModel)
                    FortuneDestination.Reward -> FortuneRewardRoute(viewModel)
                    else -> {}
                }
            }
        }
    }
}

@Composable
private fun handleFortuneSideEffect(
    sideEffect: FortuneContract.SideEffect,
    navigator: OrbitNavigator,
    snackBarHostState: SnackbarHostState,
) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(sideEffect) {
    }
}
