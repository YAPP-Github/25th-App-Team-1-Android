package com.yapp.navigator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yapp.navigator.navigation.MainNavTab
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun MainBottomNavigationBar(
    visible: Boolean,
    currentTab: MainNavTab?,
    entries: ImmutableList<MainNavTab>,
    onClickItem: (MainNavTab) -> Unit,
) {
    AnimatedVisibility(visible = visible) {
        Column {
            HorizontalDivider(color = Color.Black, thickness = 1.dp)
            Row(
                modifier = Modifier
                    .height(56.dp)
                    .background(color = Color.White),
            ) {
                entries.forEach { tab ->
                    NavItem(
                        selected = tab == currentTab,
                        label = stringResource(id = tab.titleId),
                        iconId = tab.iconId,
                        onClick = { onClickItem(tab) },
                    )
                }
            }
        }
    }
}
