package com.cornellappdev.score.util

/**
 * A simple class that represents a UI event.
 *
 * Distinct from the payload alone since different events will still
 * trigger LaunchedEffect.
 */
class UIEvent<T>(
    val payload: T,
) {

    private var isConsumed = false

    /**
     * Consume the event. An event may only be consumed once.
     */
    fun consume(then: (T) -> Unit) {
        if (isConsumed) {
            return
        }

        then(payload)
        isConsumed = true
    }

    /**
     * Consume the event in a coroutine. An event may only be consumed once.
     */
    suspend fun consumeSuspend(then: suspend (T) -> Unit) {
        if (isConsumed) {
            return
        }

        then(payload)
        isConsumed = true
    }
}
