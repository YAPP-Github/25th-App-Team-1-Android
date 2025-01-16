package com.yapp.navigator

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yapp.common.navigation.TopLevelDestination
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun OrbitBottomNavigationBar(
    visible: Boolean,
    currentTab: TopLevelDestination?,
    entries: ImmutableList<TopLevelDestination>,
    onClickItem: (TopLevelDestination) -> Unit,
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

@Composable
fun RowScope.NavItem(
    modifier: Modifier = Modifier,
    selected: Boolean,
    label: String,
    @DrawableRes iconId: Int,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .weight(1f)
            .fillMaxHeight()
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = label,
            tint = if (selected) Color.Blue else Color.Gray,
        )
        Text(text = label, color = if (selected) Color.Blue else Color.Black)
    }
}
