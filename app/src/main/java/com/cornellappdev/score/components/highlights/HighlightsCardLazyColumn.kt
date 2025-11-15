package com.cornellappdev.score.components.highlights

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.components.ScorePreview
import com.cornellappdev.score.model.HighlightData
import com.cornellappdev.score.util.highlightsList

@Composable
fun HighlightsCardLazyColumn(
    highlightsList: List<HighlightData>
){
    LazyColumn(
        modifier = Modifier.padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(highlightsList) { item ->
            when (item) {
                is HighlightData.Video -> VideoHighlightCard(item.data, true)
                is HighlightData.Article -> ArticleHighlightCard(item.data, true)
            }
        }
    }
    Spacer(modifier = Modifier.height(24.dp))
}

@Preview
@Composable
private fun HighlightsCardLazyColumnPreview(){
    ScorePreview {
        HighlightsCardLazyColumn(highlightsList)
    }
}