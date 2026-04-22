package com.cornellappdev.score.viewmodel

import com.cornellappdev.score.model.ApiResponse
import com.cornellappdev.score.model.GenderDivision
import com.cornellappdev.score.model.HighlightData
import com.cornellappdev.score.model.HighlightsRepository
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.model.SportSelection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import java.time.LocalDate
import javax.inject.Inject

data class HighlightsUiState(
    val sportSelect: SportSelection,
    val loadedState: ApiResponse<List<HighlightData>>,
    val isRefreshing: Boolean,
    val sportSelectionList: List<SportSelection>,
    val filteredHighlights: List<HighlightData>,
    val todayHighlights: List<HighlightData>,
    val pastThreeDaysHighlights: List<HighlightData>,
    val query: String,
    val recentSearches: List<String>
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

private fun recompute(highlights: List<HighlightData>, state: HighlightsUiState)
        : HighlightsUiState {

    val (filtered, today, pastThreeDays) =
        buildDerivedLists(highlights, state.sportSelect, state.query)

    return state.copy(
        filteredHighlights = filtered,
        todayHighlights = today,
        pastThreeDaysHighlights = pastThreeDays
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
        filteredHighlights = emptyList(),
        todayHighlights = emptyList(),
        pastThreeDaysHighlights = emptyList(),
        query = "",
        recentSearches = emptyList()
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
                            isRefreshing = false,
                            filteredHighlights = filtered,
                            todayHighlights = today,
                            pastThreeDaysHighlights = pastThreeDays
                        )
                    }

                    ApiResponse.Loading ->
                        if (loadedState is ApiResponse.Success) {
                            copy(
                                isRefreshing = true
                            )
                        } else {
                            copy(isRefreshing = true,
                                loadedState = ApiResponse.Loading,
                                filteredHighlights = emptyList(),
                                todayHighlights = emptyList(),
                                pastThreeDaysHighlights = emptyList()
                            )
                        }

                    ApiResponse.Error ->
                        if (loadedState is ApiResponse.Success) {
                            copy(
                                isRefreshing = false
                            )
                        } else {
                            copy(
                                loadedState = ApiResponse.Error,
                                isRefreshing = false,
                                filteredHighlights = emptyList(),
                                todayHighlights = emptyList(),
                                pastThreeDaysHighlights = emptyList()
                            )
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
