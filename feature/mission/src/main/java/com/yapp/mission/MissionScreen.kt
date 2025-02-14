package com.yapp.mission

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.ui.component.button.OrbitButton
import com.yapp.ui.component.dialog.OrbitDialog
import com.yapp.ui.utils.heightForScreenPercentage

@Composable
fun MissionRoute(viewModel: MissionViewModel = hiltViewModel()) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    MissionScreen(
        stateProvider = { state },
        eventDispatcher = viewModel::processAction,
        onNext = { viewModel.processAction(MissionContract.Action.NextStep) },
    )
}

@Composable
fun MissionScreen(
    stateProvider: () -> MissionContract.State,
    eventDispatcher: (MissionContract.Action) -> Unit,
    onNext: () -> Unit,
) {
    val state = stateProvider()
    val context = LocalContext.current

    BackHandler {
        if (state.showExitDialog) {
            eventDispatcher(MissionContract.Action.HideExitDialog)
        } else {
            eventDispatcher(MissionContract.Action.ShowExitDialog)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Image(
            painter = painterResource(id = core.designsystem.R.drawable.img_mission_main_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize(),
        )

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.heightForScreenPercentage(0.110f))
                MissionTag(label = "기상미션")
                Spacer(modifier = Modifier.heightForScreenPercentage(0.0418f))
                MissionLabel(label = "10회를 흔들어", style = OrbitTheme.typography.headline2Medium)
                Spacer(modifier = Modifier.heightForScreenPercentage(0.01f))
                MissionLabel(label = "부적을 뒤집어줘", style = OrbitTheme.typography.title2Bold)
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(id = core.designsystem.R.drawable.img_mission_main),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(1.1f),
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OrbitButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    label = "미션 시작",
                    onClick = onNext,
                    enabled = true,
                )
                Spacer(modifier = Modifier.heightForScreenPercentage(0.027f))
                MissionLabel(
                    label = "미션하지 않기",
                    style = OrbitTheme.typography.body1SemiBold,
                    clickable = true,
                    onClick = {
                        eventDispatcher(MissionContract.Action.ShowExitDialog)
                    },
                )
                Spacer(modifier = Modifier.heightForScreenPercentage(0.0714f))
            }
        }
    }

    if (state.showExitDialog) {
        OrbitDialog(
            title = "나가면 운세를 받을 수 없어요",
            message = "미션을 수행하지 않고 나가시겠어요?",
            confirmText = "나가기",
            cancelText = "취소",
            onConfirm = { (context as? androidx.activity.ComponentActivity)?.finish() },
            onCancel = { eventDispatcher(MissionContract.Action.HideExitDialog) },
        )
    }
}

@Composable
fun MissionTag(label: String) {
    Box(
        modifier = Modifier
            .background(
                color = OrbitTheme.colors.main.copy(alpha = 0.1f),
                shape = RoundedCornerShape(30.dp),
            )
            .padding(vertical = 4.dp, horizontal = 12.dp),
    ) {
        Text(
            text = label,
            color = OrbitTheme.colors.main,
            style = OrbitTheme.typography.body2Medium,
        )
    }
}

@Composable
fun MissionLabel(
    label: String,
    style: TextStyle,
    clickable: Boolean = false,
    onClick: () -> Unit = { },
) {
    Text(
        text = label,
        color = OrbitTheme.colors.white,
        style = style,
        modifier = if (clickable) { Modifier.clickable { onClick() } } else Modifier,
    )
}

@Composable
@Preview
fun MissionRoutePreview() {
    MissionScreen(
        stateProvider = { MissionContract.State() },
        eventDispatcher = { },
        onNext = { },
    )
}
