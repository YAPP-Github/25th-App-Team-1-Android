package com.yapp.fortune

// 더미 추후 제거
data class CodyItem(val resId: Int, val categoryLabel: String, val clothsNameLabel: String)

fun getCodyList(): List<CodyItem> = listOf(
    CodyItem(core.designsystem.R.drawable.ic_top, "상의", "베이지색 니트"),
    CodyItem(core.designsystem.R.drawable.ic_pants, "하의", "청색 데님 팬츠"),
    CodyItem(core.designsystem.R.drawable.ic_shoes, "신발", "흰색 스니커즈"),
    CodyItem(core.designsystem.R.drawable.ic_glove, "액세서리", "은색 목걸이"),
)
