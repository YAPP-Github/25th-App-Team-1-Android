package com.yapp.home.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.home.HomeContract
import feature.home.R

@Composable
internal fun AlarmSortDropDownMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    sortOrder: HomeContract.AlarmSortOrder,
    onDismissRequest: () -> Unit,
    onSetSortOrder: (HomeContract.AlarmSortOrder) -> Unit,
) {
    DropdownMenu(
        modifier = modifier.padding(horizontal = 8.dp),
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        containerColor = OrbitTheme.colors.gray_700,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, OrbitTheme.colors.gray_600),
    ) {
        AlarmSortDropDownMenuItem(
            text = stringResource(id = R.string.alarm_list_bottom_sheet_menu_sort_default),
            checked = sortOrder == HomeContract.AlarmSortOrder.DEFAULT,
            onClick = {
                onSetSortOrder(HomeContract.AlarmSortOrder.DEFAULT)
            },
        )
        AlarmSortDropDownMenuItem(
            text = stringResource(id = R.string.alarm_list_bottom_sheet_menu_sort_activation),
            checked = sortOrder == HomeContract.AlarmSortOrder.ACTIVATION,
            onClick = {
                onSetSortOrder(HomeContract.AlarmSortOrder.ACTIVATION)
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlarmSortDropDownMenuItem(
    text: String,
    checked: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    CompositionLocalProvider(
        LocalRippleConfiguration provides RippleConfiguration(
            rippleAlpha = RippleAlpha(
                pressedAlpha = 1f,
                focusedAlpha = 1f,
                hoveredAlpha = 1f,
                draggedAlpha = 1f,
            ),
        ),
    ) {
        Surface(
            modifier = Modifier
                .width(162.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable(
                    interactionSource = interactionSource,
                    indication = ripple(
                        bounded = false,
                        color = OrbitTheme.colors.gray_600,
                    ),
                    onClick = onClick,
                ),
            color = OrbitTheme.colors.gray_700,
            shape = RoundedCornerShape(12.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = text,
                    style = OrbitTheme.typography.body1SemiBold,
                    color = OrbitTheme.colors.white,
                )

                if (checked) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = core.designsystem.R.drawable.ic_check),
                        contentDescription = "Icon",
                        tint = OrbitTheme.colors.white,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun AlarmSortDropDownMenuPreview() {
    OrbitTheme {
        AlarmSortDropDownMenu(
            expanded = true,
            sortOrder = HomeContract.AlarmSortOrder.DEFAULT,
            onDismissRequest = {},
            onSetSortOrder = {},
        )
    }
}
