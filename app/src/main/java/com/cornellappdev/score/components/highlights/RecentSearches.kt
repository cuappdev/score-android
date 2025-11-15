package com.cornellappdev.score.components.highlights

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
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
fun RecentSearchItem(
    query: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.clickable(onClick = {/*search the query*/})
        ) {
            Icon(
                painter = painterResource(R.drawable.search),
                contentDescription = "search icon",
                tint = Color.Unspecified
            )
            Text(query, style = metricSmallNormal)
        }

        //this icon doesn't preview correctly since its too small, works when actually running though
        Icon(
            painter = painterResource(R.drawable.ic_close),
            contentDescription = "close icon",
            tint = Color.Unspecified,
            modifier = Modifier.clickable(onClick = {/*delete this search from the recent searches list*/})
        )

    }
}

@Composable
fun RecentSearches(
    recentQueriesList: List<String>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text("Recent searches", style = bodyMedium)
        recentQueriesList.map { query ->
            Spacer(Modifier.height(16.dp))
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
