package com.yapp.data.local.di

import com.yapp.data.local.datasource.AlarmLocalDataSource
import com.yapp.data.local.datasource.AlarmLocalDataSourceImpl
import com.yapp.data.remote.datasource.DummyDataSource
import com.yapp.data.remote.datasource.DummyDataSourceImpl
import com.yapp.domain.model.Alarm
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindsAlarmDataSource(
        alarmLocalDataSource: AlarmLocalDataSourceImpl,
    ): AlarmLocalDataSource
}
