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
import com.cornellappdev.score.model.SportSelection
import com.cornellappdev.score.util.sportSelectionList

@Composable
fun HighlightsScreenSearchFilterBar(
    sportList: List<SportSelection>,
    onFilterSelected: (SportSelection) -> Unit,
    navigateBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        HighlightsSearchBar(modifier = Modifier.padding(horizontal = 24.dp), navigateBack)
        Spacer(modifier = Modifier.height(16.dp))
        HighlightsFilterRow(sportList, onFilterSelected)
    }
}

@Preview
@Composable
private fun HighlightsScreenSearchFilterBarPreview() {
    ScorePreview {
        HighlightsScreenSearchFilterBar(sportSelectionList, {}, navigateBack = {})
    }
}