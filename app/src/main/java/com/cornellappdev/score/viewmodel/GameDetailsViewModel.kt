package com.cornellappdev.score.viewmodel

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
    private val scoreRepository: ScoreRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<GameDetailsUiState>(
    initialUiState = GameDetailsUiState(
        loadedState = ApiResponse.Loading
    )
) {
    private val gameId: String = checkNotNull(savedStateHandle["gameId"])

    init {
        asyncCollect(scoreRepository.currentGamesFlow) { response ->
            applyMutation {
                copy(
                    loadedState = response.map { gameCard ->
                        gameCard.toGameCardData()
                    }
                )
            }
        }
        onRefresh()
    }

    fun onRefresh() {
        applyMutation { copy(loadedState = ApiResponse.Loading) }
        scoreRepository.getGameById(gameId)
    }
}