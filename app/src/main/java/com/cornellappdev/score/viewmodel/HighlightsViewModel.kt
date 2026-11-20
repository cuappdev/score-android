package com.cornellappdev.score.viewmodel

import com.cornellappdev.score.model.ApiResponse
import com.cornellappdev.score.model.GenderDivision
import com.cornellappdev.score.model.HighlightData
import com.cornellappdev.score.model.HighlightsRepository
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.model.SportSelection
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

enum class SearchUiState { RECENT, EMPTY, RESULTS }

data class HighlightsSearchSectionUiState(
    val header: String,
    val results: List<HighlightData>,
    val searchUiState: SearchUiState
)

data class HighlightsUiState(
    val sportSelect: SportSelection,
    val loadedState: ApiResponse<List<HighlightData>>,
    val isRefreshing: Boolean,
    val sportSelectionList: List<SportSelection>,
    val query: String,
    val recentSearches: List<String>,
    val allSearchSection: HighlightsSearchSectionUiState,
    val todaySearchSection: HighlightsSearchSectionUiState,
    val pastThreeDaysSearchSection: HighlightsSearchSectionUiState
)

private fun buildDerivedLists(
    highlights: List<HighlightData>,
    sportSelect: SportSelection,
    query: String
): Triple<List<HighlightData>, List<HighlightData>, List<HighlightData>> {
    val today = LocalDate.now()
    val threeDaysAgo = today.minusDays(3)

    val filteredBySport = highlights
        .filter { it.date != null }
        .filter { h ->
            when (sportSelect) {
                is SportSelection.All -> true
                is SportSelection.SportSelect -> h.sport == sportSelect.sport
            }
        }

    val filteredByQuery =
        if (query.isBlank()) filteredBySport
        else filteredBySport.filter {
            it.title.contains(query, ignoreCase = true) ||
                    (it.sport?.displayName?.contains(query, ignoreCase = true) == true)
        }

    val sorted = filteredByQuery.sortedByDescending { it.date }

    val todayHighlights = sorted.filter { it.date == today }

    val pastThreeDays = sorted.filter { date ->
        date.date?.let {
            it < today && it >= threeDaysAgo
        } ?: false
    }

    return Triple(sorted, todayHighlights, pastThreeDays)
}

private fun searchUiState(query: String, results: List<HighlightData>): SearchUiState =
    when {
        query.isEmpty() -> SearchUiState.RECENT
        results.isEmpty() -> SearchUiState.EMPTY
        else -> SearchUiState.RESULTS
    }

private fun recompute(highlights: List<HighlightData>, state: HighlightsUiState)
        : HighlightsUiState {

    val (filtered, today, pastThreeDays) =
        buildDerivedLists(highlights, state.sportSelect, state.query)

    return state.copy(
        allSearchSection = HighlightsSearchSectionUiState(
            header = "Search all highlights",
            results = filtered,
            searchUiState = searchUiState(state.query, filtered)
        ),
        todaySearchSection = HighlightsSearchSectionUiState(
            header = "Search today",
            results = today,
            searchUiState = searchUiState(state.query, today)
        ),
        pastThreeDaysSearchSection = HighlightsSearchSectionUiState(
            header = "Search past 3 days",
            results = pastThreeDays,
            searchUiState = searchUiState(state.query, pastThreeDays)
        )
    )
}

@HiltViewModel
class HighlightsViewModel @Inject constructor(
    private val highlightsRepository: HighlightsRepository
) : BaseViewModel<HighlightsUiState>(
    HighlightsUiState(
        sportSelect = SportSelection.All,
        loadedState = ApiResponse.Loading,
        isRefreshing = true,
        sportSelectionList = Sport.getSportSelectionList(GenderDivision.ALL),
        query = "",
        recentSearches = emptyList(),
        allSearchSection = HighlightsSearchSectionUiState(
            "Search all highlights", emptyList(), SearchUiState.RECENT
        ),
        todaySearchSection = HighlightsSearchSectionUiState(
            "Search today", emptyList(), SearchUiState.RECENT
        ),
        pastThreeDaysSearchSection = HighlightsSearchSectionUiState(
            "Search past 3 days", emptyList(), SearchUiState.RECENT
        )
    )
) {
    init {
        highlightsRepository.fetchHighlights()
        asyncCollect(highlightsRepository.highlightsFlow) { response ->
            applyMutation {
                when (response) {
                    is ApiResponse.Success -> {
                        val sorted = response.data.sortedByDescending { it.date }

                        recompute(
                            sorted,
                            copy(
                                loadedState = ApiResponse.Success(sorted),
                                isRefreshing = false
                            )
                        )
                    }

                    ApiResponse.Loading ->
                        if (loadedState is ApiResponse.Success) {
                            copy(
                                isRefreshing = true
                            )
                        } else {
                            copy(
                                isRefreshing = true,
                                loadedState = ApiResponse.Loading
                            ).let { loadingState ->
                                recompute(emptyList(), loadingState)
                            }
                        }

                    ApiResponse.Error ->
                        if (loadedState is ApiResponse.Success) {
                            copy(
                                isRefreshing = false
                            )
                        } else {
                            copy(
                                loadedState = ApiResponse.Error,
                                isRefreshing = false
                            ).let { errorState ->
                                recompute(emptyList(), errorState)
                            }
                        }
                }
            }
        }
    }

    fun onQueryChange(newQuery: String) {
        applyMutation {
            val highlights = (loadedState as? ApiResponse.Success)?.data.orEmpty()

            recompute(
                highlights,
                copy(query = newQuery)
            )
        }
    }

    fun onRefresh() {
        applyMutation {
            copy(isRefreshing = true)
        }
        highlightsRepository.fetchHighlights()
    }

    fun onSportSelected(sport: SportSelection) {
        applyMutation {
            val highlights =
                (loadedState as? ApiResponse.Success)?.data.orEmpty()

            val newSelection =
                if (sportSelect == sport) {
                    SportSelection.All
                } else {
                    sport
                }

            recompute(
                highlights,
                copy(sportSelect = newSelection)
            )
        }
    }

    fun onSearch(query: String) {
        if (query.isBlank()) return

        applyMutation {
            val highlights =
                (loadedState as? ApiResponse.Success)?.data.orEmpty()

            val updatedSearches =
                (listOf(query) + recentSearches)
                    .distinct()
                    .take(3)

            recompute(
                highlights,
                copy(
                    query = query,
                    recentSearches = updatedSearches
                )
            )
        }
    }

    //Removes an item from the recent searches list
    fun onRemoveRecent(recent: String) {
        applyMutation {
            val updated = recentSearches.filterNot { it == recent }
            copy(recentSearches = updated)
        }
    }

    //Searches an item from the recent searches list
    fun onSearchRecent(recent: String) {
        onSearch(recent)
    }
}
