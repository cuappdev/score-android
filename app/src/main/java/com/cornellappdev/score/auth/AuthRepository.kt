package com.cornellappdev.score.auth

import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Singleton
class AuthRepository
@Inject
constructor(private val api: AuthApi, private val storage: SessionStorage) {
    private val mutex = Mutex()
    private val mutableState = MutableStateFlow<AuthState>(AuthState.Loading)
    val state = mutableState.asStateFlow()
    private var session: AuthSession? = null
    private var restored = false

    suspend fun restoreSession() {
        var restoredFromStorage = false
        for (attempt in 0 until RESTORE_ATTEMPTS) {
            try {
                mutex.withLock { restoreStoredSession() }
                restoredFromStorage = true
                break
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                if (attempt < RESTORE_ATTEMPTS - 1) {
                    delay((RESTORE_RETRY_DELAY_MILLIS * (attempt + 1)).milliseconds)
                }
            }
        }

        if (!restoredFromStorage) {
            // Leave restored false so later accessToken calls or a future startup can retry.
            mutex.withLock {
                if (!restored) mutableState.value = AuthState.SignedOut
            }
            return
        }

        try {
            accessToken()
        } catch (error: CancellationException) {
            throw error
        } catch (_: Exception) {
            // Network or refresh failures should not prevent a later token request from retrying.
        }
    }

    suspend fun signIn(identity: GoogleIdentity) =
        mutex.withLock {
            require(identity.user.email.endsWith("@cornell.edu", ignoreCase = true))
            val tokens =
                try {
                    api.login(identity.firebaseIdToken)
                } catch (_: UserNotFoundException) {
                    api.signup(identity.firebaseIdToken)
                }
            saveSession(AuthSession(identity.user, tokens.accessToken, tokens.refreshToken))
            restored = true
        }

    suspend fun accessToken(): String? =
        mutex.withLock {
            restoreStoredSession()
            val current = session ?: return@withLock null
            if (!isTokenExpired(current.accessToken)) return@withLock current.accessToken
            if (isTokenExpired(current.refreshToken)) {
                clearSession()
                return@withLock null
            }
            try {
                val token = api.refresh(current.refreshToken)
                saveSession(current.copy(accessToken = token))
                token
            } catch (_: InvalidSessionException) {
                clearSession()
                null
            }
        }

    suspend fun signOut() {
        val previous =
            mutex.withLock {
                val current = session
                clearSession()
                current
            } ?: return
        for (token in listOf(previous.refreshToken, previous.accessToken)) {
            try {
                api.revoke(token)
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {}
        }
    }

    private suspend fun restoreStoredSession() {
        if (restored) return
        session = storage.read()
        restored = true
        publishState()
    }

    private suspend fun saveSession(value: AuthSession) {
        storage.save(value)
        session = value
        publishState()
    }

    private suspend fun clearSession() {
        storage.clear()
        session = null
        restored = true
        publishState()
    }

    private fun publishState() {
        mutableState.value = session?.let { AuthState.SignedIn(it.user) } ?: AuthState.SignedOut
    }

    private companion object {
        const val RESTORE_ATTEMPTS = 3
        const val RESTORE_RETRY_DELAY_MILLIS = 100L
    }
}
