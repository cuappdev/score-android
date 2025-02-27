package com.cornellappdev.score.model

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
import com.cornellappdev.score.R
import com.example.score.GamesQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ScoreRepository @Inject constructor(
    private val apolloClient: ApolloClient
) {
//    suspend fun getTeams(): ApolloResponse<TeamsQuery.Data> {
//        return apolloClient.query(TeamsQuery()).execute()
//    }

//    private val _upcomingGamesFlow =
//        MutableStateFlow<ApiResponse<ApolloResponse<GamesQuery.Data>>>(ApiResponse.Loading)//<ApiResponse<List<GamesQuery.Game>>>(ApiResponse.Loading)
    private val _upcomingGamesFlow =
            MutableStateFlow<ApiResponse<List<Game>>>(ApiResponse.Loading)
    val upcomingGamesFlow = _upcomingGamesFlow.asStateFlow()


//    /**
//     * Asynchronously fetches the list of posts from the API. Once finished, will send down
//     * `allPostsFlow` to be observed.
//     */
//    fun fetchGames() {
//        _upcomingGamesFlow.value = ApiResponse.Loading
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                _upcomingGamesFlow.value =
//                   ApiResponse.Success(apolloClient.query(GamesQuery()).execute())
//            } catch (e: Exception) {
//                Log.e("ResellPostRepository", "Error fetching posts: ", e)
//                _upcomingGamesFlow.value = ApiResponse.Error
//            }
//        }
//    }

    /**
     * Asynchronously fetches the list of posts from the API. Once finished, will send down
     * `allPostsFlow` to be observed.
     */
    fun fetchGames() {
        //_upcomingGamesFlow.value = ApiResponse.Loading
        CoroutineScope(Dispatchers.IO).launch {
            try {
                //_upcomingGamesFlow.value =
                val response = (apolloClient.query(GamesQuery()).execute())
                val games = response.data?.games ?: emptyList()
                Log.d("ScoreRepository", "response and games fetched successfully")

                val list: List<Game> = games.mapNotNull{ game ->
                //if (game?.team?.image != null){
                    game?.team?.image?.let {
                        Game(
                            teamLogo = it,//game.team.image,
                            teamName = game.team.name,
                            gender = game.gender,
                            sport = game.sport,
                            date = game.date,
                            city = game.city
                        )
                    }
                }
                _upcomingGamesFlow.value = ApiResponse.Success(list)
            } catch (e: Exception) {
                Log.e("ScoreRepository", "Error fetching posts: ", e)
                _upcomingGamesFlow.value = ApiResponse.Error
            }
        }
    }

//    suspend fun getGames(): List<Game> {
//        val response = apolloClient.query(GamesQuery()).execute()
//        val games = response.data?.games ?: emptyList()
//
//        val list: List<Game> = games.mapNotNull{ game ->
//            //if (game?.team?.image != null){
//            game?.team?.image?.let {
//                Game(
//                    teamLogo = it,//game.team.image,
//                    teamName = game.team.name,
//                    gender = game.gender,
//                    sport = game.sport,
//                    date = game.date,
//                    city = game.city
//                )
//            }
//            //}
//        }
//        return list
//    }
}