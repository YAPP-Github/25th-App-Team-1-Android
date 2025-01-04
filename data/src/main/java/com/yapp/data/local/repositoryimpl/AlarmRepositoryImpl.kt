package com.yapp.data.local.repositoryimpl

import com.yapp.data.local.datasource.AlarmLocalDataSource
import com.yapp.data.local.toEntity
import com.yapp.domain.model.Alarm
import com.yapp.domain.repository.AlarmRepository
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val alarmLocalDataSource: AlarmLocalDataSource
) : AlarmRepository {
    override suspend fun getPagedAlarms(limit: Int, offset: Int): List<Alarm> {
        return alarmLocalDataSource.getPagedAlarms(limit, offset)
    }

    override suspend fun getAlarmCount(): Int {
        return alarmLocalDataSource.getAlarmCount()
    }

    override suspend fun insertAlarm(alarm: Alarm): Alarm? {
        val alarmId = alarmLocalDataSource.insertAlarm(alarm.toEntity())
        return getAlarm(alarmId)
    }

    override suspend fun updateAlarm(alarm: Alarm): Alarm? {
        val alarmId = alarmLocalDataSource.updateAlarm(alarm.toEntity())
        return getAlarm(alarmId)
    }

    override suspend fun getAlarm(id: Long): Alarm? {
        return alarmLocalDataSource.getAlarm(id)
    }

    override suspend fun deleteAlarm(id: Long): Long? {
        return alarmLocalDataSource.deleteAlarm(id)
    }
}
