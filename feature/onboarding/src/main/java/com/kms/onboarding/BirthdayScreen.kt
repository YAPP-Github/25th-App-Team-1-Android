package com.kms.onboarding

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.ui.component.timepicker.OrbitYearMonthPicker
import com.yapp.ui.utils.heightForScreenPercentage
import feature.onboarding.R

@Composable
fun BirthdayScreen(
    state: OnboardingContract.State,
    currentStep: Int,
    totalSteps: Int,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    OnboardingScreen(
        currentStep = currentStep,
        totalSteps = totalSteps,
        isButtonEnabled = true,
        onNextClick = onNextClick,
        onBackClick = onBackClick,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.heightForScreenPercentage(0.05f))
            Text(
                text = stringResource(id = R.string.onboarding_step3_text_title),
                style = OrbitTheme.typography.heading1SemiBold,
                color = OrbitTheme.colors.white,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            OrbitYearMonthPicker(
                modifier = Modifier.padding(top = 60.dp)
            ) { lunar, year, month, day ->
                Log.d("BirthdayScreen", "lunar: $lunar, year: $year, month: $month, day: $day")
            }
        }
    }
}
