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

/**
 * This is a singleton responsible for fetching and caching all data for Score.
 * Right now, it makes a network request for all possible games. In the future,
 * we should limit this to games only in a certain time range, to prevent the
 * app from slowing down and improve load times.
 */
@Singleton
class ScoreRepository @Inject constructor(
    private val apolloClient: ApolloClient,
    // TODO use this to launch queries
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
            val response = (apolloClient.query(GamesQuery()).execute())
            val games = response.data?.games ?: emptyList()
            Log.d("ScoreRepository", "response fetched successfully")
            val list: List<Game> = games.mapNotNull { game ->
              val scores = game?.result?.split(",")?.getOrNull(1)?.split("-")
//                val cornellScore = game?.boxScore?.lastOrNull()?.corScore
//                val otherScore = game?.boxScore?.lastOrNull()?.oppScore
                val cornellScore = scores?.getOrNull(0)?.toNumberOrNull()
                val otherScore = scores?.getOrNull(1)?.toNumberOrNull()
                if (game != null) {
                    Log.d("Scores", " sport: " + game.sport + " date: "+ game.date + " result: DO LATER" + "cornell: " + cornellScore + "other: " + otherScore)
                }
                game?.team?.image?.let {
                    Game(
                        teamLogo = it,//game.team.image,
                        teamName = game.team.name,
                        teamColor = game.team.color,
                        gender = game.gender,
                        sport = game.sport,
                        date = game.date,
                        city = game.city,
                        cornellScore = cornellScore,
                        otherScore = otherScore
                    )
                }
            }
            Log.d("ScoreRepository", "#games: ${list.size}")
            _upcomingGamesFlow.value = ApiResponse.Success(list.toList())
        } catch (e: Exception) {
            Log.e("ScoreRepository", "Error fetching posts: ", e)
            _upcomingGamesFlow.value = ApiResponse.Error
        }
    }
}

fun String.toNumberOrNull(): Number? {
    return when {
        this.contains(".") -> this.toFloatOrNull()  // Try converting to Float if there's a decimal
        else -> this.toIntOrNull()  // Otherwise, try converting to Int
    }
}
