package com.yapp.data.remote.di

import com.yapp.data.remote.repositoryimpl.DummyRepositoryImpl
import com.yapp.domain.repository.DummyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DummyRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindsDummyRepository(
        dummyRepository: DummyRepositoryImpl,
    ): DummyRepository
}
