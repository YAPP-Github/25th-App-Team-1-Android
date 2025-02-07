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
    private var currentUri: Uri? = null

    fun initialize(uri: Uri) {
        stopSound()
        currentUri = uri

        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(context, uri)
                prepare()
            } catch (e: Exception) {
                Log.e("SoundPlayer", "Error initializing MediaPlayer", e)
            }
        }
    }

    fun playSound(volume: Int) {
        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
                setVolume(volume)
            }
        } ?: Log.e("SoundPlayer", "MediaPlayer is not initialized")
    }

    fun stopSound() {
        mediaPlayer?.apply {
            if (isPlaying) stop()
            reset()
            release()
        }
        mediaPlayer = null
    }

    fun updateVolume(volume: Int) {
        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
            }
            setVolume(volume)
        } ?: Log.e("SoundPlayer", "MediaPlayer is not initialized")
    }

    private fun setVolume(volume: Int) {
        val normalizedVolume = (volume / 100f).coerceIn(0f, 1f)
        mediaPlayer?.setVolume(normalizedVolume, normalizedVolume)
    }

    fun release() {
        stopSound()
        currentUri = null
    }
}
