package com.cornellappdev.score.api

import com.cornellappdev.score.model.GameCardData
import com.cornellappdev.score.util.gameList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameApiRepository @Inject constructor() {

    // TODO: Change to `ApiResponse` class (steal from other repo)
    private val _upcomingGamesFlow: MutableStateFlow<List<GameCardData>> =
        MutableStateFlow(emptyList())
    val upcomingGames = _upcomingGamesFlow.asStateFlow()

    /**
     * Asynchronously start to load upcoming games, once loaded will be sent down [upcomingGames]
     */
    fun fetchUpcomingGames() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(2000)
            _upcomingGamesFlow.value = gameList
        }
    }

    fun fetchTeams(){

    }
}
