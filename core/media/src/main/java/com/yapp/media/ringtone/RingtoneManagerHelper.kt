package com.yapp.media.ringtone

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RingtoneManagerHelper @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun getAlarmSounds(): List<Pair<String, Uri>> {
        val ringtoneManager = RingtoneManager(context).apply {
            setType(RingtoneManager.TYPE_ALARM)
        }
        val cursor = ringtoneManager.cursor
        val sounds = mutableListOf<Pair<String, Uri>>()

        cursor.use {
            while (it.moveToNext()) {
                val title = it.getString(RingtoneManager.TITLE_COLUMN_INDEX)
                val uri = ringtoneManager.getRingtoneUri(it.position)
                sounds.add(title to uri)
            }
        }
        return sounds
    }
}
