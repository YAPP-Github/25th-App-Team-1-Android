package com.yapp.datastore.token

import androidx.datastore.core.Serializer
import com.yapp.common.security.CryptoManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class TokenDataSerializer @Inject constructor(
    private val cryptoManager: CryptoManager,
) : Serializer<AuthToken> {

    private val securityKeyAlias = "data-store"

    override val defaultValue: AuthToken
        get() = AuthToken()

    override suspend fun readFrom(input: InputStream): AuthToken {
        val encryptedDataWithIv = input.readBytes()

        if (encryptedDataWithIv.size < 12) return defaultValue

        val (iv, encryptedData) = encryptedDataWithIv.splitIvAndData()
        return runCatching {
            val decryptedBytes = cryptoManager.decryptData(securityKeyAlias, encryptedData, iv)
            Json.decodeFromString(AuthToken.serializer(), decryptedBytes.decodeToString())
        }.getOrElse {
            it.printStackTrace()
            defaultValue // 복호화 실패 시 defaultValue
        }
    }

    override suspend fun writeTo(t: AuthToken, output: OutputStream) {
        val encryptedResult = cryptoManager.encryptData(
            securityKeyAlias,
            Json.encodeToString(AuthToken.serializer(), t),
        )
        withContext(Dispatchers.IO) {
            output.write(encryptedResult.toCombinedByteArray())
        }
    }

    private fun ByteArray.splitIvAndData(): Pair<ByteArray, ByteArray> {
        val iv = this.copyOfRange(0, 12)
        val encryptedData = this.copyOfRange(12, this.size)
        return iv to encryptedData
    }

    private fun Pair<ByteArray, ByteArray>.toCombinedByteArray(): ByteArray {
        return second + first
    }
}
