package com.cornellappdev.score.auth

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CancellationException
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
        mutex.withLock { restoreStoredSession() }
        try {
            accessToken()
        } catch (error: CancellationException) {
            throw error
        } catch (_: Exception) {}
    }

    suspend fun signIn(identity: GoogleIdentity) =
        mutex.withLock {
            require(identity.user.email.endsWith("@cornell.edu", ignoreCase = true))
            val tokens =
                try {
                    api.login(identity.firebaseIdToken)
                } catch (_: UserNotFoundException) {
                    api.signup(identity)
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
}
