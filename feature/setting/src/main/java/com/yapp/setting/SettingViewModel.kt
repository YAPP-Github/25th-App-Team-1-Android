package com.yapp.setting

import com.yapp.common.navigation.destination.SettingDestination
import com.yapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor() :
    BaseViewModel<SettingContract.State, SettingContract.SideEffect>(
        SettingContract.State(),
    ) {
    fun onAction(action: SettingContract.Action) = intent {
        when (action) {
            is SettingContract.Action.UpdateName -> updateState {
                val isValid = SettingContract.FieldType.NAME.validationRegex.matches(action.name)
                copy(name = action.name, isNameValid = isValid)
            }

            is SettingContract.Action.UpdateBirthDate -> updateState { copy(birthDate = action.birthDate) }
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
//            is SettingContract.Action.Submit -> {}
            SettingContract.Action.ShowDialog -> updateState { copy(isDialogVisible = true) }
            SettingContract.Action.HideDialog -> updateState { copy(isDialogVisible = false) }
            SettingContract.Action.PreviousStep -> emitSideEffect(SettingContract.SideEffect.NavigateBack)
            SettingContract.Action.NavigateToEditProfile -> navigateToEditProfile()
            SettingContract.Action.NavigateToEditBirthday -> navigateToEditBirthday()
        }
    }

    private fun navigateToEditProfile() = intent {
        emitSideEffect(SettingContract.SideEffect.Navigate(SettingDestination.EditProfile.route))
    }
    private fun navigateToEditBirthday() = intent {
        emitSideEffect(SettingContract.SideEffect.Navigate(SettingDestination.EditBirthday.route))
    }
}
