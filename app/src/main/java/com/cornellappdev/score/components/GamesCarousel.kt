package com.cornellappdev.score.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.R
import com.cornellappdev.score.model.GameCardData
import com.cornellappdev.score.theme.CornellRed
import com.cornellappdev.score.theme.CrimsonPrimary
import com.cornellappdev.score.theme.GrayLight
import com.cornellappdev.score.util.gameList

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

@Composable
fun GamesCarousel(
    games: List<GameCardData>,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(pageCount = { games.size })
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 24.dp),
            pageSpacing = 24.dp
        ) { page ->
            val game = games[page]
            FeaturedGameCard(
                leftTeamLogo = painterResource(R.drawable.cornell_logo),
                rightTeamLogo = game.teamLogo,
                team = game.team,
                date = game.dateString,
                isLive = game.isLive,
                isPast = game.isPast,
                genderIcon = painterResource(game.genderIcon),
                sportIcon = painterResource(game.sportIcon),
                location = game.location,
                modifier = Modifier,
                headerModifier = Modifier,
                gradientColor1 = CornellRed,
                gradientColor2 = game.teamColor,
                leftScore = game.cornellScore?.toInt(),
                rightScore = game.otherScore?.toInt(),
                onClick = { onClick(game.id) }
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

@Composable
@Preview
private fun GamesCarouselPreview() = ScorePreview {
    GamesCarousel(gameList, onClick = {})
}