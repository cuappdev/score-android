package com.cornellappdev.score.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.cornellappdev.score.R
import com.cornellappdev.score.model.ApiResponse
import com.cornellappdev.score.model.Game
import com.cornellappdev.score.model.GameCardData
import com.cornellappdev.score.model.GameDetailsGame
import com.cornellappdev.score.model.ScoreRepository
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.nav.root.RootNavigationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class GameDetailsViewModel @Inject constructor(
    private val rootNavigationRepository: RootNavigationRepository,
    private val scoreRepository: ScoreRepository,
    private val savedStateHandle: SavedStateHandle
    ) : BaseViewModel<GameDetailsViewModel.GameDetailsUiState>(
        initialUiState = GameDetailsUiState(
            gameCard = null,
            isLive = false
    )
) {
    init {
        observeCurrentGame()
        viewModelScope.launch{
            val gameId: String? = savedStateHandle["gameId"]
            scoreRepository.getGameById(gameId!!)
        }
    }
    private fun observeCurrentGame() = scoreRepository.currentGamesFlow.onEach { response ->
        Log.d("HomeViewModel", "Response: $response")
        updateGameCard(response)
    }.launchIn(viewModelScope)

    private fun updateGameCard(response: ApiResponse<GameDetailsGame>) {
        val game: GameDetailsGame? = when (response) {
            is ApiResponse.Success -> {
                Log.d("updateGameList", "Success")
                response.data
            }
            ApiResponse.Error -> {
                Log.d("updateGameList", "Error")
                null
            }
            ApiResponse.Loading -> {
                Log.d("updateGameList", "Loading")
                null
            }
        }
        if (game == null){
            return
        }
        val currentDate = LocalDate.now()
        val tomorrowDate = LocalDate.now().plusDays(1)
        val formattedDate = formatDate(game.date)

        applyMutation {
            copy(gameCard = gameCard)
        }
    }
    private fun formatDate(strDate: String): LocalDate? {
        val monthMap = mapOf(
            "Jan" to 1,
            "Feb" to 2,
            "Mar" to 3,
            "Apr" to 4,
            "May" to 5,
            "Jun" to 6,
            "Jul" to 7,
            "Aug" to 8,
            "Sep" to 9,
            "Oct" to 10,
            "Nov" to 11,
            "Dec" to 12
        )

        val parts = strDate.split(" ")
        if (parts.size < 2) return null

        val month = monthMap[parts[0]]
        if (month == null) {
            return null
        }
        val day = parts[1].toIntOrNull() ?: return null

        val currentYear = LocalDate.now().year
        //Log.d("HomeViewModel", "formatDate: ${LocalDate.of(currentYear, month, day)}")
        return LocalDate.of(currentYear, month, day)
    }

    /**
     * Converts from format "#xxxxxx" to a valid hex, with alpha = 40. Ready to be passed into Color()
     */

    private fun formatColor(color: String): Int {
        val alpha = (40 * 255 / 100)// Convert percent to hex (0-255)
        val colorInt = Integer.parseInt(color.removePrefix("#"), 16)
        return (alpha shl 24) or colorInt
    }

    private fun dateToString(date: LocalDate?): String {
        if (date == null) {
            return "--"
        }
        //Log.d("HomeViewModel", "formatedDate: ${date.month.value}/${date.dayOfMonth}/${date.year}")
        return "${date.month.value}/${date.dayOfMonth}/${date.year}"
    }
    data class GameDetailsUiState(
        val gameCard: GameDetailsGame?,
        val isLive: Boolean
    )
}