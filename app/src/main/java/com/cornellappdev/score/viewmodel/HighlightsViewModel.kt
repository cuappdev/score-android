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

data class HighlightsUiState(
    val sportSelect: SportSelection,
    val loadedState: ApiResponse<List<HighlightData>>,
    val sportSelectionList: List<SportSelection>,
    val filteredHighlights: List<HighlightData>,
    val todayHighlights: List<HighlightData>,
    val pastThreeDaysHighlights: List<HighlightData>,
    val query: String
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
            it.sport?.displayName?.contains(query, ignoreCase = true) ?: false
        }

    val sorted = filteredByQuery.sortedByDescending { it.date }

    val todayHighlights = sorted.filter { it.date == today }

    val pastThreeDays = sorted.filter { it.date!! >= threeDaysAgo }

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
        sportSelectionList = Sport.getSportSelectionList(GenderDivision.ALL),
        filteredHighlights = emptyList(),
        todayHighlights = emptyList(),
        pastThreeDaysHighlights = emptyList(),
        query = ""
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
                                loadedState = ApiResponse.Success(sorted)
                            )
                        )
                    }

                    ApiResponse.Loading ->
                        copy(
                            loadedState = ApiResponse.Loading,
                            filteredHighlights = emptyList(),
                            todayHighlights = emptyList(),
                            pastThreeDaysHighlights = emptyList()
                        )

                    ApiResponse.Error ->
                        copy(
                            loadedState = ApiResponse.Error,
                            filteredHighlights = emptyList(),
                            todayHighlights = emptyList(),
                            pastThreeDaysHighlights = emptyList()
                        )
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
            copy(loadedState = ApiResponse.Loading)
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
}
