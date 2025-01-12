package com.yapp.ui.utils

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.heightForScreenPercentage(percentage: Float): Modifier {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    return this.height(screenHeight * percentage)
}

@Composable
fun Modifier.widthForScreenPercentage(percentage: Float): Modifier {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    return this.width(screenWidth * percentage)
}

@Composable
fun Modifier.paddingForScreenPercentage(
    horizontalPercentage: Float = 0f,
    verticalPercentage: Float = 0f,
    topPercentage: Float = 0f,
    bottomPercentage: Float = 0f,
): Modifier {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val horizontalPadding = screenWidth * horizontalPercentage
    val verticalPadding = screenHeight * verticalPercentage
    val topPadding = screenHeight * topPercentage + verticalPadding
    val bottomPadding = screenHeight * bottomPercentage + verticalPadding

    return this.padding(
        start = horizontalPadding,
        top = topPadding,
        end = horizontalPadding,
        bottom = bottomPadding,
    )
}
