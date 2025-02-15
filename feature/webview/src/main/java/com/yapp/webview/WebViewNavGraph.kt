package com.yapp.webview

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.yapp.common.navigation.OrbitNavigator
import com.yapp.common.navigation.destination.WebViewDestination

fun NavGraphBuilder.webViewNavGraph(navigator: OrbitNavigator) {
    navigation(
        route = WebViewDestination.Route.route,
        startDestination = WebViewDestination.WebView.route,
    ) {
        composable(
            route = "${WebViewDestination.WebView.route}/{url}",
            arguments = listOf(navArgument("url") { type = NavType.StringType }),
        ) { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url") ?: ""
            WebViewRoute(
                url = url,
                navController = navigator.navController,
            )
        }
    }
}
