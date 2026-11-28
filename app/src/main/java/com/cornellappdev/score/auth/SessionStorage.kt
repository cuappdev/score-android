package com.cornellappdev.score.auth

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.AtomicFile
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileNotFoundException
import java.security.KeyStore
import javax.crypto.AEADBadTagException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

interface SessionStorage {
    suspend fun read(): AuthSession?

    suspend fun save(session: AuthSession)

    suspend fun clear()
}

class EncryptedSessionStorage @Inject constructor(@ApplicationContext context: Context) :
    SessionStorage {
    private val file = AtomicFile(File(context.noBackupFilesDir, "auth_session"))

    override suspend fun read(): AuthSession? =
        withContext(Dispatchers.IO) {
            val bytes =
                try {
                    file.readFully()
                } catch (_: FileNotFoundException) {
                    return@withContext null
                }

            if (bytes.size < IV_SIZE + GCM_TAG_SIZE) {
                file.delete()
                return@withContext null
            }

            // Reading must not create a replacement key: it could never decrypt this saved session.
            val key = existingEncryptionKey()
            if (key == null) {
                file.delete()
                return@withContext null
            }

            val data =
                try {
                    val cipher = Cipher.getInstance("AES/GCM/NoPadding")
                    cipher.init(
                        Cipher.DECRYPT_MODE,
                        key,
                        GCMParameterSpec(GCM_TAG_SIZE * 8, bytes.copyOfRange(0, IV_SIZE)),
                    )
                    cipher.doFinal(bytes.copyOfRange(IV_SIZE, bytes.size))
                } catch (_: AEADBadTagException) {
                    // The current key failed AES-GCM authentication for these saved bytes.
                    file.delete()
                    return@withContext null
                }

            try {
                Json.decodeFromString<AuthSession>(String(data, Charsets.UTF_8))
            } catch (_: SerializationException) {
                file.delete()
                null
            }
        }

    override suspend fun save(session: AuthSession) =
        withContext(Dispatchers.IO) {
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.ENCRYPT_MODE, encryptionKey())
            val encrypted =
                cipher.iv + cipher.doFinal(Json.encodeToString(session).toByteArray(Charsets.UTF_8))
            val output = file.startWrite()
            try {
                output.write(encrypted)
                file.finishWrite(output)
            } catch (error: Exception) {
                file.failWrite(output)
                throw error
            }
        }

    override suspend fun clear() = withContext(Dispatchers.IO) { file.delete() }

    private fun existingEncryptionKey(): SecretKey? {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        return keyStore.getKey(KEY_ALIAS, null) as? SecretKey
    }

    private fun encryptionKey(): SecretKey {
        existingEncryptionKey()?.let { return it }
        return KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            .apply {
                init(
                    KeyGenParameterSpec.Builder(
                            KEY_ALIAS,
                            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
                        )
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .build()
                )
            }
            .generateKey()
    }

    private companion object {
        const val IV_SIZE = 12
        const val GCM_TAG_SIZE = 16
        const val KEY_ALIAS = "score_auth_session"
    }
}
