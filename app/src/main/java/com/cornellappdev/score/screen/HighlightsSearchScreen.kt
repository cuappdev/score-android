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
import com.cornellappdev.score.model.HighlightData
import com.cornellappdev.score.model.SportSelection
import com.cornellappdev.score.theme.Style.heading2
import com.cornellappdev.score.util.highlightsList
import com.cornellappdev.score.util.recentSearchList
import com.cornellappdev.score.util.sportSelectionList
import com.cornellappdev.score.viewmodel.HighlightsViewModel


@Composable
fun HighlightsSearchScreen(
    highlightsViewModel: HighlightsViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    searchScreenType: HighlightsSubScreenType
) {
    val uiState = highlightsViewModel.collectUiStateValue()

    val header = when (searchScreenType) {
        HighlightsSubScreenType.TODAY -> "Search today"
        HighlightsSubScreenType.PAST3DAYS -> "Search past 3 days"
        HighlightsSubScreenType.ALL -> "Search all highlights"
    }

    HighlightsSearchScreenContent(
        sportList = uiState.sportSelectionList,
        onFilterSelected = { highlightsViewModel.onSportSelected(it) },
        recentSearchList = uiState.recentSearches,
        filteredResults = uiState.filteredHighlights,
        query = uiState.query,
        selectedFilter = uiState.sportSelect,
        onQueryChange = { highlightsViewModel.onQueryChange(it) },
        onSearch = { highlightsViewModel.onSearch(it) },
        header = header,
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
    filteredResults: List<HighlightData>,
    query: String,
    selectedFilter: SportSelection,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    header: String,
    onItemClick: (String) -> Unit,
    onCloseClick: (String) -> Unit,
    navigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(top = 24.dp)
    ) {
        Text(text = header, style = heading2, modifier = Modifier.padding(horizontal = 24.dp))

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
            recentSearchList,
            query,
            filteredResults,
            onItemClick,
            onCloseClick,
            { HighlightsCardLazyColumnResultsHeader(highlightsList.size) })
    }
}

data class HighlightsSearchScreenPreviewData(
    val sportList: List<SportSelection>, val recentSearchList: List<String>, val query: String
)

class HighlightsSearchScreenPreviewProvider :
    PreviewParameterProvider<HighlightsSearchScreenPreviewData> {
    override val values: Sequence<HighlightsSearchScreenPreviewData> = sequence {
        yield(HighlightsSearchScreenPreviewData(sportSelectionList, recentSearchList, ""))
        yield(HighlightsSearchScreenPreviewData(sportSelectionList, recentSearchList, "Sports"))
        yield(HighlightsSearchScreenPreviewData(sportSelectionList, recentSearchList, "Hockey"))
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
            filteredResults = highlightsList,
            query = previewData.query,
            selectedFilter = SportSelection.All,
            onQueryChange = {},
            onSearch = {},
            header = "Search All Highlights",
            onItemClick = {},
            onCloseClick = {},
            navigateBack = {})
    }
}