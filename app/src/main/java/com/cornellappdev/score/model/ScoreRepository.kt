package com.cornellappdev.score.model

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.cornellappdev.score.util.isValidSport
import com.example.score.GameByIdQuery
import com.example.score.InitialGamesQuery
import com.example.score.PagedGamesQuery
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.coroutines.sync.Mutex
import java.time.LocalDate
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds

private const val TAG = "ScoreRepository"
private val GAME_DETAILS_TIMEOUT = 5.seconds
private const val PAGE_LIMIT = 100
private const val MAX_RETRIES = 3
private val PAGE_TIMEOUT = 3.seconds

/**
 * This is a singleton responsible for fetching and caching all data for Score.
 * Publishes a small date window first, then loads the full game history.
 */
@Singleton
class ScoreRepository @Inject constructor(
    private val apolloClient: ApolloClient,
    private val appScope: CoroutineScope,
) {
    private val gamesFetchMutex = Mutex()

    private val _upcomingGamesFlow =
        MutableStateFlow<ApiResponse<List<Game>>>(ApiResponse.Loading)
    val upcomingGamesFlow = _upcomingGamesFlow.asStateFlow()

    private val _currentGameFlow =
        MutableStateFlow<ApiResponse<GameDetailsGame>>(ApiResponse.Loading)
    val currentGamesFlow = _currentGameFlow.asStateFlow()

    /** Publishes nearby games first, then loads history while preserving results on failure. */
    fun fetchGames() = appScope.launch {
        gamesFetchMutex.lock()
        val previousSuccess = _upcomingGamesFlow.value as? ApiResponse.Success
        _upcomingGamesFlow.value = ApiResponse.Loading
        val allGames = mutableListOf<Game>()
        var offset = 0
        var retries = 0
        var initialWindow = true
        var historyComplete = false

        try {
            // The page count is unknown, and retries must reuse the current offset.
            while (true) {
                val pageResult = try {
                    withTimeoutOrNull(PAGE_TIMEOUT) {
                        if (initialWindow) {
                            val today = LocalDate.now()
                            apolloClient.query(
                                InitialGamesQuery(
                                    today.minusDays(7).atStartOfDay().toString(),
                                    today.plusDays(30).atStartOfDay().toString()
                                )
                            ).execute().toResult().getOrNull()?.gamesByDate
                                ?.map { it?.gameListItem }
                        } else {
                            apolloClient.query(
                                PagedGamesQuery(limit = PAGE_LIMIT, offset = offset)
                            ).execute().toResult().getOrNull()?.games
                                ?.map { it?.gameListItem }
                        }
                    }
                } catch (e: CancellationException) {
                    throw e
                } catch (_: Exception) {
                    null
                }

                // A failed or empty date window falls back to the full fetch.
                if (initialWindow && pageResult.isNullOrEmpty()) {
                    initialWindow = false
                    continue
                }

                if (pageResult == null) {
                    if (retries < MAX_RETRIES) {
                        retries++
                        continue
                    } else {
                        break
                    }
                }

                if (pageResult.isEmpty()) {
                    historyComplete = true
                    break
                }

                retries = 0

                val pageGames: List<Game> = pageResult
                    .filterNotNull()
                    .filter { isValidSport(it.sport) }
                    .mapNotNull { it.toGame() }

                allGames.addAll(pageGames)

                if (initialWindow) {
                    if (allGames.isNotEmpty()) {
                        _upcomingGamesFlow.value = ApiResponse.Success(allGames.toList())
                    }
                    initialWindow = false
                    continue
                }

                if (pageResult.size < PAGE_LIMIT) {
                    historyComplete = true
                    break
                }
                offset += PAGE_LIMIT
            }

            _upcomingGamesFlow.value =
                if (allGames.isNotEmpty()) ApiResponse.Success(allGames.asReversed().distinctBy { it.id }.asReversed())
                else if (historyComplete) ApiResponse.Success(emptyList())
                else previousSuccess ?: ApiResponse.Error

        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching upcoming games", e)
            if (_upcomingGamesFlow.value !is ApiResponse.Success) {
                _upcomingGamesFlow.value = previousSuccess ?: ApiResponse.Error
            }
        } finally {
            gamesFetchMutex.unlock()
        }
    }

    /**
     * Asynchronously fetches game details for a particular game. Once finished, will update
     * `currentGamesFlow` to be observed.
     */
    fun getGameById(id: String) = appScope.launch {
        Log.d(TAG, "Fetching game with id: $id")
        _currentGameFlow.value = ApiResponse.Loading
        try {
            val result =
                withTimeout(GAME_DETAILS_TIMEOUT) {
                    apolloClient.query(GameByIdQuery(id)).execute().toResult()
                }


            result.getOrNull()?.game?.let {
                _currentGameFlow.value = ApiResponse.Success(it.toGameDetails())

            } ?: _currentGameFlow.update { ApiResponse.Error }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching game with id: ${id}: ", e)
            _currentGameFlow.value = ApiResponse.Error
        }
    }
}
