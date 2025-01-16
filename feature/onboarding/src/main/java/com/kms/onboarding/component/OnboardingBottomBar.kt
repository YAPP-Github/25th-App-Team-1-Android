package com.kms.onboarding.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.ui.component.button.OrbitButton

@Composable
fun OnboardingBottomBar(
    currentStep: Int,
    isButtonEnabled: Boolean,
    onNextClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.imePadding(),
    ) {
        OrbitButton(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .fillMaxWidth(),
            label = "다음",
            onClick = onNextClick,
            enabled = isButtonEnabled,
        )
        if (currentStep in 3..5) {
            AnnotatedTermsText(
                onTermsClick = { /* Handle terms click */ },
                onPrivacyPolicyClick = { /* Handle privacy policy click */ },
            )
        }
    }
}

@Composable
fun AnnotatedTermsText(
    onTermsClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
) {
    val annotatedText = buildAnnotatedString {
        append("서비스 시작 시 ")
        pushStringAnnotation(tag = "TERMS", annotation = "TERMS")
        withStyle(
            style = SpanStyle(
                textDecoration = TextDecoration.Underline,
            ),
        ) {
            append("이용약관")
        }
        pop()
        append(" 및 ")
        pushStringAnnotation(tag = "PRIVACY", annotation = "PRIVACY")
        withStyle(
            style = SpanStyle(
                textDecoration = TextDecoration.Underline,
            ),
        ) {
            append("개인정보처리방침")
        }
        pop()
        append("에 동의하게 됩니다.")
    }

    Text(
        text = annotatedText,
        style = OrbitTheme.typography.label2SemiBold.copy(textAlign = TextAlign.Center),
        color = OrbitTheme.colors.gray_500,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 10.dp)
            .clickable { /* Handle general click if needed */ },
        onTextLayout = { layoutResult ->
            layoutResult.layoutInput.text.getStringAnnotations(tag = "TERMS", start = 0, end = layoutResult.layoutInput.text.length).firstOrNull()
                ?.let { onTermsClick() }
            layoutResult.layoutInput.text.getStringAnnotations(tag = "PRIVACY", start = 0, end = layoutResult.layoutInput.text.length).firstOrNull()
                ?.let { onPrivacyPolicyClick() }
        },
    )
}
