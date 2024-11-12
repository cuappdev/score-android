package com.cornellappdev.score.viewmodel

import com.cornellappdev.score.api.GameApiRepository
import com.cornellappdev.score.model.GameCardData
import com.cornellappdev.score.model.GenderDivision
import com.cornellappdev.score.model.SportSelection
import com.cornellappdev.score.nav.root.RootNavigationRepository
import com.cornellappdev.score.util.sportSelectionList
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val rootNavigationRepository: RootNavigationRepository,
    private val gameApiRepository: GameApiRepository,
) : BaseViewModel<HomeViewModel.HomeUiState>(
    initialUiState = HomeUiState(
        selectedGender = GenderDivision.ALL,
        sportSelect = SportSelection.All,
        selectionList = sportSelectionList,
        upcomingGameList = emptyList()
    )
) {

    data class HomeUiState(
        val selectedGender: GenderDivision,
        val sportSelect: SportSelection,
        val selectionList: List<SportSelection>,
        val upcomingGameList: List<GameCardData>,
        // TODO Add remaining dynamic data for UI
    )

    fun onGenderSelected(gender: GenderDivision) {
        applyMutation {
            copy(
                selectedGender = gender
            )
        }
    }

    fun onSportSelected(sport: SportSelection) {
        applyMutation {
            copy(
                sportSelect = sport
            )
        }
    }

    fun onRefresh() {
        gameApiRepository.fetchUpcomingGames()
    }

    init {
        asyncCollect(gameApiRepository.upcomingGames) { response ->
            applyMutation {
                copy(
                    upcomingGameList = response
                )
            }
        }

        onRefresh()
    }
}
