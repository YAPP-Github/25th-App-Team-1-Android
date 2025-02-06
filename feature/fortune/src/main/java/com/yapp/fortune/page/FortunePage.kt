package com.yapp.fortune.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yapp.fortune.FortuneContract
import com.yapp.fortune.FortuneViewModel

@Composable
fun FortunePage(
    viewModel: FortuneViewModel = hiltViewModel(),
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    when (state.currentStep) {
        0 -> FortuneFirstPage()
        in 1..4 -> {
            val index = (state.currentStep - 1).coerceIn(0, state.fortunePages.lastIndex)
            FortunePageLayout(state.fortunePages[index])
        }

        5 -> FortuneCompletePage(
            hasReward = state.hasReward,
            onCompleteClick = { viewModel.onAction(FortuneContract.Action.NextStep) },
        )

        else -> DefaultFortunePage(state.currentStep)
    }
}

@Composable
fun DefaultFortunePage(page: Int) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter,
    ) {}
}

@Composable
@Preview
fun FortunePagePreview() {
    FortunePage()
}
