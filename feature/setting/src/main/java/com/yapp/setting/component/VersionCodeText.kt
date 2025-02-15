package com.yapp.setting.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.yapp.designsystem.theme.OrbitTheme

@Composable
fun VersionCodeText(versionCode: String) {
    Text(
        text = versionCode,
        modifier = Modifier.fillMaxWidth(),
        style = OrbitTheme.typography.body1Regular,
        color = OrbitTheme.colors.gray_300,
        textAlign = TextAlign.Center,
    )
}
