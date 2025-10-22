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
import com.cornellappdev.score.components.HighlightsFilterButton
import com.cornellappdev.score.components.VideoHighlightCard
import com.cornellappdev.score.model.HighlightCard
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.theme.GrayLight
import com.cornellappdev.score.theme.GrayMedium
import com.cornellappdev.score.theme.Style.bodyNormal
import com.cornellappdev.score.theme.Style.heading1
import com.cornellappdev.score.theme.Style.heading2
import com.cornellappdev.score.util.highlightsList
import com.cornellappdev.score.util.sportList

@Composable
fun HighlightsScreen(
    todayHighlightsList: List<HighlightCard>,
    pastThreeHighlightsList: List<HighlightCard>,
    filtersList: List<Sport>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
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
                .clickable {/*todo navigate to search screen*/ },
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
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(filtersList.size) { i ->
                HighlightsFilterButton(filtersList[i], {/*todo filter functionality*/ })
                Spacer(Modifier.width(12.dp))
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
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
                    text = "${todayHighlightsList.size} Results",
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
            items(todayHighlightsList.size) { i ->
                VideoHighlightCard(
                    todayHighlightsList[i],
                    false
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Past 3 days",
                style = heading2
            )
            Row(
                modifier = Modifier.clickable {/*todo navigation to Past3Days screen*/ },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${pastThreeHighlightsList.size} Results",
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
            items(pastThreeHighlightsList.size) { i ->
                ArticleHighlightCard(
                    todayHighlightsList[i],
                    false
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
    }
}

@Composable
@Preview
private fun HighlightsScreenPreview() {
    HighlightsScreen(highlightsList, highlightsList, sportList)
}