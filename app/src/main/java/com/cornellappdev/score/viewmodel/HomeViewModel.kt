package com.cornellappdev.score.viewmodel

import com.cornellappdev.score.model.ApiResponse
import com.cornellappdev.score.model.GameCardData
import com.cornellappdev.score.model.GenderDivision
import com.cornellappdev.score.model.ScoreRepository
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.model.SportSelection
import com.cornellappdev.score.model.map
import com.cornellappdev.score.model.toGameCardData
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

data class HomeUiState(
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
                        (sportSelect is SportSelection.All || (sportSelect is SportSelection.SportSelect && game.sport == sportSelect.sport.displayName))
                        && (game.sport == "Baseball" || game.sport == "Basketball" || game.sport == "Field Hockey" || game.sport == "Football" || game.sport == "Ice Hockey" || game.sport == "Lacrosse" || game.sport == "Soccer")
            }

            ApiResponse.Loading -> emptyList()
            ApiResponse.Error -> emptyList()
        }
    val upcomingGames: List<GameCardData> = filteredGames.take(3)
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val scoreRepository: ScoreRepository
) : BaseViewModel<HomeUiState>(
    HomeUiState(
        selectedGender = GenderDivision.ALL,
        sportSelect = SportSelection.All,
        selectionList = Sport.getSportSelectionList().filter {
            it is SportSelection.All || (it is SportSelection.SportSelect && (it.sport.displayName == "Baseball" || it.sport.displayName == "Basketball" || it.sport.displayName == "Field Hockey" || it.sport.displayName == "Football" || it.sport.displayName == "Ice Hockey" || it.sport.displayName == "Lacrosse" || it.sport.displayName == "Soccer"))
        },
        loadedState = ApiResponse.Loading
    )
) {
    init {
        scoreRepository.fetchGames()
        asyncCollect(scoreRepository.upcomingGamesFlow) { response ->
            applyMutation {
                copy(
                    loadedState = response.map { list ->
                        list.map { game ->
                            game.toGameCardData()
                        }.filter { game ->
                            game.date?.isAfter(LocalDate.now().minusDays(1)) ?: false
                        }.sortedBy { it.date }
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
}
