package com.yapp.common.navigation.destination

import com.yapp.common.navigation.Routes

sealed class WebViewDestination(val route: String) {
    object Route : WebViewDestination(Routes.WebView.ROUTE)
    object WebView : WebViewDestination(Routes.WebView.WEBVIEW)
}
