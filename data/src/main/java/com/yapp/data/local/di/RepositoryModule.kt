package com.yapp.data.local.di

import com.yapp.data.local.repositoryimpl.AlarmRepositoryImpl
import com.yapp.domain.repository.AlarmRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindsAlarmRepository(
        alarmRepository: AlarmRepositoryImpl,
    ): AlarmRepository
}
