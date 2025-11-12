package com.cornellappdev.score.components.highlights

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.components.ScorePreview
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.theme.Style.heading1
import com.cornellappdev.score.util.sportList

@Composable
fun HighlightsScreenHeader(
    sportList: List<Sport>,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(start = 24.dp),
            style = heading1,
            text = "Highlights"
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .clip(shape = RoundedCornerShape(100.dp))
                .clickable(onClick = {/*todo clear the highlight rows and show recent searches*/ })
        ) {
            HighlightsSearchBar({})
        }
        Spacer(modifier = Modifier.height(16.dp))
        HighlightsFilterRow(sportList, {/*todo on filter selected*/ })
    }
}

@Preview
@Composable
private fun HighlightsScreenHeaderPreview() {
    ScorePreview {
        HighlightsScreenHeader(sportList)
    }
}