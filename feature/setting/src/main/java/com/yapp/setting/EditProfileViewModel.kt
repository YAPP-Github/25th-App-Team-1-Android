package com.yapp.setting

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.yapp.common.navigation.destination.SettingDestination
import com.yapp.datastore.UserPreferences
import com.yapp.domain.model.EditUser
import com.yapp.domain.repository.UserInfoRepository
import com.yapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
    private val userPreferences: UserPreferences,
) : BaseViewModel<SettingContract.State, SettingContract.SideEffect>(
    SettingContract.State(),
) {

    init {
        viewModelScope.launch {
            userPreferences.userIdFlow.collect { userId ->
                if (userId != null) {
                    fetchUserInfo(userId)
                }
            }
        }
    }

    fun onAction(action: SettingContract.Action) = intent {
        when (action) {
            is SettingContract.Action.UpdateName -> updateState {
                val isValid = SettingContract.FieldType.NAME.validationRegex.matches(action.name)
                copy(name = action.name, isNameValid = isValid)
            }

            is SettingContract.Action.UpdateBirthDate -> {
                val formattedBirthDate = formatBirthDate(state.calendarType, action.birthDate)
                Log.d("EditProfileViewModel", "Received new birthDate: ${action.birthDate}")
                reduce { state.copy(birthDate = formattedBirthDate) }
            }

            is SettingContract.Action.UpdateCalendarType -> {
                Log.d("EditProfileViewModel", "Received new calendarType: ${action.calendarType}")
                reduce { state.copy(calendarType = action.calendarType) }
            }

            is SettingContract.Action.UpdateGender -> updateState { copy(selectedGender = action.gender) }
            is SettingContract.Action.ToggleGender -> updateState {
                copy(
                    isMaleSelected = action.isMale,
                    isFemaleSelected = !action.isMale,
                )
            }

            is SettingContract.Action.ToggleTimeUnknown -> updateState {
                copy(
                    isTimeUnknown = action.isChecked,
                    timeOfBirth = if (action.isChecked) "시간모름" else "",
                )
            }

            is SettingContract.Action.UpdateTimeOfBirth -> updateState {
                val isValid = if (action.time.length == 5) {
                    SettingContract.FieldType.TIME.validationRegex.matches(action.time)
                } else {
                    true
                }
                copy(timeOfBirth = action.time, isTimeValid = isValid)
            }

            is SettingContract.Action.ConfirmAndNavigateBack -> {
                emitSideEffect(SettingContract.SideEffect.NavigateBack)
            }

            is SettingContract.Action.Reset -> updateState { SettingContract.State() }
            SettingContract.Action.ShowDialog -> updateState { copy(isDialogVisible = true) }
            SettingContract.Action.HideDialog -> updateState { copy(isDialogVisible = false) }
            SettingContract.Action.PreviousStep -> emitSideEffect(SettingContract.SideEffect.NavigateBack)
            SettingContract.Action.SubmitUserInfo -> submitUserInfo()
            SettingContract.Action.NavigateToEditBirthday -> navigateToEditBirthday()
            else -> {}
        }
    }

    private fun fetchUserInfo(userId: Long) {
        viewModelScope.launch {
            userInfoRepository.getUserInfo(userId)
                .onSuccess { user ->
                    val formattedBirthDate = formatBirthDate(user.calendarType, user.birthDate)

                    Log.d(
                        "EditProfileViewModel",
                        "Fetched user data -> birthDate: ${user.birthDate}, formattedBirthDate: $formattedBirthDate",
                    )

                    updateState {
                        copy(
                            name = user.name,
                            birthDate = formattedBirthDate,
                            selectedGender = user.gender,
                            timeOfBirth = user.birthTime ?: "99:99",
                            isTimeUnknown = user.birthTime == "시간모름",
                            isMaleSelected = user.gender == "남성",
                            isFemaleSelected = user.gender == "여성",
                        )
                    }
                }
                .onFailure { error ->
                    Log.e("EditProfileViewModel", "사용자 정보 가져오기 실패: ${error.message}")
                }
        }
    }

    private fun submitUserInfo() = viewModelScope.launch {
        val userId = userPreferences.userIdFlow.firstOrNull() ?: return@launch
        val state = container.stateFlow.value

        val updatedUser = EditUser(
            name = state.name,
            calendarType = state.calendarType,
            birthDate = extractBirthDate(state.birthDate),
            birthTime = if (state.isTimeUnknown) null else state.timeOfBirth,
            gender = state.selectedGender ?: "남성",
        )

        val result = userInfoRepository.updateUserInfo(userId, updatedUser)

        if (result.isSuccess) {
            Log.d("EditProfileViewModel", "사용자 정보 수정 성공")
            emitSideEffect(SettingContract.SideEffect.NavigateBack)
        } else {
            Log.e("EditProfileViewModel", "사용자 정보 수정 실패")
        }
    }

    private fun formatBirthDate(calendarType: String, birthDate: String): String {
        if (birthDate.contains("년") && birthDate.contains("월") && birthDate.contains("일")) {
            return birthDate
        }
        return try {
            val (year, month, day) = birthDate.split("-").map { it.toInt() }
            val type = when (calendarType.uppercase()) {
                "SOLAR" -> "양력"
                "LUNAR" -> "음력"
                else -> "양력"
            }
            "$type ${year}년 ${month}월 ${day}일"
        } catch (e: Exception) {
            "양력 2000년 1월 1일"
        }
    }

    private fun extractBirthDate(formattedDate: String): String {
        return formattedDate.replace(Regex("[^0-9-]"), "")
    }

    private fun navigateToEditBirthday() = intent {
        emitSideEffect(SettingContract.SideEffect.Navigate(SettingDestination.EditBirthday.route))
    }
}
