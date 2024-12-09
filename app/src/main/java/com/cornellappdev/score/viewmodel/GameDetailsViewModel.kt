package com.cornellappdev.score.viewmodel

import com.cornellappdev.score.api.GameApiRepository
import com.cornellappdev.score.model.GameCardData
import com.cornellappdev.score.nav.root.RootNavigationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameDetailsViewModel @Inject constructor(
    private val rootNavigationRepository: RootNavigationRepository,
    private val gameApiRepository: GameApiRepository,
    ) : BaseViewModel<GameDetailsViewModel.GameDetailsUiState>(
        initialUiState = GameDetailsUiState(
            homeScore = 0, awayScore = 0
    )
) {
    data class GameDetailsUiState(
        val homeScore: Int,
        val awayScore: Int,
    )
}