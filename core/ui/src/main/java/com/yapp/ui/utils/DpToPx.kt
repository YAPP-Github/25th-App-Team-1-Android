package com.yapp.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun Dp.toPx(): Float {
    return with(LocalDensity.current) { this@toPx.toPx() }
}

@Composable
fun Dp.toRoundPx(): Int {
    return with(LocalDensity.current) { this@toRoundPx.roundToPx() }
}
