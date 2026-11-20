package com.cornellappdev.score.components.highlights

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.R
import com.cornellappdev.score.components.EmptyStateBox
import com.cornellappdev.score.components.ScorePreview
import com.cornellappdev.score.model.HighlightData
import com.cornellappdev.score.theme.Style.bodyNormal
import com.cornellappdev.score.util.highlightsList
import com.cornellappdev.score.util.recentSearchList
import com.cornellappdev.score.viewmodel.SearchUiState

@Composable
fun HighlightsCardLazyColumn(
    uiState: SearchUiState,
    recentSearchList: List<String>,
    filteredResults: List<HighlightData>,
    onItemClick: (String) -> Unit,
    onCloseClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    numResultsHeader: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = modifier.padding(horizontal = 24.dp)
    ) {

        AnimatedContent(
            targetState = uiState,
            transitionSpec = {
                (fadeIn() + slideInVertically { it / 8 }) togetherWith
                        (fadeOut() + slideOutVertically { -it / 8 })
            },
            label = "SearchResultsAnimation"
        ) { state ->

            when (state) {
                SearchUiState.RECENT -> {
                    RecentSearches(
                        recentSearchList,
                        onItemClick,
                        onCloseClick
                    )
                }

                SearchUiState.EMPTY -> {
                    EmptyStateBox(
                        icon = R.drawable.ic_kid_star,
                        title = "No results yet."
                    )
                }

                SearchUiState.RESULTS -> {
                    Column {
                        numResultsHeader?.invoke()

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(filteredResults) { item ->
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
            }
        }
    }
}

/* Used to display number of results in on HighlightsSearchScreen*/
@Composable
fun HighlightsCardLazyColumnResultsHeader(
    size: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text("$size Results", style = bodyNormal)
        Spacer(Modifier.height(16.dp))
    }
}

@Preview
@Composable
private fun HighlightsCardLazyColumnSubScreenPreview() {
    ScorePreview {
        HighlightsCardLazyColumn(
            uiState = SearchUiState.RECENT,
            recentSearchList = recentSearchList,
            filteredResults = highlightsList,
            onItemClick = {},
            onCloseClick = {}
        )
    }
}

@Preview
@Composable
private fun HighlightsCardLazyColumnSearchResultsPreview() {
    ScorePreview {
        HighlightsCardLazyColumn(
            uiState = SearchUiState.RESULTS,
            recentSearchList = recentSearchList,
            filteredResults = highlightsList,
            onItemClick = {},
            onCloseClick = {},
            numResultsHeader = { HighlightsCardLazyColumnResultsHeader(highlightsList.size) }
        )
    }
}