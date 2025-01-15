package com.kms.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.kms.onboarding.component.OnboardingGenderToggle
import com.kms.onboarding.component.UserInfoBottomSheet
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.ui.utils.heightForScreenPercentage
import com.yapp.ui.utils.paddingForScreenPercentage
import com.yapp.ui.utils.widthForScreenPercentage
import feature.onboarding.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingGenderScreen(
    state: OnboardingContract.State,
    currentStep: Int,
    totalSteps: Int,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    onGenderSelect: (String) -> Unit,
    toggleBottomSheet: () -> Unit,
) {
    OnboardingScreen(
        currentStep = currentStep,
        totalSteps = totalSteps,
        isButtonEnabled = state.selectedGender != null,
        onNextClick = {
            toggleBottomSheet()
        },
        onBackClick = onBackClick,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.heightForScreenPercentage(0.05f))
            Text(
                text = stringResource(id = R.string.onboarding_step6_text_title),
                style = OrbitTheme.typography.heading1SemiBold,
                color = OrbitTheme.colors.white,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .paddingForScreenPercentage(topPercentage = 0.11f),
                horizontalArrangement = Arrangement.Center,
            ) {
                OnboardingGenderToggle(
                    label = "남성",
                    isSelected = state.selectedGender == "남성",
                    onToggle = { onGenderSelect("남성") },
                )
                Spacer(modifier = Modifier.widthForScreenPercentage(0.04f))
                OnboardingGenderToggle(
                    label = "여성",
                    isSelected = state.selectedGender == "여성",
                    onToggle = { onGenderSelect("여성") },
                )
            }
        }
    }
    // TODO: 추후 실제 유저 정보로
    UserInfoBottomSheet(
        isSheetOpen = state.isBottomSheetOpen,
        onDismissRequest = { toggleBottomSheet() },
        onNextClick = onNextClick,
        name = "오르비",
        gender = "남성",
        birthDate = "1999년 7월 8일",
        birthTime = "12:00",
    )
}

@Composable
@Preview
fun OnboardingGenderScreenPreview() {
    OnboardingGenderScreen(
        state = OnboardingContract.State(
            isButtonEnabled = true,
        ),
        currentStep = 0,
        totalSteps = 0,
        onNextClick = {},
        onBackClick = {},
        onGenderSelect = {},
        toggleBottomSheet = {},
    )
}
