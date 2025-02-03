package com.cornellappdev.score.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.theme.Style.title
import com.cornellappdev.score.util.gameList
import com.cornellappdev.score.R
import com.cornellappdev.score.cornell_red
import com.cornellappdev.score.model.GameCardData
import com.cornellappdev.score.penn_blue
import com.cornellappdev.score.theme.CrimsonPrimary
import com.cornellappdev.score.theme.GrayLight

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DotIndicator(
    pagerState: androidx.compose.foundation.pager.PagerState,
    totalPages: Int,
    modifier: Modifier = Modifier,
    dotSize: Dp = 14.dp,
    selectedColor: Color = CrimsonPrimary,
    unselectedColor: Color = GrayLight
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        for (i in 0 until totalPages) {
            val color = if (i == pagerState.currentPage) selectedColor else unselectedColor
            Canvas(
                modifier = Modifier
                    .size(dotSize)
                    .padding(2.dp)
            ) {
                drawCircle(color = color)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UpcomingGamesCarousel(games: List<GameCardData>) {
    val pagerState = rememberPagerState(pageCount = { games.size })
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
    ) {
        Text(
            text = "Upcoming",
            style = title,
            modifier = Modifier.fillMaxWidth()
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val game = games[page]
            UpcomingGameCard(
                leftTeamLogo = painterResource(R.drawable.cornell_logo),
                rightTeamLogo = painterResource(game.teamLogo),
                team = game.team,
                date = game.date,
                genderIcon = painterResource(game.genderIcon),
                sportIcon = painterResource(game.sportIcon),
                location = game.location,
                modifier = Modifier,
                headerModifier = Modifier,
                gradientColor1 = cornell_red,//E1A69F),
                gradientColor2 = penn_blue//FF011F5B)
            )
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            DotIndicator(
                pagerState = pagerState,
                totalPages = games.size,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun UpcomingGamesCarouselPreview() {
    UpcomingGamesCarousel(gameList)
}