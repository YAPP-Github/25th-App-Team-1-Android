package com.yapp.alarm.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.ui.extensions.customClickable

@Composable
internal fun AlarmCheckItem(
    label: String,
    isPressed: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier.customClickable(
            rippleEnabled = false,
            onClick = onClick,
        ).padding(2.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = core.designsystem.R.drawable.ic_check),
            contentDescription = "Check",
            tint = if (isPressed) OrbitTheme.colors.main else OrbitTheme.colors.white,
        )
        Text(
            text = label,
            style = OrbitTheme.typography.body1Medium,
            color = if (isPressed) OrbitTheme.colors.main else OrbitTheme.colors.white,
            modifier = Modifier.padding(start = 4.dp),
            textAlign = TextAlign.Center,
        )
    }
}
