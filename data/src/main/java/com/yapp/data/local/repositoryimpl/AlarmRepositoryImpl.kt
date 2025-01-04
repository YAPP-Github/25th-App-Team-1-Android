package com.yapp.data.local.repositoryimpl

import com.yapp.data.local.datasource.AlarmLocalDataSource
import com.yapp.data.local.toEntity
import com.yapp.domain.model.Alarm
import com.yapp.domain.repository.AlarmRepository
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val alarmLocalDataSource: AlarmLocalDataSource,
) : AlarmRepository {
    override suspend fun getPagedAlarms(limit: Int, offset: Int): Result<List<Alarm>> = runCatching {
        alarmLocalDataSource.getPagedAlarms(limit, offset)
    }.onFailure {
        return Result.failure(Exception("Failed to get paged alarms"))
    }

    override suspend fun getAlarmCount(): Result<Int> = runCatching {
        alarmLocalDataSource.getAlarmCount()
    }.onFailure {
        return Result.failure(Exception("Failed to get alarm count"))
    }

    override suspend fun insertAlarm(alarm: Alarm): Result<Alarm> = runCatching {
        val alarmId = alarmLocalDataSource.insertAlarm(alarm.toEntity())
        alarmLocalDataSource.getAlarm(alarmId)
            ?: return Result.failure(Exception("Failed to insert alarm"))
    }.onFailure {
        return Result.failure(Exception("Failed to insert alarm"))
    }

    override suspend fun updateAlarm(alarm: Alarm): Result<Alarm> = runCatching {
        val alarmId = alarmLocalDataSource.updateAlarm(alarm.toEntity())
        alarmLocalDataSource.getAlarm(alarmId)
            ?: return Result.failure(Exception("Failed to update alarm"))
    }.onFailure {
        return Result.failure(Exception("Failed to update alarm"))
    }

    override suspend fun getAlarm(id: Long): Result<Alarm> = runCatching {
        alarmLocalDataSource.getAlarm(id)
            ?: return Result.failure(Exception("Failed to get alarm"))
    }.onFailure {
        return Result.failure(Exception("Failed to get alarm"))
    }

    override suspend fun deleteAlarm(id: Long): Result<Long> = runCatching {
        alarmLocalDataSource.deleteAlarm(id)
            ?: return Result.failure(Exception("Failed to delete alarm"))
    }.onFailure {
        return Result.failure(Exception("Failed to delete alarm"))
    }
}
