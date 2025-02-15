package com.yapp.setting

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    Log.d("EditBirthdayRoute", "Received birthDate: ${state.birthDate}")
    val cleanedBirthDate = extractBirthDate(state.birthDate)
    val birthDateParts =
        cleanedBirthDate.split("-").takeIf { it.size == 3 } ?: listOf("2000", "01", "01")

    val initialYear = birthDateParts[0]
    val initialMonth = birthDateParts[1]
    val initialDay = birthDateParts[2]
    Log.d(
        "EditBirthdayRoute",
        "Parsed values -> Year: $initialYear, Month: $initialMonth, Day: $initialDay",
    )

    EditBirthdayScreen(
        state = state,
        initialYear = initialYear,
        initialMonth = initialMonth,
        initialDay = initialDay,
        onBack = { viewModel.onAction(SettingContract.Action.PreviousStep) },
        onConfirmExit = {
            viewModel.onAction(SettingContract.Action.HideDialog)
            viewModel.onAction(SettingContract.Action.PreviousStep)
        },
        onCancelDialog = { viewModel.onAction(SettingContract.Action.HideDialog) },
        onUpdateBirthDate = { birthDate ->
            viewModel.onAction(
                SettingContract.Action.UpdateBirthDate(
                    birthDate,
                ),
            )
        },
        onUpdateCalendarType = { calendarType ->
            viewModel.onAction(
                SettingContract.Action.UpdateCalendarType(
                    calendarType,
                ),
            )
        },
        onConfirm = { viewModel.onAction(SettingContract.Action.ConfirmAndNavigateBack) },
    )
}

/**
 * ✅ `양력 1997년 1월 1일` → `1997-01-01` 변환 함수
 */
private fun extractBirthDate(formattedDate: String): String {
    return try {
        val regex = Regex("""(\d{4})년 (\d{1,2})월 (\d{1,2})일""")
        val matchResult = regex.find(formattedDate)

        if (matchResult != null) {
            val (year, month, day) = matchResult.destructured
            "$year-${month.padStart(2, '0')}-${day.padStart(2, '0')}"
        } else {
            formattedDate
        }
    } catch (e: Exception) {
        Log.e("EditProfileViewModel", "extractBirthDate 오류: ${e.message}")
        "2000-01-01"
    }
}

@Composable
fun EditBirthdayScreen(
    state: SettingContract.State,
    initialYear: String,
    initialMonth: String,
    initialDay: String,
    onBack: () -> Unit,
    onConfirm: () -> Unit,
    onConfirmExit: () -> Unit,
    onCancelDialog: () -> Unit,
    onUpdateBirthDate: (String) -> Unit,
    onUpdateCalendarType: (String) -> Unit,
) {
    var selectedLunar by remember { mutableStateOf(state.calendarType) }

    var selectedYear by remember { mutableStateOf(initialYear) }
    var selectedMonth by remember { mutableStateOf(initialMonth) }
    var selectedDay by remember { mutableStateOf(initialDay) }

    LaunchedEffect(Unit) {
        Log.d(
            "EditBirthdayScreen",
            "LaunchedEffect -> Year: $initialYear, Month: $initialMonth, Day: $initialDay",
        )
        selectedYear = initialYear
        selectedMonth = initialMonth
        selectedDay = initialDay
    }

    Log.d(
        "EditBirthdayScreen",
        "Before Picker -> Year: $selectedYear, Month: $selectedMonth, Day: $selectedDay",
    )

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
                val formattedDate = "$selectedYear-$selectedMonth-$selectedDay"
                onUpdateBirthDate(formattedDate)
                onUpdateCalendarType(selectedLunar)
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
            initialLunar = selectedLunar,
            initialYear = selectedYear,
            initialMonth = selectedMonth,
            initialDay = selectedDay,
        ) { lunar, year, month, day ->
            selectedLunar = lunar
            selectedYear = year.toString()
            selectedMonth = month.toString().padStart(2, '0')
            selectedDay = day.toString().padStart(2, '0')
            Log.d(
                "EditBirthdayScreen",
                "Picker updated -> Lunar: $selectedLunar, Year: $selectedYear, Month: $selectedMonth, Day: $selectedDay",
            )
        }
    }
    Log.d(
        "EditBirthdayScreen",
        "After Picker -> Year: $selectedYear, Month: $selectedMonth, Day: $selectedDay",
    )

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
        onUpdateBirthDate = {},
        onUpdateCalendarType = {},
        initialYear = "2000",
        initialMonth = "01",
        initialDay = "01",
    )
}
