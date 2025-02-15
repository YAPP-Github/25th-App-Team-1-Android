package com.yapp.setting.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingTopAppBar(
    onBackClick: (() -> Unit)? = null,
    onActionClick: () -> Unit = {},
    showTopAppBarActions: Boolean = true,
    title: String,
    actionTitle: String? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    TopAppBar(
        title = {
            Text(
                text = title,
                style = OrbitTheme.typography.body1SemiBold,
                color = OrbitTheme.colors.white,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        },
        navigationIcon = {
            if (onBackClick != null) {
                Icon(
                    painter = painterResource(id = core.designsystem.R.drawable.ic_back),
                    contentDescription = "Back",
                    tint = OrbitTheme.colors.white,
                    modifier = Modifier
                        .clickable(onClick = onBackClick)
                        .padding(start = 20.dp),
                )
            }
        },
        actions = {
            if (showTopAppBarActions) {
                Row(
                    modifier = Modifier.padding(end = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = actionTitle ?: "",
                        style = OrbitTheme.typography.body1Medium,
                        color = if (isPressed) OrbitTheme.colors.main else OrbitTheme.colors.gray_500,
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null,
                                onClick = onActionClick,
                            ),
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = OrbitTheme.colors.gray_900),
    )
}

@Composable
@Preview
fun SettingTopAppBarPreview() {
    SettingTopAppBar(
        onBackClick = { },
        title = "설정",
        actionTitle = "완료",
    )
}
