package com.yapp.setting.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yapp.designsystem.theme.OrbitTheme

@Composable
fun TableOfContentsText(contentsTitle: String) {
    Text(
        text = contentsTitle,
        modifier = Modifier.fillMaxWidth(),
        style = OrbitTheme.typography.body1Regular,
        color = OrbitTheme.colors.gray_300,
    )
}
