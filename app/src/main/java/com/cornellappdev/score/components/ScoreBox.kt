package com.cornellappdev.score.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.cornellappdev.score.theme.Style.metricNormal
import com.cornellappdev.score.theme.Style.metricSemibold
import com.cornellappdev.score.theme.saturatedGreen
import com.cornellappdev.score.util.emptyGameData
import com.cornellappdev.score.util.gameData
import com.cornellappdev.score.util.longGameData

@Composable
fun BoxScore(gameData: GameData) {
    val maxPeriods = maxOf(
        gameData.teamScores.first.scoresByPeriod.size,
        gameData.teamScores.second.scoresByPeriod.size,
        4
    )
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(width = 1.dp, color = CrimsonPrimary)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = CrimsonPrimary)
                    .padding(top = 6.dp, bottom = 4.dp),
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
                        style = bodyNormal,
                        textAlign = TextAlign.Center
                    )
                }
                Text(
                    text = "Total",
                    modifier = Modifier.weight(1f),
                    style = bodyNormal,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
            TeamScoreRow(
                teamScore = gameData.teamScores.first,
                totalTextColor = saturatedGreen,
            )
            Divider(color = CrimsonPrimary, thickness = 1.dp)

            TeamScoreRow(
                teamScore = gameData.teamScores.second,
                totalTextColor = GrayMedium
            )
        }
    }
}

@Composable
fun TeamScoreRow(teamScore: TeamScore, totalTextColor: Color) {
    val showEmpty = teamScore.scoresByPeriod.isEmpty()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = teamScore.team.name,
            style = bodyNormal,
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
                style = metricNormal,
                color = GrayPrimary,
                textAlign = TextAlign.Center
            )
        }

        repeat(4 - teamScore.scoresByPeriod.size) {
            Text(
                text = "-",
                modifier = Modifier.weight(1f),
                style = metricNormal,
                color = GrayPrimary,
                textAlign = TextAlign.Center
            )
        }

        Text(
            text = if (showEmpty) "-" else teamScore.totalScore.toString(),
            modifier = Modifier.weight(1f),
            style = metricSemibold,
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
private fun PreviewBoxScoreEmpty() = ScorePreview {
    BoxScore(gameData = emptyGameData())
}


@Preview
@Composable
private fun PreviewTeamScoreRow() = ScorePreview {
    TeamScoreRow(gameData.teamScores.first, GrayMedium)
}