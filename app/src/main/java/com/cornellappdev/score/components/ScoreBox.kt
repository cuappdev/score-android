package com.cornellappdev.score.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.model.GameData
import com.cornellappdev.score.model.TeamScore
import com.cornellappdev.score.theme.CrimsonPrimary
import com.cornellappdev.score.theme.Style.bodyNormal
import com.cornellappdev.score.theme.Style.heading6
import com.cornellappdev.score.theme.Style.scoreText
import com.cornellappdev.score.theme.saturatedGreen
import com.cornellappdev.score.util.emptyGameData
import com.cornellappdev.score.util.gameData

@Composable
fun BoxScore(gameData: GameData) {
    val maxPeriods = maxOf(
        gameData.teamScores.first.scoresByPeriod.size,
        gameData.teamScores.second.scoresByPeriod.size,
        4
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CrimsonPrimary)
                .padding(vertical = 8.dp),
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
                    textAlign = TextAlign.Center
                )
            }
            Text(
                text = "Total",
                modifier = Modifier.weight(1f),
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
            totalTextColor = Color.Black,
        )
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
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )

        teamScore.scoresByPeriod.forEach { score ->
            Text(
                text = if (showEmpty) "-" else score.toString(),
                modifier = Modifier.weight(1f),
                style = scoreText,
                textAlign = TextAlign.Center
            )
        }

        repeat(4 - teamScore.scoresByPeriod.size) {
            Text(
                text = "-",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }

        Text(
            text = if (showEmpty) "-" else teamScore.totalScore.toString(),
            modifier = Modifier.weight(1f),
            style = heading6,
            color = if (showEmpty) Color.Gray else totalTextColor,
            fontWeight = if (showEmpty) FontWeight.Normal else FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBoxScore() {
    BoxScore(gameData = gameData)
}

@Preview(showBackground = true)
@Composable
fun PreviewBoxScoreEmpty() {
    BoxScore(gameData = emptyGameData())
}
