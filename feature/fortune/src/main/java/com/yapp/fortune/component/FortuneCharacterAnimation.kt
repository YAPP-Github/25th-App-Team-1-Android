package com.yapp.fortune.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

@Composable
internal fun FortuneCharacter(
    modifier: Modifier = Modifier,
    fortuneScore: Int,
) {
    val bubbleRes = when (fortuneScore) {
        in 0..49 -> core.designsystem.R.drawable.ic_orbit_book
        in 50..79 -> core.designsystem.R.drawable.ic_orbit_clover
        in 80..100 -> core.designsystem.R.drawable.ic_orbit_cane
        else -> core.designsystem.R.drawable.ic_fortune_preload_speech_bubble
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = bubbleRes),
            contentDescription = "",
        )
    }
}
