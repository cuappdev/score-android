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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.R
import com.cornellappdev.score.components.EmptyStateBox
import com.cornellappdev.score.components.highlights.HighlightsCardLazyColumn
import com.cornellappdev.score.components.highlights.HighlightsScreenSearchFilterBar
import com.cornellappdev.score.components.highlights.RecentSearches
import com.cornellappdev.score.model.HighlightData
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.theme.Style.bodyNormal
import com.cornellappdev.score.theme.Style.heading2
import com.cornellappdev.score.util.highlightsList
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
        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        Row(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(text = header, style = heading2)
        }
        Spacer(modifier = Modifier.height(16.dp))
        HighlightsScreenSearchFilterBar(
            sportList,
            true,
            focusRequester = focusRequester
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
                    HighlightsCardLazyColumn(filteredList)
                }
            }
        }
    }
}

@Preview
@Composable
private fun HighlightsSearchScreenStartStatePreview() {
    HighlightsSearchScreen(
        sportList,
        listOf("Columbia", "Men's ice hockey", "Late goal lifts No.6 men’s hockey"),
        highlightsList,
        "",
        "Search All Highlights"
    )
}

@Preview
@Composable
private fun HighlightsSearchScreenQueryMissPreview() {
    HighlightsSearchScreen(
        sportList,
        listOf("Columbia", "Men's ice hockey", "Late goal lifts No.6 men’s hockey"),
        highlightsList,
        "Sports",
        "Search All Highlights"
    )
}

@Preview
@Composable
private fun HighlightsSearchScreenQueryHitPreview() {
    HighlightsSearchScreen(
        sportList,
        listOf("Columbia", "Men's ice hockey", "Late goal lifts No.6 men’s hockey"),
        highlightsList,
        "Hockey",
        "Search All Highlights"
    )
}
