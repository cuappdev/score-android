package com.cornellappdev.score.viewmodel

import com.cornellappdev.score.components.DateFilter
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
    val loadedState: ApiResponse<List<GameCardData>>,
    val selectedDateFilter: DateFilter? = null
) {
    //TODO: refactor filters to use flows - not best practice to expose original games list to the view
    val filteredGames: List<GameCardData>
        get() = when (loadedState) {
            is ApiResponse.Success -> loadedState.data.filter { game ->
                val genderMatch = selectedGender == GenderDivision.ALL || game.gender == selectedGender.displayName
                val sportMatch = sportSelect is SportSelection.All ||
                        (sportSelect is SportSelection.SportSelect && game.sport == sportSelect.sport.displayName)
                val dateMatch = when (selectedDateFilter) {
                    DateFilter.TODAY -> game.date == LocalDate.now()
                    DateFilter.WITHIN_7_DAYS -> game.date != null && !game.date.isAfter(LocalDate.now().plusDays(7))
                    DateFilter.WITHIN_A_MONTH -> game.date != null && !game.date.isAfter(LocalDate.now().plusDays(30))
                    DateFilter.OVER_A_MONTH -> game.date != null && game.date.isAfter(LocalDate.now().plusDays(30))
                    null -> true
                }
                genderMatch && sportMatch && dateMatch
            }

            ApiResponse.Loading -> emptyList()
            ApiResponse.Error -> emptyList()
        }
    val upcomingGames: List<GameCardData>
        get() = when (loadedState) {
            is ApiResponse.Success -> loadedState.data

            ApiResponse.Loading -> emptyList()
            ApiResponse.Error -> emptyList()
        }.take(3)
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val scoreRepository: ScoreRepository
) : BaseViewModel<HomeUiState>(
    HomeUiState(
        selectedGender = GenderDivision.ALL,
        sportSelect = SportSelection.All,
        selectionList = Sport.getSportSelectionList(GenderDivision.ALL),
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
                selectedGender = gender,
                selectionList = Sport.getSportSelectionList(gender),
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

    fun onDateFilterApplied(date: DateFilter?) {
        applyMutation { copy(selectedDateFilter = date) }
    }

    fun onFiltersReset() {
        applyMutation { copy(selectedDateFilter = null) }
    }
}
