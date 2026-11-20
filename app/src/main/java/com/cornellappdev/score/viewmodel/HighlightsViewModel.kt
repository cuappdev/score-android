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


private data class FilteredHighlightsByDateBlock(
    val filteredHighlights: List<HighlightData>,
    val todayHighlights: List<HighlightData>,
    val pastThreeDaysHighlights: List<HighlightData>
)

private fun filterAndGroupHighlightsByDateBlock(
    highlights: List<HighlightData>,
    sportSelect: SportSelection,
    query: String
): FilteredHighlightsByDateBlock {
    val today = LocalDate.now()
    val threeDaysAgo = today.minusDays(3)

    val highlightsFilteredBySportSelection = highlights
        .filter { it.date != null }
        .filter { highlight ->
            when (sportSelect) {
                is SportSelection.All -> true
                is SportSelection.SportSelect -> highlight.sport == sportSelect.sport
            }
        }

    val filteredByQuery =
        if (query.isBlank()) highlightsFilteredBySportSelection
        else highlightsFilteredBySportSelection.filter {
            it.title.contains(query, ignoreCase = true) ||
                    (it.sport?.displayName?.contains(query, ignoreCase = true) == true)
        }

    val sorted = filteredByQuery.sortedByDescending { it.date }

    val todayHighlights = sorted.filter { it.date == today }

    val pastThreeDays = sorted.filter { date ->
        date.date?.let {
            it in threeDaysAgo..<today
        } ?: false
    }

    return FilteredHighlightsByDateBlock(
        filteredHighlights = sorted,
        todayHighlights = todayHighlights,
        pastThreeDaysHighlights = pastThreeDays
    )
}

private fun searchUiState(query: String, results: List<HighlightData>): SearchUiState =
    when {
        query.isEmpty() -> SearchUiState.RECENT
        results.isEmpty() -> SearchUiState.EMPTY
        else -> SearchUiState.RESULTS
    }

private fun refreshFilteredHighlights(highlights: List<HighlightData>, state: HighlightsUiState)
        : HighlightsUiState {

    val groupedHighlights =
        filterAndGroupHighlightsByDateBlock(highlights, state.sportSelect, state.query)

    return state.copy(
        allSearchSection = HighlightsSearchSectionUiState(
            header = "Search all highlights",
            results = groupedHighlights.filteredHighlights,
            searchUiState = searchUiState(state.query, groupedHighlights.filteredHighlights)
        ),
        todaySearchSection = HighlightsSearchSectionUiState(
            header = "Search today",
            results = groupedHighlights.todayHighlights,
            searchUiState = searchUiState(state.query, groupedHighlights.todayHighlights)
        ),
        pastThreeDaysSearchSection = HighlightsSearchSectionUiState(
            header = "Search past 3 days",
            results = groupedHighlights.pastThreeDaysHighlights,
            searchUiState = searchUiState(state.query, groupedHighlights.pastThreeDaysHighlights)
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
                        copy(
                            loadedState = ApiResponse.Success(sorted),
                            isRefreshing = false
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
                                loadedState = ApiResponse.Loading,
                            ).let { loadingState ->
                                refreshFilteredHighlights(
                                    emptyList(),
                                    loadingState
                                )
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
                                refreshFilteredHighlights(
                                    emptyList(),
                                    errorState
                                )
                            }
                        }
                }
            }
        }
    }

    fun onQueryChange(newQuery: String) {
        applyMutation {
            val highlights = (loadedState as? ApiResponse.Success)?.data.orEmpty()

            refreshFilteredHighlights(
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

    fun onSportToggled(sport: SportSelection) {
        applyMutation {
            val highlights =
                (loadedState as? ApiResponse.Success)?.data.orEmpty()

            val newSelection =
                if (sportSelect == sport) {
                    SportSelection.All
                } else {
                    sport
                }

            refreshFilteredHighlights(
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

            refreshFilteredHighlights(
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
