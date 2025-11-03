package com.cornellappdev.score.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import com.cornellappdev.score.components.highlights.HighlightsScreenHeader
import com.cornellappdev.score.model.HighlightData
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.util.highlightsList
import com.cornellappdev.score.util.sportList

/*todo: needs a UIState */
@Composable
fun HighlightsScreen(
    sportList: List<Sport>,
    todayHighlightsList: List<HighlightData>,
    pastThreeHighlightsList: List<HighlightData>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        HighlightsScreenHeader(sportList)
        Spacer(modifier = Modifier.height(24.dp))
        if (todayHighlightsList.isEmpty() && pastThreeHighlightsList.isEmpty()) {
            EmptyStateBox(
                icon = R.drawable.kid_star,
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

@Composable
@Preview
private fun HighlightsScreenHeaderPreview() {
    ScorePreview {
        HighlightsScreenHeader(sportList)
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
private fun VideoHighlightCardPreview(
    @PreviewParameter(HighlightsScreenPreviewProvider::class) previewData: HighlightsScreenPreviewData
) {
    ScorePreview {
        HighlightsScreen(
            sportList = previewData.sportList,
            todayHighlightsList = previewData.todayHighlightList,
            pastThreeHighlightsList = previewData.pastHighlightList,
        )
    }
}