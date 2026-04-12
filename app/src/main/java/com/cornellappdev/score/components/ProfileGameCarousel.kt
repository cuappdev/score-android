package com.cornellappdev.score.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.R
import com.cornellappdev.score.model.GameCardData
import com.cornellappdev.score.theme.CornellRed
import com.cornellappdev.score.theme.GrayMedium
import com.cornellappdev.score.theme.Style

@Composable
fun ProfileGameCarousel(
    title: String,
    games: List<GameCardData>,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = Style.heading6
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${games.size} Results",
                    style = Style.bodyNormalGray
                )
                Icon(
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = null,
                    tint = GrayMedium,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
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
                    modifier = Modifier.width(241.dp),
                    headerModifier = Modifier
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}