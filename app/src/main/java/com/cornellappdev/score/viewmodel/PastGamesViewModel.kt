package com.cornellappdev.score.viewmodel

import com.cornellappdev.score.model.ApiResponse
import com.cornellappdev.score.model.GameCardData
import com.cornellappdev.score.model.GenderDivision
import com.cornellappdev.score.model.OnboardingPreferencesRepository
import com.cornellappdev.score.model.ScoreRepository
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.model.SportSelection
import com.cornellappdev.score.model.map
import com.cornellappdev.score.model.toGameCardData
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

data class PastGamesUiState(
    val selectedGender: GenderDivision,
    val sportSelect: SportSelection,
    val selectionList: List<SportSelection>,
    val favoriteSports: Set<Sport> = emptySet(),
    val loadedState: ApiResponse<List<GameCardData>>
) {
    //TODO: refactor filters to use flows - not best practice to expose original games list to the view
    val filteredGames: List<GameCardData>
        get() = when (loadedState) {
            is ApiResponse.Success -> {
                val favoriteSportNames = favoriteSports.map { it.displayName }
                loadedState.data.filter { game ->
                    val matchesGender = when (selectedGender) {
                        GenderDivision.ALL -> true
                        GenderDivision.FAVORITES -> if (favoriteSports.isNotEmpty()) {
                            game.sport in favoriteSportNames
                        } else {
                            true
                        }
                        else -> game.gender == selectedGender.displayName
                    }
                    val matchesSport = (sportSelect is SportSelection.All ||
                            (sportSelect is SportSelection.SportSelect && game.sport == sportSelect.sport.displayName))
                    matchesGender && matchesSport
                }
            }

            ApiResponse.Loading -> emptyList()
            ApiResponse.Error -> emptyList()
        }
    val pastGames: List<GameCardData>
        get() = when (loadedState) {
            is ApiResponse.Success -> loadedState.data

            ApiResponse.Loading -> emptyList()
            ApiResponse.Error -> emptyList()
        }.take(3)
}

@HiltViewModel
class PastGamesViewModel @Inject constructor(
    private val scoreRepository: ScoreRepository,
    preferencesRepository: OnboardingPreferencesRepository
) : BaseViewModel<PastGamesUiState>(
    PastGamesUiState(
        selectedGender = GenderDivision.FAVORITES,
        sportSelect = SportSelection.All,
        selectionList = Sport.getSportSelectionList(GenderDivision.FAVORITES),
        favoriteSports = preferencesRepository.getFavoriteSportNames()
            .mapNotNull { runCatching { Sport.valueOf(it) }.getOrNull() }
            .toSet(),
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

    fun onRefresh() {
        applyMutation { copy(loadedState = ApiResponse.Loading) }

        scoreRepository.fetchGames()
    }
}