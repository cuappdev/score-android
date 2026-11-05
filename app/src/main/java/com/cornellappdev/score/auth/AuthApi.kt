package com.cornellappdev.score.auth

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Optional
import com.apollographql.apollo.api.http.HttpHeader
import com.apollographql.apollo.exception.ApolloHttpException
import com.example.score.LoginUserMutation
import com.example.score.LogoutUserMutation
import com.example.score.RefreshAccessTokenMutation
import com.example.score.SignupUserMutation
import javax.inject.Inject
import javax.inject.Named

interface AuthApi {
    suspend fun login(idToken: String): AuthTokens

    suspend fun signup(identity: GoogleIdentity): AuthTokens

    suspend fun refresh(refreshToken: String): String

    suspend fun revoke(token: String)
}

class UserNotFoundException : Exception()

class InvalidSessionException : Exception()

class ApolloAuthApi @Inject constructor(@Named("auth") private val client: ApolloClient) : AuthApi {
    override suspend fun login(idToken: String): AuthTokens {
        val response = client.mutation(LoginUserMutation(idToken)).execute()
        if (response.errors?.any { it.message == "User not found." } == true) {
            throw UserNotFoundException()
        }
        response.checkErrors()
        val result = response.data?.loginUser ?: error("Missing login response")
        return tokens(result.accessToken, result.refreshToken)
    }

    override suspend fun signup(identity: GoogleIdentity): AuthTokens {
        val response =
            client
                .mutation(
                    SignupUserMutation(
                        identity.firebaseIdToken,
                        Optional.present(identity.user.name),
                    )
                )
                .execute()
        response.checkErrors()
        val result = response.data?.signupUser ?: error("Missing signup response")
        return tokens(result.accessToken, result.refreshToken)
    }

    override suspend fun refresh(refreshToken: String): String {
        val response =
            client
                .mutation(RefreshAccessTokenMutation())
                .httpHeaders(listOf(HttpHeader("Authorization", "Bearer $refreshToken")))
                .execute()
        val status = (response.exception as? ApolloHttpException)?.statusCode
        if (status == 422 || response.isSessionRejected()) throw InvalidSessionException()
        response.checkErrors()
        return response.data?.refreshAccessToken?.newAccessToken?.takeIf { it.isNotBlank() }
            ?: error("Missing refresh response")
    }

    override suspend fun revoke(token: String) {
        client
            .mutation(LogoutUserMutation())
            .httpHeaders(listOf(HttpHeader("Authorization", "Bearer $token")))
            .execute()
            .checkErrors()
    }

    private fun tokens(access: String?, refresh: String?): AuthTokens {
        require(!access.isNullOrBlank() && !refresh.isNullOrBlank()) { "Missing session tokens" }
        return AuthTokens(access, refresh)
    }

    private fun ApolloResponse<*>.isSessionRejected(): Boolean {
        if ((exception as? ApolloHttpException)?.statusCode == 401) return true
        return errors.orEmpty().any {
            it.extensions?.get("code") == "UNAUTHENTICATED" ||
                it.message in
                    setOf(
                        "Token has expired",
                        "Token has been revoked",
                        "Signature verification failed",
                        "Not enough segments",
                    )
        }
    }

    private fun ApolloResponse<*>.checkErrors() {
        exception?.let { throw it }
        check(errors.isNullOrEmpty()) { "Authentication request failed" }
    }
}
