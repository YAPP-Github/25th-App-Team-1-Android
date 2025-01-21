package com.yapp.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember

fun orbitColors() = OrbitColors()
fun orbitFonts() = OrbitTypography()

@Composable
fun OrbitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colors: OrbitColors = orbitColors(),
    darkColors: OrbitColors = orbitColors(),
    typography: OrbitTypography = orbitFonts(),
    content: @Composable () -> Unit,
) {
    val currentColor = remember { if (darkTheme) darkColors else colors }
    val rememberedColors = remember { currentColor.copy() }.apply { updateColorFrom(currentColor) }
    CompositionLocalProvider(
        LocalColors provides rememberedColors,
        LocalTypography provides typography,
    ) {
        ProvideTextStyle(typography.body2Medium, content = content)
    }
}
