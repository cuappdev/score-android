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
    val isRefreshing: Boolean,
    val sportSelectionList: List<SportSelection>,
    val filteredHighlights: List<HighlightData>,
    val todayHighlights: List<HighlightData>,
    val pastThreeDaysHighlights: List<HighlightData>
)

private fun buildDerivedLists(
    highlights: List<HighlightData>,
    sportSelect: SportSelection
): Triple<List<HighlightData>, List<HighlightData>, List<HighlightData>> {

    val today = LocalDate.now()
    val threeDaysAgo = today.minusDays(3)

    // Keep only highlights with a date and matching the sport filter
    val validHighlights = highlights
        .filter { it.date != null }
        .filter { highlight ->
            when (sportSelect) {
                is SportSelection.All -> true
                is SportSelection.SportSelect ->
                    highlight.sport == sportSelect.sport
            }
        }

    val filtered = validHighlights.sortedBy { it.date }

    val todayHighlights = validHighlights.filter { it.date == today }

    val pastThreeDaysHighlights = validHighlights
        .filter { it.date!! >= threeDaysAgo } // null dates filtered out in line 32
        .sortedBy { it.date }

    return Triple(filtered, todayHighlights, pastThreeDaysHighlights)
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
        pastThreeDaysHighlights = emptyList()
    )
) {
    init {
        highlightsRepository.fetchHighlights()
        asyncCollect(highlightsRepository.highlightsFlow) { response ->
            applyMutation {
                when (response) {
                    is ApiResponse.Success -> {
                        val sorted =
                            response.data.sortedByDescending { it.date }

                        val (filtered, today, pastThreeDays) =
                            buildDerivedLists(sorted, sportSelect)

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

    fun onRefresh() {
        applyMutation {
            copy(isRefreshing = true)
        }
        highlightsRepository.fetchHighlights()
    }

    fun onSportSelected(sport: SportSelection) {
        applyMutation {
            val highlights = when (val state = loadedState) {
                is ApiResponse.Success -> state.data
                else -> emptyList()
            }

            val (filtered, today, pastThreeDays) =
                buildDerivedLists(highlights, sport)

            copy(
                sportSelect = sport,
                filteredHighlights = filtered,
                todayHighlights = today,
                pastThreeDaysHighlights = pastThreeDays
            )
        }
    }
}
