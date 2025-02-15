package com.yapp.webview

import android.util.Log
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yapp.ui.component.lottie.LottieAnimation

@Composable
fun WebViewRoute(
    url: String,
    navController: NavController,
) {
    WebViewScreen(
        url = url,
        onBackClick = {
            if (!navController.popBackStack()) {
                Log.d("WebViewScreen", "종료")
            }
        },
    )
}

@Composable
fun WebViewScreen(
    url: String,
    onBackClick: () -> Unit,
) {
    val webViewState = remember { mutableStateOf<WebView?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    BackHandler(enabled = true) {
        val webView = webViewState.value
        if (webView?.canGoBack() == true) {
            webView.goBack()
        } else {
            onBackClick()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
    ) {
        if (isLoading) {
            LottieAnimation(
                resId = core.designsystem.R.raw.star_loading,
                modifier = Modifier.size(40.dp),
            )
        }

        OrbitWebView(
            url = url,
            modifier = Modifier.weight(1f),
            onPageStarted = {
                isLoading = true
            },
            onPageFinished = {
                isLoading = false
            },
            onError = { errorMessage ->
                Log.e("WebViewScreen", errorMessage)
                isLoading = false
            },
            webViewState = webViewState,
        )
    }
}

@Composable
@Preview
private fun WebViewRoutePreview() {
    WebViewRoute(
        url = "https://www.google.com",
        navController = NavController(LocalContext.current),
    )
}
