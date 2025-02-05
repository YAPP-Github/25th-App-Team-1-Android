package com.yapp.data.local.datasource

import com.yapp.data.local.AlarmEntity
import com.yapp.domain.model.Alarm

interface AlarmLocalDataSource {
    suspend fun getPagedAlarms(limit: Int, offset: Int): List<Alarm>
    suspend fun getAlarmsByTime(hour: Int, minute: Int): List<Alarm>
    suspend fun getAlarmCount(): Int
    suspend fun insertAlarm(alarm: AlarmEntity): Long
    suspend fun updateAlarm(alarm: AlarmEntity): Int
    suspend fun getAlarm(id: Long): Alarm?
    suspend fun deleteAlarm(id: Long): Int
}
