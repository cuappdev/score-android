package com.cornellappdev.score.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.cornellappdev.score.model.ApiResponse
import com.cornellappdev.score.model.DetailsCardData
import com.cornellappdev.score.model.ScoreRepository
import com.cornellappdev.score.model.SocketManager
import com.cornellappdev.score.model.applySocketUpdate
import com.cornellappdev.score.model.map
import com.cornellappdev.score.model.toGameCardData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GameDetailsUiState(
    val loadedState: ApiResponse<DetailsCardData>
)

@HiltViewModel
class GameDetailsViewModel @Inject constructor(
    private val scoreRepository: ScoreRepository,
    private val socketManager: SocketManager,
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

        socketManager.subscribe(gameId)

        viewModelScope.launch {
            socketManager.gameUpdateFlow.collect { envelope ->
                if (envelope.gameId != gameId) return@collect
                applyMutation {
                    val current = loadedState
                    if (current !is ApiResponse.Success) return@applyMutation this
                    copy(loadedState = ApiResponse.Success(current.data.applySocketUpdate(envelope.data)))
                }
            }
        }
    }

    fun onRefresh() {
        applyMutation { copy(loadedState = ApiResponse.Loading) }
        scoreRepository.getGameById(gameId)
    }

    override fun onCleared() {
        super.onCleared()
        socketManager.unsubscribe(gameId)
    }
}
