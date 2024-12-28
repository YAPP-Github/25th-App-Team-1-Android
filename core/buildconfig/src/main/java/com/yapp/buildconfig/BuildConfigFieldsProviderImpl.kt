package com.yapp.buildconfig

import com.yapp.common.buildconfig.BuildConfigFieldProvider
import com.yapp.common.buildconfig.BuildConfigFields
import core.buildconfig.BuildConfig.BASE_URL
import core.buildconfig.BuildConfig.DEBUG
import javax.inject.Inject

class BuildConfigFieldsProviderImpl @Inject constructor() : BuildConfigFieldProvider {
    override fun get(): BuildConfigFields =
        BuildConfigFields(
            baseUrl = BASE_URL,
            isDebug = DEBUG,
        )
}
