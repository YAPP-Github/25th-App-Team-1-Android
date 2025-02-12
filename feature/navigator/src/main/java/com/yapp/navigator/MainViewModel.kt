package com.yapp.navigator

import androidx.lifecycle.viewModelScope
import com.yapp.datastore.UserPreferences
import com.yapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
) : BaseViewModel<MainContract.State, MainContract.SideEffect>(
    initialState = MainContract.State(isLoading = true),
) {
    init {
        viewModelScope.launch {
            combine(
                userPreferences.userIdFlow,
                userPreferences.onboardingCompletedFlow,
            ) { userId, onboardingCompleted ->
                Pair(userId, onboardingCompleted)
            }.collect { (userId, onboardingCompleted) ->
                updateState {
                    copy(
                        userId = userId,
                        onboardingCompleted = onboardingCompleted,
                        isLoading = false,
                    )
                }
            }
        }
    }
}
