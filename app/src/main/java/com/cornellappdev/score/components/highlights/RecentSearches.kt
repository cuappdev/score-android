package com.cornellappdev.score.components.highlights

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.R
import com.cornellappdev.score.components.ScorePreview
import com.cornellappdev.score.theme.Style.bodyMedium
import com.cornellappdev.score.theme.Style.metricSmallNormal


@Composable
private fun RecentSearchItem(
    query: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {/*search the query*/ })
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.search),
                contentDescription = "search icon",
                tint = Color.Unspecified
            )
            Text(query, style = metricSmallNormal)
        }

        IconButton(
            onClick = {/*delete this search from the recent searches list*/ }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = "close icon",
                tint = Color.Unspecified,
            )
        }
    }
}

@Composable
fun RecentSearches(
    recentQueriesList: List<String>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Recent searches", style = bodyMedium)
        recentQueriesList.forEach { query ->
            RecentSearchItem(query)
        }
    }
}

@Preview
@Composable
private fun RecentSearchItemPreview() {
    ScorePreview {
        RecentSearches(listOf("Columbia", "Men's ice hockey", "Late goal lifts No.6 men’s hockey"))
    }
}
