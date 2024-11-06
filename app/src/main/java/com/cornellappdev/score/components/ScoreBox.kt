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
import androidx.compose.ui.unit.sp
import com.cornellappdev.score.model.GameData
import com.cornellappdev.score.model.Team
import com.cornellappdev.score.model.TeamScore

@Composable
fun BoxScore(gameData: GameData) {
    val redColor = Color(0xFFB71C1C)
    val grayBackground = Color(0xFF333333)
    val greenTextColor = Color(0xFF4CAF50)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(grayBackground)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(8.dp))
        ) {
            // Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(redColor)
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "",
                    modifier = Modifier.weight(1f),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                gameData.teamScores.first.scoresByPeriod.indices.forEach { period ->
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

            // Data Row for Team 1
            TeamScoreRow(
                teamScore = gameData.teamScores.first,
                totalTextColor = greenTextColor
            )

            Divider(color = redColor, thickness = 1.dp)

            // Data Row for Team 2
            TeamScoreRow(
                teamScore = gameData.teamScores.second,
                totalTextColor = Color.Black
            )
        }
    }
}

@Composable
fun TeamScoreRow(teamScore: TeamScore, totalTextColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = teamScore.team.name,
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        teamScore.scoresByPeriod.forEach { score ->
            Text(
                text = score.toString(),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }
        Text(
            text = teamScore.totalScore.toString(),
            modifier = Modifier.weight(1f),
            color = totalTextColor,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBoxScore() {
    // Sample Data
    val team1 = Team(name = "Cornell", logoUrl = "url_to_cornell_logo")
    val team2 = Team(name = "Yale", logoUrl = "url_to_yale_logo")

    val teamScore1 = TeamScore(
        team = team1,
        scoresByPeriod = listOf(13, 14, 6, 14),
        totalScore = 47
    )
    val teamScore2 = TeamScore(
        team = team2,
        scoresByPeriod = listOf(7, 7, 9, 0),
        totalScore = 23
    )

    val gameData = GameData(teamScores = Pair(teamScore1, teamScore2))

    // Call BoxScore with the sample data
    BoxScore(gameData = gameData)
}

