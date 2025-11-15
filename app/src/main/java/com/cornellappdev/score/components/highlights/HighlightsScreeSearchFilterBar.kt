package com.cornellappdev.score.components.highlights

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.components.ScorePreview
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.theme.Style.bodyMedium
import com.cornellappdev.score.theme.Style.bodyNormal
import com.cornellappdev.score.util.sportList

@Composable
fun HighlightsScreenSearchFilterBar(
    sportList: List<Sport>,
    isActive: Boolean,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .clip(shape = RoundedCornerShape(100.dp))
                .clickable(onClick = {/*todo clear the highlight rows and show recent searches*/ }),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HighlightsSearchBar({}, isActive, Modifier.weight(1f))
            if (isActive) {
                Text("Cancel", style = bodyMedium)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        HighlightsFilterRow(sportList, {/*todo on filter selected*/ })
    }
}

@Preview
@Composable
private fun HighlightsScreenSearchFilterBarActivePreview() {
    ScorePreview {
        HighlightsScreenSearchFilterBar(sportList, true)
    }
}

@Preview
@Composable
private fun HighlightsScreenSearchFilterBarInactivePreview() {
    ScorePreview {
        HighlightsScreenSearchFilterBar(sportList, false)
    }
}