package com.yapp.data.local.di

import android.content.ContentResolver
import android.content.Context
import com.yapp.data.local.datasource.ImageLocalDataSource
import com.yapp.data.local.datasource.ImageLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MediaModule {

    @Provides
    @Singleton
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver {
        return context.contentResolver
    }

    @Provides
    @Singleton
    fun provideImageLocalDataSource(contentResolver: ContentResolver): ImageLocalDataSource {
        return ImageLocalDataSourceImpl(contentResolver)
    }
}
