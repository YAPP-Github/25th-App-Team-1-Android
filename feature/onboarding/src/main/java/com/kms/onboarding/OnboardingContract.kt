package com.kms.onboarding

import com.yapp.ui.base.UiState

sealed class OnboardingContract {

    data class State(
        val currentStep: Int = 1,
        val textFieldValue: String = "",
        val showWarning: Boolean = false,
        val isButtonEnabled: Boolean = false,
        val selectedGender: String? = null,
        val userName: String = "",
        val birthDate: String = "",
        val birthType: String = "양력",
        val birthTime: String = "",
        val isBirthDateValid: Boolean = false,
        val isBirthTimeValid: Boolean = false,
        val isValid: Boolean = false,
        val isBottomSheetOpen: Boolean = false,
    ) : UiState {
        val birthDateFormatted: String
            get() {
                val parts = birthDate.split("-")
                val year = parts[0] + "년"
                val month = parts[1].toInt().toString() + "월"
                val day = parts[2].toInt().toString() + "일"

                return "$birthType $year $month $day"
            }

        val birthTimeFormatted: String
            get() {
                val parts = birthTime.split(":")
                val hour = parts[0].toInt().toString() + "시" // ✅ 00 → 0시
                val minute = parts[1].toInt().toString() + "분" // ✅ 00 → 0분

                return "$hour $minute"
            }
    }

    sealed class Action {
        data object NextStep : Action()
        data object PreviousStep : Action()
        data class UpdateField(val value: String, val fieldType: FieldType) : Action()
        data object Reset : Action()
        data class Submit(val stepData: Map<String, String>) : Action()
        data class UpdateGender(val gender: String) : Action()
        data class UpdateBirthDate(val lunar: String, val year: Int, val month: Int, val day: Int) : Action() // ✅ 추가
        data object ToggleBottomSheet : Action()
    }

    enum class FieldType(val validationRegex: Regex) {
        TIME(Regex("^(24:00|([0-1]\\d|2[0-3]):[0-5]\\d)\$")),
        NAME(Regex("^(?=.{1,13}\$)(?=.{1,6}(?:[가-힣]|[a-zA-Z]{2})\$)[가-힣a-zA-Z]*\$")),
    }

    sealed class SideEffect : com.yapp.ui.base.SideEffect {
        data class Navigate(
            val route: String,
            val popUpTo: String? = null,
            val inclusive: Boolean = false,
        ) : SideEffect()

        data object NavigateBack : SideEffect()
        data object OnboardingCompleted : SideEffect()
        data object ResetField : SideEffect()
    }
}
