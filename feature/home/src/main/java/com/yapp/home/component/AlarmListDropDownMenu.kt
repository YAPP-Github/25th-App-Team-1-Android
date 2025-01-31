package com.yapp.home.component

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.ui.extensions.customClickable
import feature.home.R

@Composable
private fun AlarmListDropDownMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onClickEdit: () -> Unit,
) {
    DropdownMenu(
        modifier = modifier.padding(6.dp),
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        containerColor = OrbitTheme.colors.gray_700,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, OrbitTheme.colors.gray_600),
    ) {
        AlarmListDropDownMenuItem(
            text = stringResource(id = R.string.alarm_list_bottom_sheet_menu_edit),
            iconRes = core.designsystem.R.drawable.ic_edit,
        ) {
            onClickEdit()
        }
    }
}

@Composable
private fun AlarmListDropDownMenuItem(
    text: String,
    @DrawableRes iconRes: Int,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .width(120.dp)
            .customClickable(
                onClick = onClick,
                rippleEnabled = false,
            )
            .padding(
                horizontal = 14.dp,
                vertical = 8.dp,
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = text,
            style = OrbitTheme.typography.body1SemiBold,
            color = OrbitTheme.colors.white,
        )

        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(id = iconRes),
            contentDescription = "Icon",
            tint = OrbitTheme.colors.white,
        )
    }
}

@Preview
@Composable
private fun AlarmListDropDownMenuPreview() {
    var expanded by remember { mutableStateOf(false) }

    OrbitTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            Box {
                Button(
                    onClick = { expanded = true },
                ) {
                    Text("Show Menu")
                }
                AlarmListDropDownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    onClickEdit = {
                        Log.d("AlarmListDropDownMenu", "Edit Clicked")
                    },
                )
            }
        }
    }
}
