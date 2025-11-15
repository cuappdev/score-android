package com.cornellappdev.score.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.cornellappdev.score.components.highlights.HighlightsCardRow
import com.cornellappdev.score.components.highlights.HighlightsFilterRow
import com.cornellappdev.score.components.highlights.HighlightsScreenSearchFilterBar
import com.cornellappdev.score.components.highlights.HighlightsSearchBarUI
import com.cornellappdev.score.model.HighlightData
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.theme.Style.heading1
import com.cornellappdev.score.util.highlightsList
import com.cornellappdev.score.util.sportList

/*todo: needs a UIState */
@Composable
fun HighlightsScreen(
    sportList: List<Sport> = emptyList(),
    todayHighlightsList: List<HighlightData> = emptyList(),
    pastThreeHighlightsList: List<HighlightData> = emptyList(),
    toSearchScreen: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Column(
            modifier = Modifier.padding(start =24.dp)
        ){
            Text("Highlights", style = heading1)
            Spacer(modifier = Modifier.height(12.dp))
            HighlightsSearchBarUI(toSearchScreen)
        }
        Spacer(modifier = Modifier.height(16.dp))
        HighlightsFilterRow(sportList, { /*handle with viewmodel*/ })
        Spacer(modifier = Modifier.height(24.dp))
        if (todayHighlightsList.isEmpty() && pastThreeHighlightsList.isEmpty()) {
            EmptyStateBox(
                icon = R.drawable.ic_kid_star,
                title = "No results yet.",
            )
        }
        if (todayHighlightsList.isNotEmpty()) {
            HighlightsCardRow(todayHighlightsList, "Today")
        }
        if (pastThreeHighlightsList.isNotEmpty()) {
            HighlightsCardRow(pastThreeHighlightsList, "Past 3 days")
        }
    }
}

data class HighlightsScreenPreviewData(
    val sportList: List<Sport>,
    val todayHighlightList: List<HighlightData>,
    val pastHighlightList: List<HighlightData>
)

class HighlightsScreenPreviewProvider : PreviewParameterProvider<HighlightsScreenPreviewData> {
    override val values: Sequence<HighlightsScreenPreviewData> = sequence {
        yield(HighlightsScreenPreviewData(sportList, highlightsList, highlightsList))
        yield(HighlightsScreenPreviewData(sportList, emptyList(), emptyList()))
        yield(HighlightsScreenPreviewData(sportList, emptyList(), highlightsList))
    }
}

@Preview(showBackground = true)
@Composable
private fun HighlightScreenPreview(
    @PreviewParameter(HighlightsScreenPreviewProvider::class) previewData: HighlightsScreenPreviewData
) {
    ScorePreview {
        HighlightsScreen(
            sportList = previewData.sportList,
            todayHighlightsList = previewData.todayHighlightList,
            pastThreeHighlightsList = previewData.pastHighlightList,
            toSearchScreen = {}
        )
    }
}