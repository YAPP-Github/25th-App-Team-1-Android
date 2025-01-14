package com.yapp.alarm

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.ui.component.timepicker.OrbitPicker

@Composable
fun AlarmAddEditRoute() {
    AlarmAddEditScreen()
}

@Composable
fun AlarmAddEditScreen() {
    var selectedAmPm by remember { mutableStateOf("오전") }
    var selectedHour by remember { mutableIntStateOf(1) }
    var selectedMinute by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AlarmAddEditTopBar(
            title = "1일 12시간 후에 울려요",
            onBack = { },
        )
        OrbitPicker(
            modifier = Modifier.padding(top = 40.dp),
            selectedAmPm = selectedAmPm,
            selectedHour = selectedHour,
            selectedMinute = selectedMinute,
        ) { amPm, hour, minute ->
            selectedAmPm = amPm
            selectedHour = hour
            selectedMinute = minute
        }
    }
}

@Composable
private fun AlarmAddEditTopBar(
    title: String,
    onBack: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .statusBarsPadding(),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(id = core.designsystem.R.drawable.ic_back),
            contentDescription = "Back",
            tint = OrbitTheme.colors.white,
            modifier = Modifier
                .clickable(onClick = onBack)
                .padding(start = 20.dp)
                .align(Alignment.CenterStart),
        )

        Text(
            title,
            style = OrbitTheme.typography.body1SemiBold,
            color = OrbitTheme.colors.white,
        )
    }
}

@Preview
@Composable
fun AlarmAddEditScreenPreview() {
    AlarmAddEditScreen()
}
