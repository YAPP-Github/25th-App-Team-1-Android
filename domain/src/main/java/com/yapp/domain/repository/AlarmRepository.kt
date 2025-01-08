package com.yapp.domain.repository

import com.yapp.domain.model.Alarm

interface AlarmRepository {
    suspend fun getPagedAlarms(limit: Int, offset: Int): Result<List<Alarm>>
    suspend fun getAlarmCount(): Result<Int>
    suspend fun insertAlarm(alarm: Alarm): Result<Alarm>
    suspend fun updateAlarm(alarm: Alarm): Result<Alarm>
    suspend fun getAlarm(id: Long): Result<Alarm>
    suspend fun deleteAlarm(id: Long): Result<Unit>
}
