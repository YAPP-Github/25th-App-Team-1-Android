package com.yapp.fortune

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.yapp.common.navigation.OrbitNavigator
import com.yapp.common.navigation.destination.FortuneDestination
import com.yapp.common.navigation.extensions.sharedViewModel

fun NavGraphBuilder.fortuneNavGraph(
    navigator: OrbitNavigator,
) {
    navigation(
        route = FortuneDestination.Route.route,
        startDestination = FortuneDestination.Fortune.route,
    ) {
        FortuneDestination.routes.forEach { destination ->
            composable(destination.route) { backStackEntry ->
                val viewModel = backStackEntry.sharedViewModel<FortuneViewModel>(navigator.navController)

                LaunchedEffect(viewModel) {
                    viewModel.container.sideEffectFlow.collect { sideEffect ->
                        handleFortuneSideEffect(sideEffect, navigator, viewModel)
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

private fun handleFortuneSideEffect(
    sideEffect: FortuneContract.SideEffect,
    navigator: OrbitNavigator,
    viewModel: FortuneViewModel,
) {
    when (sideEffect) {
        is FortuneContract.SideEffect.Navigate -> navigator.navigateTo(
            route = sideEffect.route,
            popUpTo = sideEffect.popUpTo,
            inclusive = sideEffect.inclusive,
        )

        FortuneContract.SideEffect.NavigateBack -> navigator.navigateBack()
    }
}
