package com.yapp.fortune.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FortuneTopAppBar(
    onCloseClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "미래에서 온 편지",
                style = OrbitTheme.typography.body1SemiBold,
                color = OrbitTheme.colors.white,
            )
        },
        actions = {
            Icon(
                painter = painterResource(id = core.designsystem.R.drawable.ic_close),
                contentDescription = "Close",
                modifier = Modifier
                    .clickable(onClick = onCloseClick)
                    .padding(end = 12.dp),
                tint = OrbitTheme.colors.white,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
    )
}

@Composable
@Preview
fun FortuneTopAppBarPreview() {
    FortuneTopAppBar(onCloseClick = {})
}
