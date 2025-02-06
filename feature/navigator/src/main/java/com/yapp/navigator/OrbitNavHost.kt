package com.yapp.navigator

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import com.kms.onboarding.onboardingNavGraph
import com.yapp.common.navigation.OrbitNavigator
import com.yapp.common.navigation.destination.TopLevelDestination
import com.yapp.common.navigation.rememberOrbitNavigator
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.home.homeNavGraph
import com.yapp.mission.missionNavGraph
import com.yapp.mypage.myPageNavGraph
import com.yapp.ui.component.snackbar.OrbitSnackBar
import kotlinx.collections.immutable.toImmutableList

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun OrbitNavHost(
    modifier: Modifier = Modifier,
    navigator: OrbitNavigator = rememberOrbitNavigator(),
) {
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            OrbitBottomNavigationBar(
                visible = false,
                currentTab = navigator.currentTab,
                entries = TopLevelDestination.entries.toImmutableList(),
                onClickItem = navigator::navigateToTopLevelDestination,
            )
        },
        snackbarHost = {
            OrbitSnackBarHost(snackBarHostState = snackBarHostState)
        },
        containerColor = OrbitTheme.colors.gray_900,
    ) {
        NavHost(
            navController = navigator.navController,
            startDestination = navigator.startDestination,
            modifier = Modifier.navigationBarsPadding(),
        ) {
            onboardingNavGraph(
                navigator = navigator,
                onFinishOnboarding = { navigator.navigateToTopLevelDestination(TopLevelDestination.HOME) },
            )
            homeNavGraph(
                navigator = navigator,
                snackBarHostState = snackBarHostState,
            )
            myPageNavGraph(
                navigator = navigator,
            )
            missionNavGraph(
                navigator = navigator,
            )
        }
    }
}

@Composable
private fun OrbitSnackBarHost(
    snackBarHostState: SnackbarHostState,
) {
    SnackbarHost(
        hostState = snackBarHostState,
        snackbar = { data ->
            OrbitSnackBar(
                modifier = Modifier.padding(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 12.dp,
                ),
                label = data.visuals.actionLabel ?: "",
                iconRes = core.designsystem.R.drawable.ic_check_green,
                message = data.visuals.message,
                onAction = { snackBarHostState.currentSnackbarData?.performAction() },
            )
        },
    )
}
