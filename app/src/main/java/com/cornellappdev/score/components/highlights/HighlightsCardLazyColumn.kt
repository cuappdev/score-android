package com.cornellappdev.score.components.highlights

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

@Composable
fun HighlightsCardLazyColumn(
    recentSearchList: List<String>,
    query: String,
    highlightsList: List<HighlightData>,
    numResultsHeader: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        if (recentSearchList.isNotEmpty() && query.isEmpty()) { //start state: no search attempted yet
            RecentSearches(recentSearchList)
        } else if (query.isNotEmpty()) { //filtering - will pull this out to the viewmodel when i do that, just here for sanity check rn
            val filteredList = highlightsList.filter {
                it.title.contains(query, ignoreCase = true)
            }
            if (filteredList.isEmpty()) {
                EmptyStateBox(
                    icon = R.drawable.ic_kid_star,
                    title = "No results yet.",
                )
            } else {
                numResultsHeader?.invoke()
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(highlightsList) { item ->
                        when (item) {
                            is HighlightData.Video -> VideoHighlightCard(item.data, true)
                            is HighlightData.Article -> ArticleHighlightCard(item.data, true)
                        }
                    }
                }
            }
        }
    }
}

/* Used to display number of results in on HighlightsSearchScreen*/
@Composable
fun HighlightsCardLazyColumnNumResultsHeader(
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
        HighlightsCardLazyColumn(recentSearchList, "", highlightsList)
    }
}

@Preview
@Composable
private fun HighlightsCardLazyColumnSearchResultsPreview() {
    ScorePreview {
        HighlightsCardLazyColumn(
            recentSearchList, "", highlightsList,
            { HighlightsCardLazyColumnNumResultsHeader(highlightsList.size) })
    }
}