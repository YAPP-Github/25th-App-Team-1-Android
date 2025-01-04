package com.yapp.data.remote.di

import com.yapp.data.remote.datasource.DummyDataSource
import com.yapp.data.remote.datasource.DummyDataSourceImpl
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
    abstract fun bindsDummyDataSource(
        dummyDataSource: DummyDataSourceImpl,
    ): DummyDataSource
}
