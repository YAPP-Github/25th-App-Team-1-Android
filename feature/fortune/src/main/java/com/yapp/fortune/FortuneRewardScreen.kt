package com.yapp.fortune

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.fortune.component.FortuneTopAppBar
import com.yapp.ui.component.button.OrbitButton
import com.yapp.ui.utils.heightForScreenPercentage

@Composable
fun FortuneRewardRoute(
    viewModel: FortuneViewModel = hiltViewModel(),
) {
    val state = viewModel.container.stateFlow.collectAsStateWithLifecycle()
    FortuneRewardScreen(
        state = state,
    )
}

@Composable
fun FortuneRewardScreen(
    state: State<FortuneContract.State>,
) {
    val parts = state.value.dailyFortune.split(" ")
    val nickName = parts.getOrNull(0)?.trim() ?: ""

    val randomImageRes = remember {
        listOf(
            core.designsystem.R.drawable.ic_fortune_reward1,
            core.designsystem.R.drawable.ic_fortune_reward2,
            core.designsystem.R.drawable.ic_fortune_reward3,
            core.designsystem.R.drawable.ic_fortune_reward4,
            core.designsystem.R.drawable.ic_fortune_reward5,
        ).random()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
    ) {
        Image(
            painter = painterResource(id = core.designsystem.R.drawable.ic_fortune_reward_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize(),
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            FortuneTopAppBar(
                titleLabel = "행운 부적",
                onCloseClick = {},
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp)
                    .align(Alignment.Start),
                text = "$nickName\n부적에 소원을 적으면\n이루어질거야!",
                style = OrbitTheme.typography.H1,
                color = OrbitTheme.colors.white,
            )
            Spacer(modifier = Modifier.heightForScreenPercentage(0.0467f))

            Icon(
                painter = painterResource(id = randomImageRes),
                contentDescription = null,
                tint = Color.Unspecified,
            )
            Spacer(modifier = Modifier.height(30.dp))
            Icon(
                painter = painterResource(id = core.designsystem.R.drawable.ic_shadow),
                contentDescription = null,
                tint = Color.Unspecified,
            )
            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OrbitButton(
                    label = "완료",
                    modifier = Modifier.weight(1f),
                    onClick = {},
                    enabled = true,
                    containerColor = OrbitTheme.colors.gray_600,
                    contentColor = OrbitTheme.colors.white,
                    pressedContainerColor = OrbitTheme.colors.gray_700,
                    pressedContentColor = OrbitTheme.colors.white,
                )
                OrbitButton(
                    label = "앨범에 저장",
                    modifier = Modifier.weight(1f),
                    onClick = {},
                    enabled = true,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFortuneRewardScreen() {
    val fakeState = remember {
        mutableStateOf(
            FortuneContract.State(
                dailyFortune = "오르비, 오늘은 기회가 많으니 적극적으로 움직여봐!",
                hasReward = true,
            ),
        )
    }

    FortuneRewardScreen(state = fakeState)
}
