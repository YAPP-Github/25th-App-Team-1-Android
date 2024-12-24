package com.yapp.data.di

import com.yapp.data.service.DummyService
import com.yapp.network.di.NoneAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DummyServiceModule {
    @Provides
    @Singleton
    fun providesDummyService(@NoneAuth retrofit: Retrofit): DummyService =
        retrofit.create(DummyService::class.java)
}
