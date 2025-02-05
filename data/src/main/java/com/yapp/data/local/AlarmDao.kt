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
    suspend fun updateAlarm(alarm: AlarmEntity): Int

    @Query("SELECT * FROM ${AlarmDatabase.DATABASE_NAME} WHERE id = :id")
    suspend fun getAlarm(id: Long): AlarmEntity?

    @Query("SELECT * FROM ${AlarmDatabase.DATABASE_NAME} ORDER BY isAm DESC, hour ASC, minute ASC LIMIT :limit OFFSET :offset")
    suspend fun getPagedAlarms(limit: Int, offset: Int): List<AlarmEntity>

    @Query("SELECT * FROM ${AlarmDatabase.DATABASE_NAME} WHERE hour = :hour AND minute = :minute")
    suspend fun getAlarmsByTime(hour: Int, minute: Int): List<AlarmEntity>

    @Query("SELECT COUNT(*) FROM ${AlarmDatabase.DATABASE_NAME}")
    suspend fun getAlarmCount(): Int

    @Query("DELETE FROM ${AlarmDatabase.DATABASE_NAME} WHERE id = :id")
    suspend fun deleteAlarm(id: Long): Int
}
