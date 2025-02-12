package com.yapp.common.navigation.destination

import com.yapp.common.navigation.Routes

sealed class SettingDestination(val route: String) {
    data object Route : SettingDestination(Routes.Setting.ROUTE)
    data object Setting : SettingDestination(Routes.Setting.SETTING)
    data object EditProfile : SettingDestination(Routes.Setting.EDIT_PROFILE)
    data object EditBirthday : SettingDestination(Routes.Setting.EDIT_BIRTHDAY)

    companion object {
        val routes = listOf(Setting, EditProfile, EditBirthday)
    }
}
