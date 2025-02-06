package com.yapp.data.local.datasource

import com.yapp.data.local.AlarmEntity
import com.yapp.domain.model.Alarm
import kotlinx.coroutines.flow.Flow

interface AlarmLocalDataSource {
    fun getAllAlarms(): Flow<List<Alarm>>
    fun getPagedAlarms(limit: Int, offset: Int): Flow<List<Alarm>>
    fun getAlarmsByTime(hour: Int, minute: Int, isAm: Boolean): Flow<List<Alarm>>
    fun getAlarmCount(): Flow<Int>
    suspend fun insertAlarm(alarm: AlarmEntity): Long
    suspend fun updateAlarm(alarm: AlarmEntity): Int
    suspend fun getAlarm(id: Long): Alarm?
    suspend fun deleteAlarm(id: Long): Int
}
