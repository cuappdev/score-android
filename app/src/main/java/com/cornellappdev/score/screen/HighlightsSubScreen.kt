package com.cornellappdev.score.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.R
import com.cornellappdev.score.components.EmptyStateBox
import com.cornellappdev.score.components.ScorePreview
import com.cornellappdev.score.components.highlights.ArticleHighlightCard
import com.cornellappdev.score.components.highlights.HighlightsScreenSearchFilterBar
import com.cornellappdev.score.components.highlights.RecentSearches
import com.cornellappdev.score.components.highlights.VideoHighlightCard
import com.cornellappdev.score.model.HighlightData
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.theme.Style.bodyNormal
import com.cornellappdev.score.theme.Style.heading2
import com.cornellappdev.score.theme.White
import com.cornellappdev.score.util.highlightsList
import com.cornellappdev.score.util.sportList

@Composable
fun HighlightsSubScreenHeader(
    header: String
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(96.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_left_arrowhead),
            contentDescription = "back arrow"
        )
        Text(header, style = heading2)
    }
}

@Preview
@Composable
private fun HighlightsSubScreenHeader() {
    ScorePreview {
        HighlightsSubScreenHeader("Past 3 Days")
    }
}

@Composable
fun HighlightsSubScreen(
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
        Surface(
            modifier = Modifier.wrapContentSize().dropShadow(
                shape = RectangleShape,
                shadow = Shadow(
                    radius = 2.dp,
                    color = Color.Black.copy(alpha = 0.05f),
                    offset = DpOffset(0.dp, (2).dp)
                )
            ),
            color = White
        ) {
            HighlightsSubScreenHeader(header)
        }
        Spacer(modifier = Modifier.height(16.dp))
        HighlightsScreenSearchFilterBar(
            sportList,

            false
        ) //need viewmodel to handle comms between composables
        Spacer(modifier = Modifier.height(24.dp))
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
                    Text("${filteredList.size} Results", style = bodyNormal)
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item { Spacer(Modifier.width(8.dp)) }
                        items(filteredList) { item ->
                            when (item) {
                                is HighlightData.Video -> VideoHighlightCard(item.data, true)
                                is HighlightData.Article -> ArticleHighlightCard(item.data, true)
                            }
                        }
                        item { Spacer(Modifier.width(8.dp)) }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

            }
        }
    }
}

@Preview
@Composable
private fun HighlightsSubScreenPreview() {
    HighlightsSubScreen(
        sportList = sportList,
        recentSearchList = listOf(
            "Columbia",
            "Men's ice hockey",
            "Late goal lifts No.6 men’s hockey"
        ),
        highlightsList = highlightsList,
        query = "hockey",
        header = "Past 3 Days"
    )
}