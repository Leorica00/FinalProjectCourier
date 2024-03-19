package com.example.final_project.presentation.util

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class EncryptionHelper {
    private val key: SecretKey
    private val encryptCipher: Cipher
    private val decryptCipher: Cipher

    init {
        val keyBytes = "UniqueKey".toByteArray(Charsets.UTF_8)
        val paddedKeyBytes = if (keyBytes.size >= 16) {
            keyBytes.sliceArray(0 until 16)
        } else {
            keyBytes + ByteArray(16 - keyBytes.size)
        }
        key = SecretKeySpec(paddedKeyBytes, "AES")

        encryptCipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
        decryptCipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
    }

    fun encrypt(data: String): String {
        val iv = ByteArray(encryptCipher.blockSize)
        val secureRandom = SecureRandom()
        secureRandom.nextBytes(iv)

        val ivParams = IvParameterSpec(iv)
        encryptCipher.init(Cipher.ENCRYPT_MODE, key, ivParams)

        val encryptedBytes = encryptCipher.doFinal(data.toByteArray(Charsets.UTF_8))

        val combinedByteArray = ByteArray(iv.size + encryptedBytes.size)
        System.arraycopy(iv, 0, combinedByteArray, 0, iv.size)
        System.arraycopy(encryptedBytes, 0, combinedByteArray, iv.size, encryptedBytes.size)

        return Base64.encodeToString(combinedByteArray, Base64.DEFAULT)
    }

    fun decrypt(encryptedData: String): String {
        val combinedByteArray = Base64.decode(encryptedData, Base64.DEFAULT)

        val ivSize = encryptCipher.blockSize
        val iv = combinedByteArray.sliceArray(0 until ivSize)
        val encryptedBytes = combinedByteArray.sliceArray(ivSize until combinedByteArray.size)

        val ivParams = IvParameterSpec(iv)
        decryptCipher.init(Cipher.DECRYPT_MODE, key, ivParams)

        val decryptedBytes = decryptCipher.doFinal(encryptedBytes)

        return String(decryptedBytes, Charsets.UTF_8)
    }
}