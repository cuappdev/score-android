package com.cornellappdev.score.model

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.cornellappdev.score.util.isValidSport
import com.cornellappdev.score.util.parseColor
import com.cornellappdev.score.util.parseResultScore
import com.example.score.GameByIdQuery
import com.example.score.InitialGamesQuery
import com.example.score.GamesQuery
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

private const val TIMEOUT_TIME_MILLIS = 5000L
private const val PAGE_LIMIT = 100
private const val MAX_RETRIES = 3
private const val PAGE_TIMEOUT_MILLIS = 3000L

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

    /**
     * Asynchronously fetches the list of games from the API. Once finished, will send down
     * `upcomingGamesFlow` to be observed.
     */
    fun fetchGamesPrev() = appScope.launch {
        _upcomingGamesFlow.value = ApiResponse.Loading
        try {
            val result =
                withTimeout(TIMEOUT_TIME_MILLIS) {
                    apolloClient.query((GamesQuery())).execute().toResult()
                }

            if (result.isSuccess) {
                val games = result.getOrNull()

                val gamesList: List<Game> =
                    games?.games?.filter { game -> isValidSport(game?.sport ?: "") }
                        ?.mapNotNull { game ->
                            /**
                             * The final scores in the past game cards are obtained by parsing a String
                             * result from the GameQuery, which is oftentimes in the format
                             * Result, CornellScore-OpponentScore (e.g. "W, 2-1"). Not all of the strings
                             * are in this format (e.g. 4th of 6, 1498 points for women's Swimming and
                             * Diving), but in this case, the cornellScore and otherScore parameters of
                             * the game and associated card should be null, and as of right now,
                             * null-scored games are filtered out.
                             */
                            val scores = game?.result?.split(",")?.getOrNull(1)?.split("-")
                            val cornellScore = scores?.getOrNull(0)?.toNumberOrNull()
                            val otherScore = scores?.getOrNull(1)?.toNumberOrNull()
                            game?.team?.image?.let {
                                Game(
                                    id = game.id ?: "", // Should never be null
                                    teamLogo = it,
                                    teamName = game.team.name,
                                    time = game.time,
                                    teamColor = parseColor(game.team.color).copy(alpha = 0.4f * 255),
                                    gender = if (game.gender == "Mens") "Men's" else "Women's",
                                    sport = game.sport,
                                    date = game.date,
                                    city = game.city,
                                    cornellScore = cornellScore,
                                    otherScore = otherScore
                                )
                            }
                        } ?: emptyList()
                _upcomingGamesFlow.value = ApiResponse.Success(gamesList)
            } else {
                _upcomingGamesFlow.value = ApiResponse.Error
            }

        } catch (e: Exception) {
            Log.e("ScoreRepository", "Error fetching posts: ", e)
            _upcomingGamesFlow.value = ApiResponse.Error
        }
    }

    fun fetchGames() = appScope.launch {
        if (!gamesFetchMutex.tryLock()) return@launch
        _upcomingGamesFlow.value = ApiResponse.Loading
        val allGames = mutableListOf<Game>()
        var offset = 0
        var retries = 0
        var initialWindow = true

        try {
            while (true) {
                val pageResult = try {
                    withTimeoutOrNull(PAGE_TIMEOUT_MILLIS) {
                        if (initialWindow) {
                            val today = LocalDate.now()
                            apolloClient.query(
                                InitialGamesQuery(
                                    today.atStartOfDay().toString(),
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
                } catch (e: Exception) {
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
                    break
                }

                retries = 0

                val pageGames: List<Game> = pageResult
                    .filterNotNull()
                    .filter { gql -> isValidSport(gql.sport ?: "") }
                    .mapNotNull { graphqlGame ->
                        val scores = graphqlGame.result?.split(",")?.getOrNull(1)?.split("-")
                        val cornellScore = scores?.getOrNull(0)?.toNumberOrNull()
                            ?: parseResultScore(graphqlGame.result)?.first
                        val otherScore = scores?.getOrNull(1)?.toNumberOrNull() ?: parseResultScore(
                            graphqlGame.result
                        )?.second
                        graphqlGame.team?.image?.let { imageUrl ->
                            Game(
                                id = graphqlGame.id ?: "",
                                teamLogo = imageUrl,
                                time = graphqlGame.time,
                                teamName = graphqlGame.team.name,
                                teamColor = parseColor(graphqlGame.team.color).copy(alpha = 0.4f * 255),
                                gender = if (graphqlGame.gender == "Mens") "Men's" else "Women's",
                                sport = graphqlGame.sport,
                                date = graphqlGame.date,
                                city = graphqlGame.city,
                                cornellScore = cornellScore,
                                otherScore = otherScore
                            )
                        }
                    }

                allGames.addAll(pageGames)

                if (initialWindow) {
                    if (allGames.isNotEmpty()) {
                        _upcomingGamesFlow.value = ApiResponse.Success(allGames.toList())
                    }
                    initialWindow = false
                    continue
                }

                if (pageResult.size < PAGE_LIMIT) break
                offset += PAGE_LIMIT
            }

            _upcomingGamesFlow.value =
                if (allGames.isNotEmpty()) ApiResponse.Success(allGames.asReversed().distinctBy { it.id }.asReversed())
                else if (_upcomingGamesFlow.value is ApiResponse.Success) _upcomingGamesFlow.value
                else ApiResponse.Error

        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("ScoreRepository", "Error fetching upcoming games", e)
            if (_upcomingGamesFlow.value !is ApiResponse.Success) {
                _upcomingGamesFlow.value = ApiResponse.Error
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
        Log.d("ScoreRepository", "Fetching game with id: $id")
        _currentGameFlow.value = ApiResponse.Loading
        try {
            val result =
                withTimeout(TIMEOUT_TIME_MILLIS) {
                    apolloClient.query(GameByIdQuery(id)).execute().toResult()
                }


            result.getOrNull()?.game?.let {
                _currentGameFlow.value = ApiResponse.Success(it.toGameDetails())

            } ?: _currentGameFlow.update { ApiResponse.Error }
        } catch (e: Exception) {
            Log.e("ScoreRepository", "Error fetching game with id: ${id}: ", e)
            _currentGameFlow.value = ApiResponse.Error
        }
    }
}

fun String.toNumberOrNull(): Number? {
    return when {
        this.contains(".") -> this.toFloatOrNull()  // Try converting to Float if there's a decimal
        else -> this.toIntOrNull()  // Otherwise, try converting to Int
    }
}

