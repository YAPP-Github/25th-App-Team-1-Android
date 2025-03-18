package com.yapp.analytics.di

import android.content.Context
import com.amplitude.android.Amplitude
import com.amplitude.android.Configuration
import com.yapp.analytics.AmplitudeAnalyticsHelper
import com.yapp.analytics.AnalyticsHelper
import com.yapp.analytics.BuildConfig
import com.yapp.analytics.DebugAnalyticsHelper
import com.yapp.common.buildconfig.BuildConfigFieldProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {

    @Provides
    @Singleton
    fun providesAmplitude(
        @ApplicationContext context: Context,
        buildConfigFieldProvider: BuildConfigFieldProvider,
    ): Amplitude = Amplitude(
        Configuration(
            apiKey = buildConfigFieldProvider.get().amplitudeApiKey,
            context = context,
        ),
    )

    @Provides
    @Singleton
    @Debug
    fun provideDebugAnalyticsHelper(): AnalyticsHelper = DebugAnalyticsHelper()

    @Provides
    @Singleton
    @Release
    fun provideReleaseAnalyticsHelper(amplitude: Amplitude): AnalyticsHelper = AmplitudeAnalyticsHelper(amplitude)

    @Provides
    @Singleton
    fun provideAnalyticsHelper(
        @Debug debugAnalyticsHelper: AnalyticsHelper,
        @Release releaseAnalyticsHelper: AnalyticsHelper,
    ): AnalyticsHelper = if (BuildConfig.DEBUG) debugAnalyticsHelper else releaseAnalyticsHelper
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Debug

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Release
