package com.cornellappdev.score.viewmodel

import com.cornellappdev.score.components.DateFilter
import com.cornellappdev.score.components.LocationFilter
import com.cornellappdev.score.components.PriceFilter
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

data class HomeUiState(
    val selectedFilter: GameFilter,
    val sportSelect: SportSelection,
    val selectionList: List<SportSelection>,
    val favoriteSports: Set<ProfileFavorite>,
    val loadedState: ApiResponse<List<GameCardData>>,
    val priceFilter: PriceFilter? = null,
    val locationFilter: LocationFilter? = null,
    val dateFilter: DateFilter? = null
) {
    private val games: List<GameCardData>
        get() = (loadedState as? ApiResponse.Success)?.data.orEmpty()

    //TODO: refactor filters to use flows - not best practice to expose original games list to the view
    val filteredGames: List<GameCardData>
        get() {
            val favoriteGenders = favoriteSports.map { it.gender.displayName }.toSet()
            return games.filter { game ->
                val matchesGender = when (selectedFilter) {
                    GameFilter.ALL -> true
                    GameFilter.FAVORITES -> if (favoriteSports.isNotEmpty()) {
                        game.gender in favoriteGenders
                    } else {
                        true
                    }

                    else -> game.gender == selectedFilter.displayName
                }
                val matchesSport = when (sportSelect) {
                    is SportSelection.SportSelect -> game.sport == sportSelect.sport.displayName
                    else -> true
                }
                val matchesLocation = when (locationFilter) {
                    LocationFilter.ON_CAMPUS -> game.isHome
                    else -> true
                }
                val matchesDate = when (dateFilter) {
                    DateFilter.TODAY -> game.date == LocalDate.now()
                    DateFilter.WITHIN_7_DAYS -> game.date?.isBefore(
                        LocalDate.now().plusDays(8)
                    ) == true && (game.date?.isAfter(LocalDate.now().minusDays(1)) == true)

                    DateFilter.WITHIN_A_MONTH -> game.date?.isBefore(
                        LocalDate.now().plusMonths(1)
                    ) == true && (game.date?.isAfter(LocalDate.now().minusDays(1)) == true)

                    DateFilter.OVER_A_MONTH -> game.date?.isAfter(
                        LocalDate.now().plusMonths(1)
                    ) == true

                    null -> true
                }
                matchesGender && matchesSport && matchesLocation && matchesDate
            }
        }

    val upcomingGames: List<GameCardData>
        get() = games.take(3)
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val scoreRepository: ScoreRepository,
    preferencesRepository: OnboardingPreferencesRepository
) : BaseViewModel<HomeUiState>(
    HomeUiState(
        selectedFilter = GameFilter.FAVORITES,
        sportSelect = SportSelection.All,
        selectionList = Sport.getSportSelectionList(GameFilter.FAVORITES),
        favoriteSports = preferencesRepository.getFavoriteSports(),
        loadedState = ApiResponse.Loading
    )
) {
    init {
        applyMutation {
            copy(selectionList = selectionListFor(selectedFilter, favoriteSports, emptyList()))
        }
        scoreRepository.fetchGames()
        asyncCollect(scoreRepository.upcomingGamesFlow) { response ->
            applyMutation {
                val loadedState = response.map { list ->
                    list.map { game ->
                        game.toGameCardData()
                    }.filter { game ->
                        game.date?.isAfter(LocalDate.now().minusDays(1)) ?: false
                    }.sortedBy { it.date }
                }
                copy(
                    loadedState = loadedState,
                    selectionList = selectionListFor(
                        selectedFilter,
                        favoriteSports,
                        (loadedState as? ApiResponse.Success)?.data.orEmpty()
                    )
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
        applyMutation { copy(sportSelect = sport) }
    }

    fun onApplyAdvancedFilters(
        priceFilter: PriceFilter?,
        locationFilter: LocationFilter?,
        dateFilter: DateFilter?
    ) {
        applyMutation {
            copy(
                priceFilter = priceFilter,
                locationFilter = locationFilter,
                dateFilter = dateFilter
            )
        }
    }

    fun onResetAdvancedFilters() {
        applyMutation {
            copy(
                priceFilter = null,
                locationFilter = null,
                dateFilter = null
            )
        }
    }

    private fun selectionListFor(
        filter: GameFilter,
        favorites: Set<ProfileFavorite>,
        upcomingGames: List<GameCardData>
    ): List<SportSelection> {
        val nextGameDateBySport = upcomingGames
            .groupBy { it.sport }
            .mapValues { (_, games) -> games.mapNotNull { it.date }.minOrNull() }

        val sports = if (filter == GameFilter.FAVORITES) {
            listOf(SportSelection.All) +
                    favorites.map { SportSelection.SportSelect(it.sport) }.distinct()
        } else {
            Sport.getSportSelectionList(filter)
        }

        return sports.sortedWith(
            compareBy<SportSelection> { if (it is SportSelection.All) 0 else 1 }
                .thenBy { selection ->
                    (selection as? SportSelection.SportSelect)
                        ?.sport
                        ?.displayName
                        ?.let(nextGameDateBySport::get)
                        ?: LocalDate.MAX
                }
                .thenBy { (it as? SportSelection.SportSelect)?.sport?.displayName.orEmpty() }
        )
    }
}
