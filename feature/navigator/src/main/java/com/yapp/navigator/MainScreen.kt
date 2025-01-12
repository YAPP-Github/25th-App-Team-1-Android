package com.yapp.navigator

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.home.homeNavGraph
import com.yapp.mypage.mypageNavGraph
import com.yapp.navigator.navigation.MainNavTab
import com.yapp.navigator.navigation.MainNavigator
import com.yapp.navigator.navigation.rememberMainNavigator
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun MainScreen(
    modifier: Modifier = Modifier,
    navigator: MainNavigator = rememberMainNavigator(),
) {
    Scaffold(
        modifier = modifier,
        bottomBar = {
            MainBottomNavigationBar(
                visible = navigator.shouldShowBottomBar(),
                currentTab = navigator.currentTab,
                entries = MainNavTab.entries.toImmutableList(),
                onClickItem = navigator::navigate,
            )
        },
        containerColor = OrbitTheme.colors.gray_900
    ) { innerPadding ->
        NavHost(
            navController = navigator.navController,
            startDestination = navigator.startDestination,
            modifier = Modifier.padding(innerPadding),
        ) {
            homeNavGraph(padding = innerPadding)
            mypageNavGraph(padding = innerPadding)
        }
    }
}
