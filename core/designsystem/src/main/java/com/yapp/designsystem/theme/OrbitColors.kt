package com.yapp.designsystem.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

class OrbitColors(
    main: Color = Color(0xFFFEFF65),
    sub_main: Color = Color(0xFFFDFE90),
    white: Color = Color(0xFFFFFFFF),
    gray_50: Color = Color(0xFFE6EDF8),
    gray_100: Color = Color(0xFFD7E1EE),
    gray_200: Color = Color(0xFFC8D3E3),
    gray_300: Color = Color(0xFFA5B2C5),
    gray_400: Color = Color(0xFF7B8696),
    gray_500: Color = Color(0xFF5D6470),
    gray_600: Color = Color(0xFF3D424B),
    gray_700: Color = Color(0xFF2A2F38),
    gray_800: Color = Color(0xFF1F2127),
    gray_900: Color = Color(0xFF17191F),
    alert: Color = Color(0xFFF2544A),
    alert_pressed: Color = Color(0xFFE53D33),
    success: Color = Color(0xFF22C55E)
) {
    var main by mutableStateOf(main)
        private set
    var sub_main by mutableStateOf(sub_main)
        private set
    var white by mutableStateOf(white)
        private set
    var gray_50 by mutableStateOf(gray_50)
        private set
    var gray_100 by mutableStateOf(gray_100)
        private set
    var gray_200 by mutableStateOf(gray_200)
        private set
    var gray_300 by mutableStateOf(gray_300)
        private set
    var gray_400 by mutableStateOf(gray_400)
        private set
    var gray_500 by mutableStateOf(gray_500)
        private set
    var gray_600 by mutableStateOf(gray_600)
        private set
    var gray_700 by mutableStateOf(gray_700)
        private set
    var gray_800 by mutableStateOf(gray_800)
        private set
    var gray_900 by mutableStateOf(gray_900)
        private set
    var alert by mutableStateOf(alert)
        private set
    var alert_pressed by mutableStateOf(alert_pressed)
        private set
    var success by mutableStateOf(success)
        private set

    fun copy(
        main: Color = this.main,
        sub_main: Color = this.sub_main,
        white: Color = this.white,
        gray_50: Color = this.gray_50,
        gray_100: Color = this.gray_100,
        gray_200: Color = this.gray_200,
        gray_300: Color = this.gray_300,
        gray_400: Color = this.gray_400,
        gray_500: Color = this.gray_500,
        gray_600: Color = this.gray_600,
        gray_700: Color = this.gray_700,
        gray_800: Color = this.gray_800,
        gray_900: Color = this.gray_900,
        alert: Color = this.alert,
        alert_pressed: Color = this.alert_pressed,
        success: Color = this.success
    ) = OrbitColors(
        main = main,
        sub_main = sub_main,
        white = white,
        gray_50 = gray_50,
        gray_100 = gray_100,
        gray_200 = gray_200,
        gray_300 = gray_300,
        gray_400 = gray_400,
        gray_500 = gray_500,
        gray_600 = gray_600,
        gray_700 = gray_700,
        gray_800 = gray_800,
        gray_900 = gray_900,
        alert = alert,
        alert_pressed = alert_pressed,
        success = success
    )

    fun updateColorFrom(other: OrbitColors) {
        main = other.main
        sub_main = other.sub_main
        white = other.white
        gray_50 = other.gray_50
        gray_100 = other.gray_100
        gray_200 = other.gray_200
        gray_300 = other.gray_300
        gray_400 = other.gray_400
        gray_500 = other.gray_500
        gray_600 = other.gray_600
        gray_700 = other.gray_700
        gray_800 = other.gray_800
        gray_900 = other.gray_900
        alert = other.alert
        alert_pressed = other.alert_pressed
        success = other.success
    }
}
