package com.cornellappdev.score.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.cornellappdev.score.model.ApiResponse
import com.cornellappdev.score.model.DetailsCardData
import com.cornellappdev.score.model.ScoreRepository
import com.cornellappdev.score.model.map
import com.cornellappdev.score.model.toGameCardData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class GameDetailsUiState(
    val loadedState: ApiResponse<DetailsCardData>
)

@HiltViewModel
class GameDetailsViewModel @Inject constructor(
    scoreRepository: ScoreRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<GameDetailsUiState>(
        initialUiState = GameDetailsUiState(
            loadedState = ApiResponse.Loading
    )
) {
    init {
        val gameId: String? = savedStateHandle["gameId"]
        scoreRepository.getGameById(gameId ?: "")
        asyncCollect(scoreRepository.currentGamesFlow) { response ->
            applyMutation {
                copy(
                    loadedState = response.map { gameCard ->
                        gameCard.toGameCardData()
                    }
                )
            }
        }
    }
}