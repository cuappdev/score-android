package com.cornellappdev.score.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.R
import com.cornellappdev.score.model.GameCardData
import com.cornellappdev.score.theme.CornellRed
import com.cornellappdev.score.util.gameList

@Composable
fun GamesCarousel(
    games: List<GameCardData>,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(games) { game ->
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
                gradientColor1 = CornellRed,
                gradientColor2 = game.teamColor,
                leftScore = game.cornellScore?.toInt(),
                rightScore = game.otherScore?.toInt(),
                onClick = { onClick(game.id) },
                modifier = Modifier.width(300.dp)
            )
        }
    }
}

@Composable
@Preview
private fun GamesCarouselPreview() = ScorePreview {
    GamesCarousel(gameList, onClick = {})
}
