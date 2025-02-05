package com.yapp.common.util

import android.content.Context
import androidx.annotation.DrawableRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ResourceProvider @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun getString(resId: Int): String = context.getString(resId)

    @DrawableRes
    fun getDrawable(@DrawableRes resId: Int): Int = resId
}
