package com.yapp.media.sound

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoundPlayer @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private var mediaPlayer: MediaPlayer? = null

    fun playSound(uri: Uri) {
        stopSound()

        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(context, uri)
                prepareAsync()
                setOnPreparedListener { start() }
            } catch (e: Exception) {
                Log.e("SoundPlayer", "Error playing sound", e)
                stopSound()
            }
        }
    }

    fun stopSound() {
        mediaPlayer?.let {
            try {
                if (it.isPlaying) {
                    it.stop()
                }
                it.reset()
                it.release()
            } catch (e: Exception) {
                Log.e("SoundPlayer", "Error stopping sound", e)
            }
        }
        mediaPlayer = null
    }

    fun updateVolume(volume: Int) {
        val normalizedVolume = (volume / 100f).coerceIn(0f, 1f)
        mediaPlayer?.setVolume(normalizedVolume, normalizedVolume)
    }
}
