package com.kms.onboarding.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBoardingTopAppBar(
    currentStep: Int,
    totalSteps: Int,
    onBackClick: (() -> Unit)? = null,
    showTopAppBarActions: Boolean = true,
) {
    TopAppBar(
        title = { },
        navigationIcon = {
            if (onBackClick != null) {
                Icon(
                    painter = painterResource(id = core.designsystem.R.drawable.ic_back),
                    contentDescription = "Back",
                    tint = OrbitTheme.colors.white,
                    modifier = Modifier
                        .clickable(onClick = onBackClick)
                        .padding(start = 20.dp),
                )
            }
        },
        actions = {
            if (showTopAppBarActions) {
                Box(
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .width(54.dp)
                        .height(30.dp)
                        .background(OrbitTheme.colors.gray_700, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "$currentStep/$totalSteps",
                        style = OrbitTheme.typography.body1Medium,
                        color = OrbitTheme.colors.gray_200,
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = OrbitTheme.colors.gray_900),
    )
}

@Composable
@Preview
fun OnBoardingTopAppBarPreview() {
    OnBoardingTopAppBar(currentStep = 1, totalSteps = 3)
}
