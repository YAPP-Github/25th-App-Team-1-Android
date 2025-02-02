package com.yapp.data.local.repositoryimpl

import com.yapp.data.local.datasource.AlarmLocalDataSource
import com.yapp.data.local.toEntity
import com.yapp.domain.model.Alarm
import com.yapp.domain.model.AlarmSound
import com.yapp.domain.repository.AlarmRepository
import com.yapp.media.ringtone.RingtoneManagerHelper
import com.yapp.media.sound.SoundPlayer
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val alarmLocalDataSource: AlarmLocalDataSource,
    private val ringtoneManagerHelper: RingtoneManagerHelper,
    private val soundPlayer: SoundPlayer,
) : AlarmRepository {
    override suspend fun getAlarmSounds(): Result<List<AlarmSound>> = runCatching {
        ringtoneManagerHelper.getAlarmSounds().map { (title, uri) ->
            AlarmSound(title, uri)
        }
    }

    override fun playAlarmSound(alarmSound: AlarmSound, volume: Int) {
        soundPlayer.playSound(alarmSound.uri, volume / 100f)
    }

    override fun stopAlarmSound() {
        soundPlayer.stopSound()
    }

    override fun updateAlarmVolume(volume: Int) {
        soundPlayer.updateVolume(volume)
    }

    override fun releaseSoundPlayer() {
        soundPlayer.release()
    }

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
        val updatedRows = alarmLocalDataSource.updateAlarm(alarm.toEntity())
        if (updatedRows > 0) {
            alarmLocalDataSource.getAlarm(alarm.id)
                ?: throw Exception("Failed to fetch updated alarm")
        } else {
            throw Exception("No rows updated")
        }
    }

    override suspend fun getAlarm(id: Long): Result<Alarm> = runCatching {
        alarmLocalDataSource.getAlarm(id)
            ?: return Result.failure(Exception("Failed to get alarm"))
    }.onFailure {
        return Result.failure(Exception("Failed to get alarm"))
    }

    override suspend fun deleteAlarm(id: Long): Result<Unit> = runCatching {
        val deletedRows = alarmLocalDataSource.deleteAlarm(id)
        if (deletedRows > 0) {
            Unit
        } else {
            throw Exception("No rows deleted")
        }
    }
}
