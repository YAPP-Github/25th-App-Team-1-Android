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
    private var mediaPlayer: MediaPlayer = MediaPlayer()

    fun playSound(uri: Uri, volume: Float) {
        stopSound()

        try {
            mediaPlayer.setDataSource(context, uri)
            mediaPlayer.setOnPreparedListener { mediaPlayer.start() }
            mediaPlayer.prepareAsync()
            mediaPlayer.setVolume(volume, volume)
        } catch (e: Exception) {
            Log.e("SoundPlayer", "Error playing sound", e)
            stopSound()
        }
    }

    fun stopSound() {
        try {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
            mediaPlayer.reset()
        } catch (e: Exception) {
            Log.e("SoundPlayer", "Error stopping sound", e)
        }
    }

    fun updateVolume(volume: Int) {
        val normalizedVolume = (volume / 100f).coerceIn(0f, 1f)
        mediaPlayer.setVolume(normalizedVolume, normalizedVolume)
    }

    fun release() {
        try {
            mediaPlayer.release()
        } catch (e: Exception) {
            Log.e("SoundPlayer", "Error releasing mediaPlayer", e)
        }
    }
}
