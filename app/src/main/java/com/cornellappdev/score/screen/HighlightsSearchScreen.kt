package com.cornellappdev.score.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.cornellappdev.score.R
import com.cornellappdev.score.components.EmptyStateBox
import com.cornellappdev.score.components.ScorePreview
import com.cornellappdev.score.components.highlights.HighlightsCardLazyColumn
import com.cornellappdev.score.components.highlights.HighlightsScreenSearchFilterBar
import com.cornellappdev.score.components.highlights.RecentSearches
import com.cornellappdev.score.model.HighlightData
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.theme.Style.bodyNormal
import com.cornellappdev.score.theme.Style.heading2
import com.cornellappdev.score.util.highlightsList
import com.cornellappdev.score.util.recentSearchList
import com.cornellappdev.score.util.sportList

@Composable
fun HighlightsSearchScreen(
    sportList: List<Sport>,
    recentSearchList: List<String>,
    highlightsList: List<HighlightData>,
    query: String,
    header: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(top = 24.dp)
    ) {
        Row(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(text = header, style = heading2)
        }
        Spacer(modifier = Modifier.height(16.dp))
        HighlightsScreenSearchFilterBar(
            sportList
        )
        Spacer(modifier = Modifier.height(24.dp))
        Column(
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            if (recentSearchList.isNotEmpty() && query.isEmpty()) { //start state: no search attempted yet
                RecentSearches(recentSearchList)
            } else if (query.isNotEmpty()) { //todo: will pull this out to the viewmodel, just here for sanity check rn
                val filteredList = highlightsList.filter {
                    it.title.contains(query, ignoreCase = true)
                }
                if (filteredList.isEmpty()) {
                    EmptyStateBox(
                        icon = R.drawable.ic_kid_star,
                        title = "No results yet.",
                    )
                } else {
                    Text("${filteredList.size} Results", style = bodyNormal)
                    HighlightsCardLazyColumn(filteredList)
                }
            }
        }
    }
}

data class HighlightsSearchScreenPreviewData(
    val sportList: List<Sport>,
    val recentSearchList: List<String>,
    val query: String
)

class HighlightsSearchScreenPreviewProvider : PreviewParameterProvider<HighlightsSearchScreenPreviewData> {
    override val values: Sequence<HighlightsSearchScreenPreviewData> = sequence {
        yield(HighlightsSearchScreenPreviewData(sportList, recentSearchList, ""))
        yield(HighlightsSearchScreenPreviewData(sportList, recentSearchList, "Sports"))
        yield(HighlightsSearchScreenPreviewData(sportList, recentSearchList, "Hockey"))
    }
}

@Preview(showBackground = true)
@Composable
private fun HighlightScreenPreview(
    @PreviewParameter(HighlightsSearchScreenPreviewProvider::class) previewData: HighlightsSearchScreenPreviewData
) {
    ScorePreview {
        HighlightsSearchScreen(
            sportList = previewData.sportList,
            recentSearchList = previewData.recentSearchList,
            highlightsList = highlightsList,
            query = previewData.query,
            header = "Search All Highlights"
        )
    }
}