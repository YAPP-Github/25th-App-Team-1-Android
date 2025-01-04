package com.yapp.data.local.datasource

import com.yapp.data.local.AlarmDao
import com.yapp.data.local.AlarmEntity
import com.yapp.data.local.toDomain
import com.yapp.domain.model.Alarm
import javax.inject.Inject

class AlarmLocalDataSourceImpl @Inject constructor(
    private val alarmDao: AlarmDao,
) : AlarmLocalDataSource {
    override suspend fun getPagedAlarms(
        limit: Int,
        offset: Int,
    ): List<Alarm> {
        return alarmDao.getAlarms(limit, offset).map {
            it.toDomain()
        }
    }

    override suspend fun getAlarmCount(): Int {
        return alarmDao.getAlarmCount()
    }

    override suspend fun insertAlarm(alarm: AlarmEntity): Long {
        return alarmDao.insertAlarm(alarm)
    }

    override suspend fun updateAlarm(alarm: AlarmEntity): Long {
        return alarmDao.updateAlarm(alarm)
    }

    override suspend fun getAlarm(id: Long): Alarm? {
        return alarmDao.getAlarm(id)?.toDomain()
    }

    override suspend fun deleteAlarm(id: Long): Long? {
        return alarmDao.deleteAlarm(id)
    }
}
