package com.cornellappdev.score.model

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.example.score.GamesQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import com.cornellappdev.score.util.parseColor

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


    /**
     * Asynchronously fetches the list of games from the API. Once finished, will send down
     * `upcomingGamesFlow` to be observed.
     */
    fun fetchGames() = appScope.launch {
        _upcomingGamesFlow.value = ApiResponse.Loading
        try {
            val result = (apolloClient.query(GamesQuery()).execute()).toResult()

            if (result.isSuccess) {
                val games = result.getOrNull()

                val upcomingGameslist: List<Game> =
                    games?.games?.mapNotNull { game ->
                        game?.team?.image?.let {
                            Game(
                                teamLogo = it,
                                teamName = game.team.name,
                                teamColor = parseColor(game.team.color).copy(alpha = 0.4f*255),
                                gender = game.gender,
                                sport = game.sport,
                                date = game.date,
                                city = game.city
                            )
                        }
                    } ?: emptyList()
                _upcomingGamesFlow.value = ApiResponse.Success(upcomingGameslist)
            } else {
                _upcomingGamesFlow.value = ApiResponse.Error
            }

        } catch (e: Exception) {
            Log.e("ScoreRepository", "Error fetching posts: ", e)
            _upcomingGamesFlow.value = ApiResponse.Error
        }
    }
}
