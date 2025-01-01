package com.yapp.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

object OrbitTheme {
    val colors: OrbitColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    val typography: OrbitTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current
}
