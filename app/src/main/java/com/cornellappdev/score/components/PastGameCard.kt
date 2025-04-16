package com.cornellappdev.score.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.cornellappdev.score.R
import com.cornellappdev.score.model.GameCardData
import com.cornellappdev.score.theme.AmbientColor
import com.cornellappdev.score.theme.GrayLight
import com.cornellappdev.score.theme.GrayMedium
import com.cornellappdev.score.theme.GrayPrimary
import com.cornellappdev.score.theme.GrayStroke
import com.cornellappdev.score.theme.SpotColor
import com.cornellappdev.score.theme.Style.heading2
import com.cornellappdev.score.theme.Style.labelsNormal
import com.cornellappdev.score.theme.Style.metricMedium
import java.time.LocalDate

@Composable
fun PastGameCard(
    data: GameCardData,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .shadow(elevation = 6.dp, spotColor = SpotColor, ambientColor = AmbientColor)
            .then(
                Modifier
                    .border(width = 1.dp, color = GrayStroke, RoundedCornerShape(16.dp))
            )
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .width(224.dp)
                    .drawBehind {
                        drawLine(
                            color = GrayLight,
                            start = Offset(size.width, 0f),
                            end = Offset(size.width, size.height),
                            strokeWidth = 1.dp.toPx()
                        )
                    }) {
                TeamScore(
                    data.isHome,
                    data.teamLogo,
                    data.team,
                    data.firstTeamListedWins,
                    data.cornellScore,
                    data.otherScore,
                )
                Spacer(modifier = Modifier.height(10.dp))
                TeamScore(
                    !data.isHome,
                    data.teamLogo,
                    data.team,
                    data.secondTeamListedWins,
                    data.cornellScore,
                    data.otherScore
                )
            }
            Spacer(modifier = Modifier.width(24.dp))
            Column(
                modifier = Modifier.height(64.dp),
                verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.End
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(data.sportIcon),
                        contentDescription = "Sport Icon",
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp),
                        tint = Color.Unspecified
                    )
                    Icon(
                        painter = painterResource(data.genderIcon),
                        contentDescription = "Gender Icon",
                        modifier = Modifier
                            .padding(2.5.dp)
                            .width(19.dp)
                            .height(19.dp),
                        tint = Color.Unspecified
                    )
                }
                Text(
                    text = data.dateString,
                    style = labelsNormal,
                    color = GrayMedium
                )
            }
        }
    }
}

@Composable
private fun TeamScore(
    isCornell: Boolean,
    teamLogo: String,
    team: String,
    winningTeam: Boolean,
    cornellScore: Number?,
    otherScore: Number?
) {

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.widthIn(0.dp, 170.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isCornell) {
                Image(
                    painter = painterResource(R.drawable.cornell_logo),
                    contentDescription = "Cornell Logo",
                    modifier = Modifier
                        .height(27.dp)
                        .padding(horizontal = 2.dp, vertical = 4.dp)
                )
            } else {
                AsyncImage(
                    model = teamLogo,
                    modifier = Modifier
                        .height(20.dp)
                        .padding(horizontal = 4.dp),
                    contentDescription = ""
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = if (isCornell) "Cornell" else team,
                style = heading2,
                color = if (winningTeam) GrayPrimary else GrayLight
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = if (isCornell) cornellScore?.toString() ?: "-" else otherScore?.toString()
                    ?: "-",
                style = metricMedium,
                color = if (winningTeam) GrayPrimary else GrayLight
            )
            Spacer(modifier = Modifier.width(12.dp))
            if (winningTeam) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Indicates winning team",
                    modifier = Modifier
                        .width(11.dp)
                        .height(14.dp),
                )
            } else {
                Box(modifier = Modifier.width(11.dp))
            }
        }
    }
}

@Preview
@Composable
private fun PastGameCardPreview() = ScorePreview {
    val gameCard = GameCardData(
        id = "1",
        teamLogo = "https://cornellbigred.com/images/logos/penn_200x200.png?width=80&height=80&mode=max",
        team = "University of Pennsylvania",
        teamColor = Color.Red,
        date = LocalDate.of(2025, 3, 24),
        dateString = "March 24, 2025",
        isLive = false,
        isPast = true,
        location = "Ithaca, NY",
        gender = "Men",
        genderIcon = R.drawable.ic_gender_men, // replace with your actual drawable resource
        sport = "Basketball",
        sportIcon = R.drawable.ic_basketball, // replace with your actual drawable resource
        cornellScore = 85,
        otherScore = 80
    )

    Column {
        PastGameCard(
            data = gameCard
        )
    }
}