package com.cornellappdev.score.viewmodel

import com.cornellappdev.score.model.ApiResponse
import com.cornellappdev.score.model.GameCardData
import com.cornellappdev.score.model.GenderDivision
import com.cornellappdev.score.model.ScoreRepository
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.model.SportSelection
import com.cornellappdev.score.model.isValidSport
import com.cornellappdev.score.model.map
import com.cornellappdev.score.model.toGameCardData
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

data class PastGamesUiState(
    val selectedGender: GenderDivision,
    val sportSelect: SportSelection,
    val selectionList: List<SportSelection>,
    val loadedState: ApiResponse<List<GameCardData>>
) {
    //TODO: refactor filters to use flows - not best practice to expose original games list to the view
    val filteredGames: List<GameCardData>
        get() = when (loadedState) {
            is ApiResponse.Success -> loadedState.data.filter { game ->
                (selectedGender == GenderDivision.ALL || game.gender == selectedGender.displayName) &&
                        (sportSelect is SportSelection.All ||
                                (sportSelect is SportSelection.SportSelect && game.sport == sportSelect.sport.displayName)) &&
                        isValidSport(game.sport)
            }

            ApiResponse.Loading -> emptyList()
            ApiResponse.Error -> emptyList()
        }
    val pastGames: List<GameCardData>
        get() = when (loadedState) {
            is ApiResponse.Success -> loadedState.data.filter { game ->
                isValidSport(game.sport)
            }

            ApiResponse.Loading -> emptyList()
            ApiResponse.Error -> emptyList()
        }.take(3)
}

@HiltViewModel
class PastGamesViewModel @Inject constructor(
    private val scoreRepository: ScoreRepository
) : BaseViewModel<PastGamesUiState>(
    PastGamesUiState(
        selectedGender = GenderDivision.ALL,
        sportSelect = SportSelection.All,
        selectionList = Sport.getSportSelectionList(GenderDivision.ALL).filter {
            it is SportSelection.All ||
                    (it is SportSelection.SportSelect && isValidSport(it.sport.displayName))
        },
        loadedState = ApiResponse.Loading
    )
) {
    init {
        asyncCollect(scoreRepository.upcomingGamesFlow) { response ->
            applyMutation {
                copy(
                    loadedState = response.map { list ->
                        list.map { game ->
                            game.toGameCardData()
                        }.filter { game ->
                            game.date?.isBefore(LocalDate.now()) ?: false
                        }.sortedByDescending { it.date }
                    }
                )
            }
        }
    }

    fun onRefresh() {
        applyMutation {
            copy(loadedState = ApiResponse.Loading)
        }

        scoreRepository.fetchGames()
    }

    fun onGenderSelected(gender: GenderDivision) {
        applyMutation {
            copy(
                selectedGender = gender,
                selectionList = Sport.getSportSelectionList(gender).filter {
                    it is SportSelection.All ||
                            (it is SportSelection.SportSelect && isValidSport(it.sport.displayName))
                },
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
}