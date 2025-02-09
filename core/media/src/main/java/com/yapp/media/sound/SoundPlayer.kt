package com.yapp.media.sound

import android.content.Context
import android.media.AudioAttributes
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
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build(),
                )
                prepare()
            } catch (e: Exception) {
                Log.e("SoundPlayer", "Error initializing MediaPlayer", e)
            }
        }
    }

    fun playSound(volume: Int) {
        mediaPlayer?.let {
            if (!it.isPlaying) {
                updateVolume(volume)
                it.start()
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
        val clampedVolume = volume.coerceIn(0, 100)
        val normalizedVolume = clampedVolume / 100f

        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
            }
            it.setVolume(normalizedVolume, normalizedVolume)
        } ?: Log.e("SoundPlayer", "MediaPlayer is not initialized")
    }

    fun release() {
        stopSound()
        currentUri = null
    }
}
