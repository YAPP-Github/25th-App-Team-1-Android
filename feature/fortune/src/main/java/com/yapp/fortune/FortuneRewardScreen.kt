package com.yapp.fortune

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.fortune.component.FortuneTopAppBar
import com.yapp.ui.component.button.OrbitButton
import com.yapp.ui.utils.heightForScreenPercentage

@Composable
fun FortuneRewardRoute(
    viewModel: FortuneViewModel = hiltViewModel(),
) {
    FortuneRewardScreen()
}

@Composable
fun FortuneRewardScreen() {
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
                text = "문수,\n부적에 소원을 적으면\n이루어질거야!",
                style = OrbitTheme.typography.H2,
                color = OrbitTheme.colors.white,
            )
            Spacer(modifier = Modifier.heightForScreenPercentage(0.0467f))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = painterResource(id = core.designsystem.R.drawable.ic_fortune_reward_amulet),
                    contentDescription = null,
                )
                Text(
                    modifier = Modifier.padding(top = 30.dp),
                    text = "운세점수 100점",
                    style = OrbitTheme.typography.H2,
                    color = OrbitTheme.colors.blue_3,
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            OrbitButton(
                label = "앨범에 저장",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 14.dp),
                onClick = {},
                enabled = true,
            )
            Row(
                modifier = Modifier.wrapContentWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "공유하기",
                    style = OrbitTheme.typography.body1SemiBold,
                    color = OrbitTheme.colors.white,
                )
                Icon(
                    painter = painterResource(id = core.designsystem.R.drawable.ic_share),
                    contentDescription = null,
                    tint = Color.Unspecified,
                )
            }
        }
    }
}

@Composable
@Preview
fun PreviewRewardScreen() {
    FortuneRewardScreen()
}
