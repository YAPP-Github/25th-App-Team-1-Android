package com.yapp.fortune.component

import androidx.compose.foundation.layout.Spacer
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
import com.yapp.ui.extensions.customClickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FortuneTopAppBar(
    titleLabel: String,
    onCloseClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = titleLabel,
                style = OrbitTheme.typography.body1SemiBold,
                color = OrbitTheme.colors.white,
            )
        },
        actions = {
            Icon(
                painter = painterResource(id = core.designsystem.R.drawable.ic_close),
                contentDescription = "Close",
                modifier = Modifier
                    .customClickable(
                        rippleEnabled = false,
                        fadeOnPress = true,
                        pressedAlpha = 0.5f,
                        onClick = onCloseClick,
                    ),
                tint = OrbitTheme.colors.white,
            )
            Spacer(modifier = Modifier.padding(end = 12.dp))
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
    )
}

@Composable
@Preview
fun FortuneTopAppBarPreview() {
    FortuneTopAppBar(
        titleLabel = "미래에서 온 편지",
        onCloseClick = {},
    )
}
