package com.yapp.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.setting.component.SettingTopAppBar
import com.yapp.ui.component.dialog.OrbitDialog
import com.yapp.ui.component.timepicker.OrbitYearMonthPicker
import com.yapp.ui.utils.heightForScreenPercentage

@Composable
fun EditBirthdayRoute(
    viewModel: EditProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    EditBirthdayScreen(
        state = state,
        onBack = { viewModel.onAction(SettingContract.Action.PreviousStep) },
        onConfirmExit = {
            viewModel.onAction(SettingContract.Action.HideDialog)
            viewModel.onAction(SettingContract.Action.PreviousStep)
        },
        onCancelDialog = { viewModel.onAction(SettingContract.Action.HideDialog) },
        onUpdateBirthDate = { lunar, year, month, day ->
            viewModel.onAction(
                SettingContract.Action.UpdateBirthDate(
                    lunar,
                    year,
                    month,
                    day,
                ),
            )
        },
        onConfirm = { viewModel.onAction(SettingContract.Action.ConfirmAndNavigateBack) },
    )
}

@Composable
fun EditBirthdayScreen(
    state: SettingContract.State,
    onBack: () -> Unit,
    onConfirm: () -> Unit,
    onConfirmExit: () -> Unit,
    onCancelDialog: () -> Unit,
    onUpdateBirthDate: (String, Int, Int, Int) -> Unit,
) {
    val (year, month, day) = state.birthDate.split("-")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OrbitTheme.colors.gray_900)
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SettingTopAppBar(
            onBackClick = onBack,
            showTopAppBarActions = true,
            title = "생년월일 수정",
            actionTitle = "확인",
            onActionClick = {
                onConfirm()
            },
        )
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "생년월일을 알려주세요",
            style = OrbitTheme.typography.title3SemiBold,
            color = OrbitTheme.colors.white,
        )
        Spacer(modifier = Modifier.heightForScreenPercentage(0.16f))

        OrbitYearMonthPicker(
            initialLunar = state.birthType,
            initialYear = year,
            initialMonth = month,
            initialDay = day,
        ) { lunar, year, month, day ->
            onUpdateBirthDate(lunar, year, month, day)
        }
    }

    if (state.isDialogVisible) {
        OrbitDialog(
            title = "변경 사항 삭제",
            message = "변경 사항을 저장하지 않고\n나가시겠어요?",
            confirmText = "나가기",
            cancelText = "취소",
            onConfirm = onConfirmExit,
            onCancel = onCancelDialog,
        )
    }
}

@Composable
@Preview
fun PreviewEditBirthdayScreen() {
    EditBirthdayScreen(
        state = SettingContract.State(),
        onBack = {},
        onConfirm = {},
        onConfirmExit = {},
        onCancelDialog = {},
        onUpdateBirthDate = { _, _, _, _ -> },
    )
}
