package com.cornellappdev.score.model

import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.exception.NoDataException

/**
 * Maps an ApolloResponse to a generic Kotlin result. It either provides the data with no errors, or
 * a failure response containing the error message in the throwable.
 */
fun <T : Operation.Data> ApolloResponse<T>.toResult(): Result<T> {
    if (hasErrors() || exception != null) {
        return Result.failure(
            exception?.cause ?: RuntimeException(
                errors?.firstOrNull()?.message ?: "Unknown error occurred"
            )
        )
    }
    return try {
        Result.success(dataOrThrow())
    } catch (e: NoDataException) {
        Result.failure(e)
    }
}