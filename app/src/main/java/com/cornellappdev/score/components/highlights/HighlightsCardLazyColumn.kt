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

sealed interface SearchResultsState {
    data object Recent : SearchResultsState
    data class Results(val items: List<HighlightData>) : SearchResultsState
    data object Empty : SearchResultsState
}

@Composable
fun HighlightsCardLazyColumn(
    recentSearchList: List<String>,
    query: String,
    highlightsList: List<HighlightData>,
    onItemClick: () -> Unit,
    onCloseClick: () -> Unit,
    numResultsHeader: (@Composable () -> Unit)? = null
) {

    Column(
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        /*todo: move to VM*/
        val resultsState: SearchResultsState =
            when {
                recentSearchList.isNotEmpty() && query.isEmpty() ->
                    SearchResultsState.Recent

                query.isNotEmpty() -> {
                    val filtered = highlightsList.filter {
                        it.title.contains(query, ignoreCase = true)
                    }

                    if (filtered.isEmpty()) {
                        SearchResultsState.Empty
                    } else {
                        SearchResultsState.Results(filtered)
                    }
                }

                else -> SearchResultsState.Recent
            }


        AnimatedContent(
            targetState = resultsState,
            transitionSpec = {
                (fadeIn() + slideInVertically { it / 8 }) togetherWith
                        (fadeOut() + slideOutVertically { -it / 8 })
            },
            label = "SearchResultsAnimation"
        ) { state ->

            when (state) {
                SearchResultsState.Recent -> {
                    RecentSearches(
                        recentSearchList,
                        onItemClick,
                        onCloseClick
                    )
                }

                SearchResultsState.Empty -> {
                    EmptyStateBox(
                        icon = R.drawable.ic_kid_star,
                        title = "No results yet."
                    )
                }

                is SearchResultsState.Results -> {
                    Column {
                        numResultsHeader?.invoke()

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(state.items) { item ->
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
    size: Int
) {
    Column {
        Text("$size Results", style = bodyNormal)
        Spacer(Modifier.height(16.dp))
    }
}

@Preview
@Composable
private fun HighlightsCardLazyColumnSubScreenPreview() {
    ScorePreview {
        HighlightsCardLazyColumn(recentSearchList, "", highlightsList, {}, {})
    }
}

@Preview
@Composable
private fun HighlightsCardLazyColumnSearchResultsPreview() {
    ScorePreview {
        HighlightsCardLazyColumn(
            recentSearchList, "hockey", highlightsList, {}, {},
            { HighlightsCardLazyColumnResultsHeader(highlightsList.size) })
    }
}