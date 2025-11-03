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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.cornellappdev.score.model.HighlightData
import com.cornellappdev.score.theme.Style.bodyNormal
import com.cornellappdev.score.theme.Style.heading2
import com.cornellappdev.score.util.highlightsList

@Composable
fun HighlightsCardRow(
    highlightsList: List<HighlightData>,
    rowHeader: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = rowHeader,
                style = heading2
            )
            Row(
                modifier = Modifier.clickable {/*todo navigation to Today screen*/ },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${highlightsList.size} Results",
                    style = bodyNormal
                )
                Icon(
                    painter = painterResource(R.drawable.ic_right_chevron_small),
                    contentDescription = "right chevron",
                    tint = Color.Unspecified
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(Modifier.width(8.dp)) }
            items(highlightsList) { item ->
                when (item) {
                    is HighlightData.Video -> VideoHighlightCard(item.data)
                    is HighlightData.Article -> ArticleHighlightCard(item.data)
                }
            }
            item { Spacer(Modifier.width(8.dp)) }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview
@Composable
private fun HighlightsCardRowPreview() {
    ScorePreview {
        HighlightsCardRow(highlightsList, "Today")
    }
}
