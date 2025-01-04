package com.yapp.domain.repository

import com.yapp.domain.model.Alarm

interface AlarmRepository {
    suspend fun getPagedAlarms(limit: Int, offset: Int): List<Alarm>
    suspend fun getAlarmCount(): Int
    suspend fun insertAlarm(alarm: Alarm): Alarm?
    suspend fun updateAlarm(alarm: Alarm): Alarm?
    suspend fun getAlarm(id: Long): Alarm?
    suspend fun deleteAlarm(id: Long): Long?
}
