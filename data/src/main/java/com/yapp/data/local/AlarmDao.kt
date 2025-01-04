package com.yapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface AlarmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarm: AlarmEntity): Long

    @Update
    suspend fun updateAlarm(alarm: AlarmEntity): Long

    @Query("SELECT * FROM ${AlarmDatabase.DATABASE_NAME} WHERE id = :id")
    suspend fun getAlarm(id: Long): AlarmEntity?

    @Query("SELECT * FROM ${AlarmDatabase.DATABASE_NAME} ORDER BY id LIMIT :limit OFFSET :offset")
    suspend fun getAlarms(limit: Int, offset: Int): List<AlarmEntity>

    @Query("SELECT COUNT(*) FROM ${AlarmDatabase.DATABASE_NAME}")
    suspend fun getAlarmCount(): Int

    @Query("DELETE FROM ${AlarmDatabase.DATABASE_NAME} WHERE id = :id")
    suspend fun deleteAlarm(id: Long): Long?
}
