package com.yapp.fortune.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal fun SlidingIndicator(
    pagerState: PagerState,
    count: Int,
    dotHeight: Dp,
    spacing: Dp,
    inactiveColor: Color,
    activeColor: Color,
) {
    val currentPage by remember { derivedStateOf { pagerState.currentPage } }
    val currentConfiguration = LocalConfiguration.current

    val horizontalPadding = 20.dp
    val totalSpacing = spacing * (count - 1)
    val screenWidth = currentConfiguration.screenWidthDp.dp
    val availableWidth = screenWidth - (horizontalPadding * 2)
    val dotWidth = remember { derivedStateOf { availableWidth / count - (totalSpacing / count) } }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding, vertical = 8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(count) { index ->
                Box(
                    modifier = Modifier
                        .width(dotWidth.value)
                        .height(dotHeight)
                        .background(
                            color = if (index <= currentPage) activeColor else inactiveColor,
                            shape = RoundedCornerShape(3.dp),
                        ),
                )

                if (index < count - 1) {
                    Spacer(modifier = Modifier.width(spacing))
                }
            }
        }
    }
}
