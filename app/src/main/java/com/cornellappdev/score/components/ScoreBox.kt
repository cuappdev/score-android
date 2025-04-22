package com.cornellappdev.score.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.model.GameData
import com.cornellappdev.score.model.TeamScore
import com.cornellappdev.score.theme.CrimsonPrimary
import com.cornellappdev.score.theme.GrayMedium
import com.cornellappdev.score.theme.GrayPrimary
import com.cornellappdev.score.theme.Style.bodyNormal
import com.cornellappdev.score.theme.Style.labelsNormal
import com.cornellappdev.score.theme.saturatedGreen
import com.cornellappdev.score.util.emptyGameData
import com.cornellappdev.score.util.gameData
import com.cornellappdev.score.util.longGameData
import com.cornellappdev.score.util.mediumGameData

@Composable
fun BoxScore(gameData: GameData) {
    val maxPeriods = maxOf(
        gameData.teamScores.first.scoresByPeriod.size,
        gameData.teamScores.second.scoresByPeriod.size
    )
    val rowTextStyle = if (maxPeriods > 4) labelsNormal else bodyNormal
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(8.dp))
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .border(BorderStroke(width = 1.dp, color = CrimsonPrimary))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = CrimsonPrimary)
                .padding(top = 6.dp, bottom = 4.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "",
                modifier = Modifier.weight(1f),
                color = Color.White,
                textAlign = TextAlign.Center
            )
            repeat(maxPeriods) { period ->
                Text(
                    text = "${period + 1}",
                    modifier = Modifier.weight(1f),
                    color = Color.White,
                    style = rowTextStyle,
                    textAlign = TextAlign.Center
                )
            }
            Text(
                text = "Total",
                modifier = Modifier.weight(1f),
                style = rowTextStyle,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
        TeamScoreRow(
            teamScore = gameData.teamScores.first,
            totalTextColor = saturatedGreen,
            maxPeriods,
            rowTextStyle
        )
        HorizontalDivider(thickness = 1.dp, color = CrimsonPrimary)

        TeamScoreRow(
            teamScore = gameData.teamScores.second,
            totalTextColor = GrayMedium,
            maxPeriods,
            rowTextStyle
        )

    }
}

@Composable
fun TeamScoreRow(
    teamScore: TeamScore,
    totalTextColor: Color,
    maxPeriods: Int,
    rowTextStyle: TextStyle
) {
    val showEmpty = teamScore.scoresByPeriod.isEmpty()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = teamScore.team.name,
            style = rowTextStyle,
            color = GrayPrimary,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        teamScore.scoresByPeriod.forEach { score ->
            Text(
                text = if (showEmpty) "-" else score.toString(),
                modifier = Modifier.weight(1f),
                style = rowTextStyle,
                color = GrayPrimary,
                textAlign = TextAlign.Center
            )
        }

        repeat(maxPeriods - teamScore.scoresByPeriod.size) {
            Text(
                text = "-",
                modifier = Modifier.weight(1f),
                style = rowTextStyle,
                color = GrayPrimary,
                textAlign = TextAlign.Center
            )
        }

        Text(
            text = if (showEmpty) "-" else teamScore.totalScore.toString(),
            modifier = Modifier.weight(1f),
            style = rowTextStyle,
            color = if (showEmpty) Color.Gray else totalTextColor,
            fontWeight = if (showEmpty) FontWeight.Normal else FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}


@Preview
@Composable
private fun PreviewBoxScore() = ScorePreview {
    BoxScore(gameData = gameData)
}

@Preview
@Composable
private fun PreviewBoxScoreForLongGame() = ScorePreview {
    BoxScore(longGameData)
}

@Preview
@Composable
private fun PreviewBoxScoreForMedGame() = ScorePreview {
    BoxScore(mediumGameData)
}

@Preview
@Composable
private fun PreviewBoxScoreEmpty() = ScorePreview {
    BoxScore(gameData = emptyGameData())
}


@Preview
@Composable
private fun PreviewTeamScoreRow() = ScorePreview {
    TeamScoreRow(gameData.teamScores.first, GrayMedium, 4, bodyNormal)
}