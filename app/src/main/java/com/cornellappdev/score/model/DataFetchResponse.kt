package com.cornellappdev.score.model


/**
 * Represents the state of an ApiResponse fetching data of type [T].
 * Can be: [Loading], which represents the call still loading in, [Error], which represents the
 * API call failing, and [Success], which contains a `data` field containing the [T] data.
 */
sealed class ApiResponse<out T : Any> {
    data object Loading : ApiResponse<Nothing>()
    data object Error : ApiResponse<Nothing>()
    data class Success<out T : Any>(val data: T) : ApiResponse<T>()
}

fun <T : Any, R : Any> ApiResponse<T>.map(transform: (T) -> R): ApiResponse<R> =
    when (this) {
        ApiResponse.Error -> ApiResponse.Error
        ApiResponse.Loading -> ApiResponse.Loading
        is ApiResponse.Success -> ApiResponse.Success(transform(this.data))
    }