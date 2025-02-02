package com.yapp.data.local.di

import android.content.Context
import androidx.room.Room
import com.yapp.data.local.AlarmDao
import com.yapp.data.local.AlarmDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun providesAlarmDatabase(
        @ApplicationContext context: Context,
    ): AlarmDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AlarmDatabase::class.java,
            AlarmDatabase.DATABASE_NAME,
        ).build()
    }

    @Provides
    @Singleton
    fun providesAlarmDao(
        database: AlarmDatabase,
    ): AlarmDao {
        return database.alarmDao()
    }
}
