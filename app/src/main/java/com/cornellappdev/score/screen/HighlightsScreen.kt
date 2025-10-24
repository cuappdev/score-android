package com.cornellappdev.score.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.R
import com.cornellappdev.score.components.ArticleHighlightCard
import com.cornellappdev.score.components.EmptyStateBox
import com.cornellappdev.score.components.HighlightsFilterRow
import com.cornellappdev.score.components.VideoHighlightCard
import com.cornellappdev.score.model.ArticleHighlightData
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.model.VideoHighlightData
import com.cornellappdev.score.theme.GrayLight
import com.cornellappdev.score.theme.GrayMedium
import com.cornellappdev.score.theme.Style.bodyNormal
import com.cornellappdev.score.theme.Style.heading1
import com.cornellappdev.score.theme.Style.heading2
import com.cornellappdev.score.util.highlightsList
import com.cornellappdev.score.util.sportList

@Composable
fun HighlightsScreenHeader(
    onSearch: () -> Unit
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
                .border(width = 1.dp, color = GrayLight, shape = RoundedCornerShape(100.dp))
                .background(color = Color.White, shape = RoundedCornerShape(100.dp))
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(100.dp))
                .clickable { onSearch },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.search),
                    contentDescription = "search icon"
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Search keywords",
                    style = bodyNormal
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        HighlightsFilterRow({/*todo on filter selected*/})
    }
}

@Composable
fun HighlightsCardRow(
    highlightsList: List<Any>
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
                text = "Today",
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
                Image(
                    painter = painterResource(R.drawable.ic_right_chevron),
                    contentDescription = "right chevron",
                    colorFilter = ColorFilter.tint(GrayMedium, blendMode = BlendMode.SrcIn)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            modifier = Modifier.padding(start = 24.dp)
        ) {
            items(highlightsList.size) { i ->
                if (highlightsList[i] is VideoHighlightData) {
                    VideoHighlightCard(
                        highlightsList[i] as VideoHighlightData,
                        false
                    )
                } else if (highlightsList[i] is ArticleHighlightData) {
                    ArticleHighlightCard(
                        highlightsList[i] as ArticleHighlightData,
                        false
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun HighlightsScreen(
    todayHighlightsList: List<Any>,
    pastThreeHighlightsList: List<Any>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        HighlightsScreenHeader({})
        Spacer(modifier = Modifier.height(24.dp))
        if (todayHighlightsList.isEmpty() and pastThreeHighlightsList.isEmpty()) {
            EmptyStateBox(
                icon = R.drawable.kid_star,
                title = "No results yet."
            )
        }
        if (todayHighlightsList.isNotEmpty()) {
            HighlightsCardRow(todayHighlightsList)
        }
        if (pastThreeHighlightsList.isNotEmpty()) {
            HighlightsCardRow(pastThreeHighlightsList)
        }
    }
}

@Composable
@Preview
private fun HighlightsScreenHeaderPreview() {
    HighlightsScreenHeader({})
}

@Composable
@Preview
private fun HighlightsScreenPreview() {
    HighlightsScreen(highlightsList, highlightsList)
}

@Composable
@Preview
private fun EmptyHighlightsScreenPreview() {
    HighlightsScreen(emptyList(), emptyList())
}

@Composable
@Preview
private fun PartialHighlightsScreenPreview() {
    HighlightsScreen(emptyList(), highlightsList)
}