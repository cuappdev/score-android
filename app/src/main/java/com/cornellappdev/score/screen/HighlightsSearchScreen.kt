package com.cornellappdev.score.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.score.components.ScorePreview
import com.cornellappdev.score.components.highlights.HighlightsCardLazyColumn
import com.cornellappdev.score.components.highlights.HighlightsCardLazyColumnResultsHeader
import com.cornellappdev.score.components.highlights.HighlightsScreenSearchFilterBar
import com.cornellappdev.score.model.SportSelection
import com.cornellappdev.score.theme.Style.heading2
import com.cornellappdev.score.util.highlightsList
import com.cornellappdev.score.util.recentSearchList
import com.cornellappdev.score.util.sportSelectionList
import com.cornellappdev.score.viewmodel.HighlightsSearchSectionUiState
import com.cornellappdev.score.viewmodel.HighlightsViewModel
import com.cornellappdev.score.viewmodel.SearchUiState

@Composable
fun HighlightsSearchScreen(
    navigateBack: () -> Unit,
    searchScreenType: HighlightsSubScreenType,
    highlightsViewModel: HighlightsViewModel = hiltViewModel()
) {
    val uiState = highlightsViewModel.collectUiStateValue()

    val sectionUiState = when (searchScreenType) {
        HighlightsSubScreenType.TODAY -> uiState.todaySearchSection
        HighlightsSubScreenType.PAST3DAYS -> uiState.pastThreeDaysSearchSection
        HighlightsSubScreenType.ALL -> uiState.allSearchSection
    }

    HighlightsSearchScreenContent(
        sportList = uiState.sportSelectionList,
        onFilterSelected = { highlightsViewModel.onSportToggled(it) },
        recentSearchList = uiState.recentSearches,
        sectionUiState = sectionUiState,
        query = uiState.query,
        selectedFilter = uiState.sportSelect,
        onQueryChange = { highlightsViewModel.onQueryChange(it) },
        onSearch = { highlightsViewModel.onSearch(it) },

        onItemClick = { highlightsViewModel.onSearchRecent(it) },
        onCloseClick = { highlightsViewModel.onRemoveRecent(it) },
        navigateBack = navigateBack
    )
}

@Composable
fun HighlightsSearchScreenContent(
    sportList: List<SportSelection>,
    onFilterSelected: (SportSelection) -> Unit,
    recentSearchList: List<String>,
    sectionUiState: HighlightsSearchSectionUiState,
    query: String,
    selectedFilter: SportSelection,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onItemClick: (String) -> Unit,
    onCloseClick: (String) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(top = 24.dp)
    ) {
        Text(
            text = sectionUiState.header,
            style = heading2,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        HighlightsScreenSearchFilterBar(
            sportList,
            query,
            selectedFilter,
            onQueryChange,
            onSearch,
            onFilterSelected,
            navigateBack
        )
        Spacer(modifier = Modifier.height(24.dp))
        HighlightsCardLazyColumn(
            uiState = sectionUiState.searchUiState,
            recentSearchList = recentSearchList,
            filteredResults = sectionUiState.results,
            onItemClick = onItemClick,
            onCloseClick = onCloseClick,
            numResultsHeader = {
                HighlightsCardLazyColumnResultsHeader(sectionUiState.results.size)
            }
        )
    }
}

data class HighlightsSearchScreenPreviewData(
    val sportList: List<SportSelection>,
    val recentSearchList: List<String>,
    val query: String,
    val searchUiState: SearchUiState
)

class HighlightsSearchScreenPreviewProvider :
    PreviewParameterProvider<HighlightsSearchScreenPreviewData> {
    override val values: Sequence<HighlightsSearchScreenPreviewData> = sequence {
        yield(HighlightsSearchScreenPreviewData(sportSelectionList, recentSearchList, "", SearchUiState.RECENT))
        yield(HighlightsSearchScreenPreviewData(sportSelectionList, recentSearchList, "Sports", SearchUiState.RESULTS))
        yield(HighlightsSearchScreenPreviewData(sportSelectionList, recentSearchList, "Hockey", SearchUiState.EMPTY))
    }
}

@Preview(showBackground = true)
@Composable
private fun HighlightScreenPreview(
    @PreviewParameter(HighlightsSearchScreenPreviewProvider::class) previewData: HighlightsSearchScreenPreviewData
) {
    ScorePreview {
        HighlightsSearchScreenContent(
            sportList = previewData.sportList,
            onFilterSelected = {},
            recentSearchList = previewData.recentSearchList,
            sectionUiState = HighlightsSearchSectionUiState(
                header = "Search All Highlights",
                results = highlightsList,
                searchUiState = previewData.searchUiState
            ),
            query = previewData.query,
            selectedFilter = SportSelection.All,
            onQueryChange = {},
            onSearch = {},
            onItemClick = {},
            onCloseClick = {},
            navigateBack = {})
    }
}