package com.cornellappdev.score.viewmodel

import com.cornellappdev.score.model.ApiResponse
import com.cornellappdev.score.model.GameCardData
import com.cornellappdev.score.model.GameFilter
import com.cornellappdev.score.model.OnboardingPreferencesRepository
import com.cornellappdev.score.model.ProfileFavorite
import com.cornellappdev.score.model.ScoreRepository
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.model.SportSelection
import com.cornellappdev.score.model.map
import com.cornellappdev.score.model.toGameCardData
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

data class PastGamesUiState(
    val selectedFilter: GameFilter,
    val sportSelect: SportSelection,
    val selectionList: List<SportSelection>,
    val favoriteSports: Set<ProfileFavorite> = emptySet(),
    val loadedState: ApiResponse<List<GameCardData>>
) {
    //TODO: refactor filters to use flows - not best practice to expose original games list to the view
    val filteredGames: List<GameCardData>
        get() = when (loadedState) {
            is ApiResponse.Success -> {
                loadedState.data.filter { game ->
                    val matchesGender = when (selectedFilter) {
                        GameFilter.ALL -> true
                        GameFilter.FAVORITES -> if (favoriteSports.isNotEmpty()) {
                            favoriteSports.any { favorite ->
                                game.sport == favorite.sport.displayName &&
                                        game.gender == favorite.gender.displayName
                            }
                        } else {
                            true
                        }

                        else -> game.gender == selectedFilter.displayName
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
        selectedFilter = GameFilter.FAVORITES,
        sportSelect = SportSelection.All,
        selectionList = Sport.getSportSelectionList(GameFilter.FAVORITES),
        favoriteSports = preferencesRepository.getFavoriteSports(),
        loadedState = ApiResponse.Loading
    )
) {
    init {
        asyncCollect(scoreRepository.upcomingGamesFlow) { response ->
            applyMutation {
                val loadedState = response.map { list ->
                    list.map { game ->
                        game.toGameCardData()
                    }.filter { game ->
                        game.date?.isBefore(LocalDate.now()) ?: false
                    }.sortedByDescending { it.date }
                }
                val pastGames = (loadedState as? ApiResponse.Success)?.data.orEmpty()
                copy(
                    loadedState = loadedState,
                    selectionList = selectionListFor(selectedFilter, favoriteSports, pastGames)
                )
            }
        }
    }

    fun onFilterSelected(filter: GameFilter) {
        applyMutation {
            val newSelectionList = selectionListFor(
                filter,
                favoriteSports,
                (loadedState as? ApiResponse.Success)?.data.orEmpty()
            )
            copy(
                selectedFilter = filter,
                selectionList = newSelectionList,
                sportSelect = if (
                    sportSelect is SportSelection.All || sportSelect in newSelectionList
                ) {
                    sportSelect
                } else {
                    SportSelection.All
                }
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

    private fun selectionListFor(
        filter: GameFilter,
        favoriteSports: Set<ProfileFavorite>,
        pastGames: List<GameCardData>
    ): List<SportSelection> {
        val mostRecentGameBySport = pastGames
            .groupBy { it.sport }
            .mapValues { (_, games) -> games.mapNotNull { it.date }.maxOrNull() }

        val sports = if (filter == GameFilter.FAVORITES) {
            listOf(SportSelection.All) +
                    favoriteSports.map { SportSelection.SportSelect(it.sport) }.distinct()
        } else {
            Sport.getSportSelectionList(filter)
        }

        return sports.sortedWith(
            compareBy<SportSelection> { if (it is SportSelection.All) 0 else 1 }
                .thenByDescending { selection ->
                    (selection as? SportSelection.SportSelect)
                        ?.sport
                        ?.displayName
                        ?.let(mostRecentGameBySport::get)
                        ?: LocalDate.MIN
                }
                .thenBy { (it as? SportSelection.SportSelect)?.sport?.displayName.orEmpty() }
        )
    }
}