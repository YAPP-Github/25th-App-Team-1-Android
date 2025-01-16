package com.yapp.navigator

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.yapp.common.navigation.OrbitNavigator
import com.yapp.common.navigation.rememberOrbitNavigator
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.home.homeNavGraph
import com.yapp.mypage.mypageNavGraph
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun OrbitNavHost(
    modifier: Modifier = Modifier,
    navigator: OrbitNavigator = rememberOrbitNavigator(),
) {
    Scaffold(
        modifier = modifier,
        bottomBar = {
            OrbitBottomNavigationBar(
                visible = false,
                currentTab = navigator.c,
                entries = MainNavTab.entries.toImmutableList(),
                onClickItem = navigator::navigate,
            )
        },
        containerColor = OrbitTheme.colors.gray_900,
    ) { innerPadding ->
        NavHost(
            navController = navigator.navController,
            startDestination = navigator.startDestination,
            modifier = Modifier.padding(innerPadding),
        ) {
            homeNavGraph()
            mypageNavGraph()
        }
    }
}
