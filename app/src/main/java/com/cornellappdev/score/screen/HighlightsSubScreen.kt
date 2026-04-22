package com.cornellappdev.score.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.score.R
import com.cornellappdev.score.components.ErrorState
import com.cornellappdev.score.components.ScorePreview
import com.cornellappdev.score.components.ScorePullToRefreshBox
import com.cornellappdev.score.components.highlights.ArticleHighlightCard
import com.cornellappdev.score.components.highlights.HighlightsFilterRow
import com.cornellappdev.score.components.highlights.HighlightsSearchEntryPointRow
import com.cornellappdev.score.components.highlights.SubHighlightsLoadingScreen
import com.cornellappdev.score.components.highlights.VideoHighlightCard
import com.cornellappdev.score.model.ApiResponse
import com.cornellappdev.score.model.HighlightData
import com.cornellappdev.score.model.SportSelection
import com.cornellappdev.score.theme.Style.heading2
import com.cornellappdev.score.theme.White
import com.cornellappdev.score.util.highlightsList
import com.cornellappdev.score.util.sportSelectionList
import com.cornellappdev.score.viewmodel.HighlightsViewModel

@Composable
fun HighlightsSubScreenHeader(
    header: String,
    navigateBack: () -> Unit
) {
    Surface(
        modifier = Modifier
            .wrapContentSize()
            .dropShadow(
                shape = RectangleShape,
                shadow = Shadow(
                    radius = 2.dp,
                    color = Color.Black.copy(alpha = 0.05f),
                    offset = DpOffset(0.dp, (2).dp)
                )
            ),
        color = White
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_left_arrowhead),
                contentDescription = "back arrow",
                modifier = Modifier
                    .clickable(onClick = { navigateBack() })
                    .align(Alignment.CenterStart)
            )
            Text(header, style = heading2, modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Preview
@Composable
private fun HighlightsSubScreenHeaderPreview() {
    ScorePreview {
        HighlightsSubScreenHeader("Past 3 Days", {})
    }
}

@Composable
fun HighlightsSubScreen(
    highlightsViewModel: HighlightsViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    toSearchScreen: () -> Unit,
    subScreenType: HighlightsSubScreenType
) {
    val uiState = highlightsViewModel.collectUiStateValue()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        val (highlightsList, header) = when (subScreenType) {
            HighlightsSubScreenType.TODAY ->
                uiState.todayHighlights to "Today"

            HighlightsSubScreenType.PAST3DAYS ->
                uiState.pastThreeDaysHighlights to "Past 3 Days"

            HighlightsSubScreenType.ALL ->
                uiState.filteredHighlights to "All highlights"
        }

        when (uiState.loadedState) {
            is ApiResponse.Loading -> {
                SubHighlightsLoadingScreen(header)
            }

            is ApiResponse.Error -> {
                ErrorState({ highlightsViewModel.onRefresh() }, "Oops! Highlights failed to load.")
            }

            is ApiResponse.Success -> {
                ScorePullToRefreshBox(
                    isRefreshing = uiState.loadedState == ApiResponse.Loading,
                    { highlightsViewModel.onRefresh() }
                ) {
                    HighlightsSubScreenContent(
                        selectedSport = uiState.sportSelect,
                        sportList = uiState.sportSelectionList,
                        onFilterSelected = { highlightsViewModel.onSportSelected(it) },
                        highlightsList = highlightsList,
                        header = header,
                        navigateBack = navigateBack,
                        toSearchScreen = toSearchScreen
                    )
                }
            }
        }
    }
}

@Composable
fun HighlightsSubScreenContent(
    selectedSport: SportSelection,
    sportList: List<SportSelection>,
    onFilterSelected: (SportSelection) -> Unit,
    highlightsList: List<HighlightData>,
    header: String,
    navigateBack: () -> Unit,
    toSearchScreen: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            //.padding(top = 24.dp)
    ) {
        HighlightsSubScreenHeader(header, navigateBack)
        Spacer(modifier = Modifier.height(24.dp))
        Column(
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            HighlightsSearchEntryPointRow({ toSearchScreen() })
        }
        Spacer(modifier = Modifier.height(16.dp))
        HighlightsFilterRow(
            sportList,
            selectedSport = selectedSport,
            onFilterSelected = onFilterSelected
        )


        Spacer(modifier = Modifier.height(24.dp))
        LazyColumn(
            Modifier.padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(highlightsList) { item ->
                when (item) {
                    is HighlightData.Video ->
                        VideoHighlightCard(item.data, true)

                    is HighlightData.Article ->
                        ArticleHighlightCard(item.data, true)
                }
            }
        }
    }
}

@Preview
@Composable
private fun HighlightsSubScreenPreview() {
    HighlightsSubScreenContent(
        selectedSport = SportSelection.All,
        sportList = sportSelectionList,
        onFilterSelected = {},
        highlightsList = highlightsList,
        header = "Past 3 Days",
        {}, {}
    )
}