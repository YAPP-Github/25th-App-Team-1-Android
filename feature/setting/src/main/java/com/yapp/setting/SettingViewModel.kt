package com.yapp.setting

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.yapp.common.navigation.destination.SettingDestination
import com.yapp.datastore.UserPreferences
import com.yapp.domain.repository.UserInfoRepository
import com.yapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
    private val userPreferences: UserPreferences,
) :
    BaseViewModel<SettingContract.State, SettingContract.SideEffect>(
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
                val formattedDate = "${action.year}-${action.month.toString().padStart(2, '0')}-${action.day.toString().padStart(2, '0')}"
                updateState { state.copy(birthDate = formattedDate) }
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
            is SettingContract.Action.Reset -> updateState { SettingContract.State() }
            SettingContract.Action.ShowDialog -> updateState { copy(isDialogVisible = true) }
            SettingContract.Action.HideDialog -> updateState { copy(isDialogVisible = false) }
            SettingContract.Action.PreviousStep -> emitSideEffect(SettingContract.SideEffect.NavigateBack)
            SettingContract.Action.NavigateToEditProfile -> navigateToEditProfile()
            is SettingContract.Action.OpenWebView -> openWebView(action.url)
            else -> {}
        }
    }

    private fun fetchUserInfo(userId: Long) {
        viewModelScope.launch {
            userInfoRepository.getUserInfo(userId)
                .onSuccess { user ->
                    updateState {
                        copy(
                            name = user.name,
                            birthDate = user.birthDate,
                            selectedGender = user.gender,
                        )
                    }
                }
                .onFailure { error ->
                    Log.e("SettingViewModel", "사용자 정보 가져오기 실패: ${error.message}")
                }
        }
    }

    private fun navigateToEditProfile() = intent {
        emitSideEffect(SettingContract.SideEffect.Navigate(SettingDestination.EditProfile.route))
    }

    private fun openWebView(url: String) {
        emitSideEffect(SettingContract.SideEffect.OpenWebView(url))
    }
}
