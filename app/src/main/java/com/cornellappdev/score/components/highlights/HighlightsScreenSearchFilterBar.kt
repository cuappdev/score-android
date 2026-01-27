package com.cornellappdev.score.components.highlights

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.components.ScorePreview
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.util.sportList

@Composable
fun HighlightsScreenSearchFilterBar(
    sportList: List<Sport>
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        HighlightsSearchBar(modifier = Modifier.padding(horizontal = 24.dp))
        Spacer(modifier = Modifier.height(16.dp))
        HighlightsFilterRow(sportList, {/*todo on filter selected*/ })
    }
}

@Preview
@Composable
private fun HighlightsScreenSearchFilterBarPreview() {
    ScorePreview {
        HighlightsScreenSearchFilterBar(sportList)
    }
}