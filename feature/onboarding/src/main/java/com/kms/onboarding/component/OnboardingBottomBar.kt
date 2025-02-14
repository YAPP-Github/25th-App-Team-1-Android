package com.kms.onboarding.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.ui.component.button.OrbitButton

@Composable
fun OnboardingBottomBar(
    currentStep: Int,
    isButtonEnabled: Boolean,
    onNextClick: () -> Unit,
    buttonLabel: String,
) {
    DebounceManager { manager ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.imePadding(),
        ) {
            OrbitButton(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 12.dp)
                    .fillMaxWidth(),
                label = buttonLabel,
                onClick = { manager.debounceClick { onNextClick() } },
                enabled = isButtonEnabled,
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
@Preview
fun OnboardingBottomBarPreview() {
    OrbitTheme {
        OnboardingBottomBar(
            currentStep = 3,
            isButtonEnabled = true,
            onNextClick = {},
            buttonLabel = "다음",
        )
    }
}
