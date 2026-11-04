package com.cornellappdev.score.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.score.R
import com.cornellappdev.score.components.EmptyStateBox
import com.cornellappdev.score.components.ErrorState
import com.cornellappdev.score.components.highlights.HighlightsLoadingScreen
import com.cornellappdev.score.components.LoadingScreen
import com.cornellappdev.score.components.ScorePreview
import com.cornellappdev.score.components.ScorePullToRefreshBox
import com.cornellappdev.score.components.highlights.HighlightsCardRow
import com.cornellappdev.score.components.highlights.HighlightsFilterRow
import com.cornellappdev.score.components.highlights.HighlightsSearchEntryPointRow
import com.cornellappdev.score.model.ApiResponse
import com.cornellappdev.score.model.HighlightData
import com.cornellappdev.score.model.SportSelection
import com.cornellappdev.score.theme.Style.heading1
import com.cornellappdev.score.util.highlightsList
import com.cornellappdev.score.util.sportSelectionList
import com.cornellappdev.score.viewmodel.HighlightsViewModel
import kotlinx.serialization.Serializable

@Composable
fun HighlightsScreen(
    highlightsViewModel: HighlightsViewModel = hiltViewModel(),
    toSearchScreen: () -> Unit,
    toSubScreen: (HighlightsSubScreenType) -> Unit
) {
    val uiState = highlightsViewModel.collectUiStateValue()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(top = 24.dp)
    ) {
        when (uiState.loadedState) {
            is ApiResponse.Loading -> {
                HighlightsLoadingScreen("Loading Highlights...")
            }

            is ApiResponse.Error -> {
                ErrorState({ highlightsViewModel.onRefresh() }, "Oops! Highlights failed to load.")
            }

            is ApiResponse.Success -> {
                ScorePullToRefreshBox(
                    isRefreshing = uiState.isRefreshing,
                    { highlightsViewModel.onRefresh() }
                ) {
                    HighlightsScreenContent(
                        selectedSport = uiState.sportSelect,
                        sportList = uiState.sportSelectionList,
                        onSportSelected = { highlightsViewModel.onSportSelected(it) },
                        todayHighlightsList = uiState.todayHighlights,
                        pastThreeHighlightsList = uiState.pastThreeDaysHighlights,
                        toSearchScreen = toSearchScreen,
                        toSubScreen = toSubScreen
                    )
                }
            }
        }
    }
}

@Serializable
enum class HighlightsSubScreenType {
    TODAY, PAST3DAYS, ALL
}

@Composable
private fun HighlightsScreenContent(
    selectedSport: SportSelection,
    onSportSelected: (SportSelection) -> Unit,
    sportList: List<SportSelection>,
    todayHighlightsList: List<HighlightData>,
    pastThreeHighlightsList: List<HighlightData>,
    toSearchScreen: () -> Unit,
    toSubScreen: (HighlightsSubScreenType) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Text("Highlights", style = heading1)
            Spacer(modifier = Modifier.height(12.dp))
            HighlightsSearchEntryPointRow(toSearchScreen)
        }
        Spacer(modifier = Modifier.height(16.dp))
        HighlightsFilterRow(sportList, selectedSport, onSportSelected)
        Spacer(modifier = Modifier.height(24.dp))
        if (todayHighlightsList.isEmpty() && pastThreeHighlightsList.isEmpty()) {
            EmptyStateBox(
                icon = R.drawable.ic_kid_star,
                title = "No results yet.",
            )
        }
        if (todayHighlightsList.isNotEmpty()) {
            HighlightsCardRow(todayHighlightsList, "Today", toSubScreen)
        }
        if (pastThreeHighlightsList.isNotEmpty()) {
            HighlightsCardRow(pastThreeHighlightsList, "Past 3 days", toSubScreen)
        }
    }
}


data class HighlightsScreenPreviewData(
    val sportList: List<SportSelection>,
    val todayHighlightList: List<HighlightData>,
    val pastHighlightList: List<HighlightData>
)

class HighlightsScreenPreviewProvider : PreviewParameterProvider<HighlightsScreenPreviewData> {
    override val values: Sequence<HighlightsScreenPreviewData> = sequence {
        yield(HighlightsScreenPreviewData(sportSelectionList, highlightsList, highlightsList))
        yield(HighlightsScreenPreviewData(sportSelectionList, emptyList(), emptyList()))
        yield(HighlightsScreenPreviewData(sportSelectionList, emptyList(), highlightsList))
    }
}

@Preview(showBackground = true)
@Composable
private fun HighlightScreenPreview(
    @PreviewParameter(HighlightsScreenPreviewProvider::class) previewData: HighlightsScreenPreviewData
) {
    ScorePreview {
        HighlightsScreenContent(
            selectedSport = SportSelection.All,
            sportList = previewData.sportList,
            todayHighlightsList = previewData.todayHighlightList,
            pastThreeHighlightsList = previewData.pastHighlightList,
            toSearchScreen = {},
            onSportSelected = {},
            toSubScreen = {})
    }
}