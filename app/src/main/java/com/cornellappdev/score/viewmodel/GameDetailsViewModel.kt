package com.cornellappdev.score.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.cornellappdev.score.model.ApiResponse
import com.cornellappdev.score.model.DetailsCardData
import com.cornellappdev.score.model.ScoreRepository
import com.cornellappdev.score.model.SocketGameUpdateData
import com.cornellappdev.score.model.SocketManager
import com.cornellappdev.score.model.applySocketUpdate
import com.cornellappdev.score.model.map
import com.cornellappdev.score.model.toGameCardData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import java.time.Instant
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
    private val pendingUpdates = mutableListOf<SocketGameUpdateData>()
    private var latestUpdateTime: Instant? = null

    init {
        asyncCollect(scoreRepository.currentGamesFlow) { response ->
            if (response is ApiResponse.Success && response.data.id != gameId) return@asyncCollect
            applyMutation {
                copy(
                    loadedState = response.map { gameCard ->
                        // Apply every partial update in order so omitted fields aren't lost.
                        pendingUpdates.fold(gameCard.toGameCardData()) { card, update ->
                            card.applySocketUpdate(update)
                        }.also { pendingUpdates.clear() }
                    }
                )
            }
        }
        onRefresh()

        // Start listening before subscribing, including an immediate subscription snapshot.
        viewModelScope.launch(start = CoroutineStart.UNDISPATCHED) {
            socketManager.gameUpdateFlow.collect { envelope ->
                if (envelope.gameId != gameId) return@collect
                val updateTime = runCatching { Instant.parse(envelope.timestamp) }.getOrNull()
                val latestTime = latestUpdateTime
                if (updateTime != null && latestTime != null && updateTime.isBefore(latestTime)) {
                    return@collect
                }
                if (updateTime != null) latestUpdateTime = updateTime
                applyMutation {
                    val current = loadedState
                    if (current !is ApiResponse.Success) {
                        pendingUpdates.add(envelope.data)
                        return@applyMutation this
                    }
                    copy(loadedState = ApiResponse.Success(current.data.applySocketUpdate(envelope.data)))
                }
            }
        }
        socketManager.subscribe(gameId)
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
