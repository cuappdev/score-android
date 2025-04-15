package com.cornellappdev.score.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.cornellappdev.score.model.ApiResponse
import com.cornellappdev.score.model.DetailsCardData
import com.cornellappdev.score.model.ScoreRepository
import com.cornellappdev.score.model.map
import com.cornellappdev.score.model.toGameCardData
import com.cornellappdev.score.nav.root.ScoreScreens
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
    private val gameDetailsPageData = savedStateHandle.toRoute<ScoreScreens.GameDetailsPage>()

    init {
        scoreRepository.getGameById(gameDetailsPageData.gameId)
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