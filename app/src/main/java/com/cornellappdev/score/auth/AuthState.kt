package com.cornellappdev.score.auth

import java.util.Base64
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull

sealed interface AuthState {
    data object Loading : AuthState

    data object SignedOut : AuthState

    data class SignedIn(val user: AuthUser) : AuthState
}

@Serializable data class AuthUser(val id: String, val name: String, val email: String)

@Serializable
data class AuthSession(val user: AuthUser, val accessToken: String, val refreshToken: String)

data class AuthTokens(val accessToken: String, val refreshToken: String)

data class GoogleIdentity(val user: AuthUser, val firebaseIdToken: String)

fun isTokenExpired(token: String, nowMillis: Long = System.currentTimeMillis()): Boolean {
    return try {
        val payload = String(Base64.getUrlDecoder().decode(token.split('.')[1]), Charsets.UTF_8)
        val expiresAt =
            Json.parseToJsonElement(payload).jsonObject["exp"]?.jsonPrimitive?.longOrNull
                ?: return true
        expiresAt <= nowMillis / 1000 + 60
    } catch (_: IllegalArgumentException) {
        true
    } catch (_: IndexOutOfBoundsException) {
        true
    }
}
