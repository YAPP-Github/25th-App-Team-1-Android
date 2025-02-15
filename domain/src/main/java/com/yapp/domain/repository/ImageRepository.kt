package com.yapp.domain.repository

interface ImageRepository {
    suspend fun saveImage(byteArray: ByteArray): Boolean
}
