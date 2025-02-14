package com.yapp.data.local.datasource

interface ImageLocalDataSource {
    suspend fun saveImage(byteArray: ByteArray, fileName: String = "fortune_${System.currentTimeMillis()}.png"): Boolean
}
