package com.yapp.fortune

// 더미 추후 제거
data class CodyItem(val resId: Int, val categoryLabel: String, val clothsNameLabel: String)

fun getCodyList(): List<CodyItem> = listOf(
    CodyItem(core.designsystem.R.drawable.ic_top, "상의", "description"),
    CodyItem(core.designsystem.R.drawable.ic_pants, "하의", "description"),
    CodyItem(core.designsystem.R.drawable.ic_shoes, "신발", "description"),
    CodyItem(core.designsystem.R.drawable.ic_glove, "액세서리", "description"),
)
