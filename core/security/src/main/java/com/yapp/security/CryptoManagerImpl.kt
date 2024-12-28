package com.yapp.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties.BLOCK_MODE_GCM
import android.security.keystore.KeyProperties.ENCRYPTION_PADDING_NONE
import android.security.keystore.KeyProperties.KEY_ALGORITHM_AES
import android.security.keystore.KeyProperties.PURPOSE_DECRYPT
import android.security.keystore.KeyProperties.PURPOSE_ENCRYPT
import com.yapp.common.security.CryptoManager
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject

class CryptoManagerImpl @Inject constructor() : CryptoManager {

    private val provider = "AndroidKeyStore"
    private val charset = Charsets.UTF_8

    private val cipher: Cipher by lazy { Cipher.getInstance("AES/GCM/NoPadding") }
    private val keyStore: KeyStore by lazy { KeyStore.getInstance(provider).apply { load(null) } }
    private val keyGenerator: KeyGenerator by lazy { KeyGenerator.getInstance(KEY_ALGORITHM_AES, provider) }

    /**
     * 데이터 암호화.
     * @param keyAlias 키 별칭
     * @param text 암호화할 텍스트
     * @return 암호화된 데이터 + 초기화 벡터(IV)
     */
    override fun encryptData(keyAlias: String, text: String): Pair<ByteArray, ByteArray> {
        val secretKey = getOrCreateSecretKey(keyAlias)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        return cipher.doFinal(text.toByteArray(charset)) to cipher.iv
    }

    /**
     * 데이터를 복호화.
     * @param keyAlias 키 별칭
     * @param encryptedData 암호화된 데이터
     * @param iv 초기화 벡터(IV)
     * @return 복호화된 데이터
     */
    override fun decryptData(keyAlias: String, encryptedData: ByteArray, iv: ByteArray): ByteArray {
        val secretKey = getSecretKey(keyAlias)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(128, iv))
        return cipher.doFinal(encryptedData)
    }

    /**
     * 키가 없으면 생성하고, 이미 존재하면 가져옴.
     * @param keyAlias 키 별칭
     * @return SecretKey
     */
    private fun getOrCreateSecretKey(keyAlias: String): SecretKey =
        keyStore.getSecretKeyOrNull(keyAlias) ?: generateSecretKey(keyAlias)

    /**
     * 새로운 SecretKey를 생성.
     * @param keyAlias 키 별칭
     * @return SecretKey
     */
    private fun generateSecretKey(keyAlias: String): SecretKey {
        val parameterSpec = KeyGenParameterSpec.Builder(keyAlias, PURPOSE_ENCRYPT or PURPOSE_DECRYPT)
            .apply {
                setBlockModes(BLOCK_MODE_GCM)
                setEncryptionPaddings(ENCRYPTION_PADDING_NONE)
            }.build()
        keyGenerator.init(parameterSpec)
        return keyGenerator.generateKey()
    }

    /**
     * KeyStore에서 SecretKey 가져오기.
     * @param keyAlias 키 별칭
     * @return SecretKey
     */
    private fun getSecretKey(keyAlias: String): SecretKey =
        keyStore.getSecretKeyOrNull(keyAlias)
            ?: throw IllegalStateException("SecretKey for alias $keyAlias does not exist")

    /**
     * 키 존재하지 않으면 null 반환.
     */
    private fun KeyStore.getSecretKeyOrNull(keyAlias: String): SecretKey? =
        (getEntry(keyAlias, null) as? KeyStore.SecretKeyEntry)?.secretKey
}
