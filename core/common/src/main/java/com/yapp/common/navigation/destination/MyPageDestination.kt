package com.yapp.common.navigation.destination

import com.yapp.common.navigation.Routes

sealed class MyPageDestination(val route: String) {
    data object Route : MyPageDestination(Routes.MyPage.ROUTE)
    data object MyPage : MyPageDestination(Routes.MyPage.MYPAGE)
}
