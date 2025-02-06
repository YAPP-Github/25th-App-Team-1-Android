package com.yapp.fortune.page

import com.yapp.domain.model.fortune.Fortune

data class FortunePageData(
    val title: String,
    val description: String,
    val backgroundResId: Int,
    val details: List<Fortune>? = null,
    val isCodyPage: Boolean = false,
    val isLuckyColorPage: Boolean = false,
)
