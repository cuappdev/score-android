package com.cornellappdev.score.viewmodel

import com.cornellappdev.score.model.ApiResponse
import com.cornellappdev.score.model.GenderDivision
import com.cornellappdev.score.model.HighlightData
import com.cornellappdev.score.model.HighlightsRepository
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.model.SportSelection
import com.cornellappdev.score.model.map
import com.cornellappdev.score.util.parseIsoDateToLocalDateOrNull
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

data class HighlightsUiState(
    val sportSelect: SportSelection,
    val loadedState: ApiResponse<List<HighlightData>>,
    val sportSelectionList: List<SportSelection>
) {
    //TODO: refactor filters to use flows - not best practice to expose original games list to the view
    val filteredHighlights: List<HighlightData>
        get() = when (loadedState) {
            is ApiResponse.Success -> loadedState.data.filter { highlight ->
                (sportSelect is SportSelection.All ||
                        (sportSelect is SportSelection.SportSelect && highlight.sport == sportSelect.sport))
            }

            ApiResponse.Loading -> emptyList()
            ApiResponse.Error -> emptyList()
        }.sortedBy { it.date }

    val todayHighlights: List<HighlightData>
        get() = when (loadedState) {
            is ApiResponse.Success -> loadedState.data

            ApiResponse.Loading -> emptyList()
            ApiResponse.Error -> emptyList()
        }.filter { highlight ->
                parseIsoDateToLocalDateOrNull(highlight.date) == LocalDate.now()
        }.sortedBy { highlight ->
            parseIsoDateToLocalDateOrNull(highlight.date)
        }

    val pastThreeDaysHighlights: List<HighlightData>
        get() = when (loadedState) {
            is ApiResponse.Success -> loadedState.data

            ApiResponse.Loading -> emptyList()
            ApiResponse.Error -> emptyList()
        }.filter { highlight ->
            val date = parseIsoDateToLocalDateOrNull(highlight.date)
            date != null && !date.isBefore(LocalDate.now().minusDays(3))
        }.sortedBy { highlight ->
            parseIsoDateToLocalDateOrNull(highlight.date)
        }

}

@HiltViewModel
class HighlightsViewModel @Inject constructor(
    private val highlightsRepository: HighlightsRepository
) : BaseViewModel<HighlightsUiState>(
    HighlightsUiState(
        sportSelect = SportSelection.All,
        loadedState = ApiResponse.Loading,
        sportSelectionList = Sport.getSportSelectionList(GenderDivision.ALL)
    )
) {
    init {
        highlightsRepository.fetchHighlights()
        asyncCollect(highlightsRepository.highlightsFlow) { response ->
            applyMutation {
                copy(
                    loadedState = response.map { highlights ->
                        highlights
                            .sortedByDescending { highlight ->
                                when (highlight) {
                                    is HighlightData.Video -> highlight.data.date
                                    is HighlightData.Article -> highlight.data.date
                                }
                            }
                    }
                )
            }
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
            copy(
                sportSelect = sport
            )
        }
    }
}
