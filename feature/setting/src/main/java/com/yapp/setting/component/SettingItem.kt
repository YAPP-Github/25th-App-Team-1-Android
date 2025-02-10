package com.yapp.setting.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SettingItem(
    itemTitle: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = com.yapp.designsystem.theme.OrbitTheme.colors.gray_900,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = itemTitle,
            style = com.yapp.designsystem.theme.OrbitTheme.typography.body1Medium,
            color = com.yapp.designsystem.theme.OrbitTheme.colors.white,
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            painter = painterResource(id = core.designsystem.R.drawable.ic_arrow_right),
            contentDescription = "",
            tint = com.yapp.designsystem.theme.OrbitTheme.colors.gray_300,
        )
    }
}

@Composable
@Preview
fun SettingItemPreview() {
    SettingItem(itemTitle = "안녕")
}
