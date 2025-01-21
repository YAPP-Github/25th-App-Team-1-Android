package com.yapp.navigator

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.kms.onboarding.onboardingNavGraph
import com.yapp.common.navigation.OrbitNavigator
import com.yapp.common.navigation.destination.TopLevelDestination
import com.yapp.common.navigation.rememberOrbitNavigator
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.home.homeNavGraph
import com.yapp.mission.missionNavGraph
import com.yapp.mypage.myPageNavGraph
import kotlinx.collections.immutable.toImmutableList

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun OrbitNavHost(
    modifier: Modifier = Modifier,
    navigator: OrbitNavigator = rememberOrbitNavigator(),
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            OrbitBottomNavigationBar(
                visible = false,
                currentTab = navigator.currentTab,
                entries = TopLevelDestination.entries.toImmutableList(),
                onClickItem = navigator::navigateToTopLevelDestination,
            )
        },
        containerColor = OrbitTheme.colors.gray_900,
    ) {
        NavHost(
            navController = navigator.navController,
            startDestination = navigator.startDestination,
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding(),
        ) {
            onboardingNavGraph(
                navigator = navigator,
                onFinishOnboarding = { navigator.navigateToTopLevelDestination(TopLevelDestination.HOME) },
            )
            homeNavGraph(
                navigator = navigator,
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
