package com.cornellappdev.score.model

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.exception.ApolloException
import com.cornellappdev.score.util.isValidSport
import com.cornellappdev.score.util.parseColor
import com.example.score.GameByIdQuery
import com.example.score.GamesQuery
import com.example.score.PagedGamesQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import javax.inject.Singleton

private const val TIMEOUT_TIME_MILLIS = 5000L

/**
 * This is a singleton responsible for fetching and caching all data for Score.
 * Right now, it makes a network request for all possible games. In the future,
 * we should limit this to games only in a certain time range, to prevent the
 * app from slowing down and improve load times.
 */
@Singleton
class ScoreRepository @Inject constructor(
    private val apolloClient: ApolloClient,
    private val appScope: CoroutineScope,
) {
    private val _upcomingGamesFlow =
        MutableStateFlow<ApiResponse<List<Game>>>(ApiResponse.Loading)
    val upcomingGamesFlow = _upcomingGamesFlow.asStateFlow()

    private val _currentGameFlow =
        MutableStateFlow<ApiResponse<GameDetailsGame>>(ApiResponse.Loading)
    val currentGamesFlow = _currentGameFlow.asStateFlow()

    companion object {
        private const val PAGE_LIMIT = 100
        private const val MAX_RETRIES = 3
        private const val PAGE_TIMEOUT_MILLIS = 3000L
    }

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
        _upcomingGamesFlow.value = ApiResponse.Loading
        val allGames = mutableListOf<Game>()
        var offset = 0
        var retries = 0

        try {
            while (true) {
                val pageResult: List<PagedGamesQuery.Game?>? = try {
                    withTimeout(PAGE_TIMEOUT_MILLIS) {
                        apolloClient.query(PagedGamesQuery(limit = PAGE_LIMIT, offset = offset))
                            .execute()
                            .data
                            ?.games
                    }
                } catch (e: Exception) {
                    null
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
                    .mapNotNull { gql ->
                        val scores = gql.result?.split(",")?.getOrNull(1)?.split("-")
                        val cornellScore = scores?.getOrNull(0)?.toNumberOrNull()
                        val otherScore = scores?.getOrNull(1)?.toNumberOrNull()
                        gql.team?.image?.let { imageUrl ->
                            Game(
                                id = gql.id ?: "",
                                teamLogo = imageUrl,
                                teamName = gql.team.name,
                                teamColor = parseColor(gql.team.color).copy(alpha = 0.4f * 255),
                                gender = if (gql.gender == "Mens") "Men's" else "Women's",
                                sport = gql.sport,
                                date = gql.date,
                                city = gql.city,
                                cornellScore = cornellScore,
                                otherScore = otherScore
                            )
                        }
                    }

                allGames.addAll(pageGames)

                if (pageResult.size < PAGE_LIMIT) break
                offset += PAGE_LIMIT
            }

            _upcomingGamesFlow.value =
                if (allGames.isNotEmpty()) ApiResponse.Success(allGames)
                else ApiResponse.Error

        } catch (e: Exception) {
            Log.e("ScoreRepository", "Error fetching upcoming games", e)
            _upcomingGamesFlow.value = ApiResponse.Error
        }
    }

    /**
     * Asynchronously fetches game details for a particular game. Once finished, will update
     * `currentGamesFlow` to be observed.
     */
    fun getGameById(id: String) = appScope.launch {
        _currentGameFlow.value = ApiResponse.Loading
        try {
            val response =
                withTimeout(TIMEOUT_TIME_MILLIS) {
                    apolloClient.query(GameByIdQuery(id)).execute()
                }

            if (response.hasErrors()) {
                Log.e("ScoreRepository", "Error fetching game with id: $id: ${response.errors}")
                _currentGameFlow.value = ApiResponse.Error
                return@launch
            }

            response.data?.game?.let {
                _currentGameFlow.value = ApiResponse.Success(it.toGameDetails())
            } ?: _currentGameFlow.update { ApiResponse.Error }

        } catch (e: ApolloException) {
            Log.e("ScoreRepository", "Error fetching game with id: $id: ", e)
            _currentGameFlow.value = ApiResponse.Error
        } catch (e: Exception) {
            Log.e("ScoreRepository", "A timeout or other error occurred for game id: $id", e)
            _currentGameFlow.value = ApiResponse.Error
        }
    }

}

fun String.toNumberOrNull(): Number? {
    return this.trim().toFloatOrNull() ?: this.trim().toIntOrNull()
}

