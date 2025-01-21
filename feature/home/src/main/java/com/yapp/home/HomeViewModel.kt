package com.yapp.home

import com.yapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel<HomeContract.State, HomeContract.SideEffect>(
    initialState = HomeContract.State(),
) {
    fun processAction(action: HomeContract.Action) {
        when (action) {
            else -> { }
        }
    }

    private fun navigateBack() {
        emitSideEffect(HomeContract.SideEffect.NavigateBack)
    }
}
