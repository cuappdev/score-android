package com.cornellappdev.score.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.model.GameData
import com.cornellappdev.score.model.TeamScore
import com.cornellappdev.score.model.mapToPeriodScores
import com.cornellappdev.score.theme.CrimsonPrimary
import com.cornellappdev.score.theme.GrayMedium
import com.cornellappdev.score.theme.GrayPrimary
import com.cornellappdev.score.theme.Style.metricNormal
import com.cornellappdev.score.theme.Style.metricSmallNormal
import com.cornellappdev.score.theme.saturatedGreen
import com.cornellappdev.score.util.emptyGameData
import com.cornellappdev.score.util.gameData
import com.cornellappdev.score.util.longGameData
import com.cornellappdev.score.util.mediumGameData
import com.cornellappdev.score.util.shortGameData

@Composable
fun BoxScore(
    gameData: GameData,
    modifier: Modifier = Modifier
) {
    val maxPeriods = maxOf(
        gameData.teamScores.first.scoresByPeriod.size,
        gameData.teamScores.second.scoresByPeriod.size
    )
    val rowTextStyle = if (maxPeriods > 4) metricSmallNormal else metricNormal

    Surface(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        border = (BorderStroke(width = 1.dp, color = CrimsonPrimary)),
        shadowElevation = 8.dp,
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .fillMaxWidth()
        ) {
            TeamNameColumn(
                gameData.teamScores.first.team.name,
                gameData.teamScores.second.team.name,
                rowTextStyle
            )
            CompleteTableData(
                gameData = gameData,
                rowTextStyle = rowTextStyle
            )
        }
    }
}

@Composable
private fun TeamNameColumn(
    teamOneName: String,
    teamTwoName: String,
    rowTextStyle: TextStyle,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.width(60.dp)
        //.width(IntrinsicSize.Max)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = CrimsonPrimary)
                .weight(1F)
                .padding(top = 6.dp, bottom = 4.dp)
        ) {}
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
                .padding(top = 8.dp, bottom = 8.dp)
        ) {
            Text(
                text = teamOneName,
                style = rowTextStyle,
                color = GrayPrimary,
                modifier = Modifier
                    .weight(1f, fill = true)
                    .padding(10.dp),
                textAlign = TextAlign.Left,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        HorizontalDivider(thickness = 1.dp, color = CrimsonPrimary)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 8.dp, bottom = 8.dp)
        ) {
            Text(
                text = teamTwoName,
                style = rowTextStyle,
                color = GrayPrimary,
                modifier = Modifier
                    .weight(1f, fill = true)
                    .padding(10.dp),
                textAlign = TextAlign.Left,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun TableDataColumn(
    header: Int,
    team1Score: String,
    team2Score: String,
    rowTextStyle: TextStyle,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = CrimsonPrimary)
                .weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = header.toString(),
                color = Color.White,
                style = rowTextStyle,
                textAlign = TextAlign.Center
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = team1Score,
                style = rowTextStyle,
                textAlign = TextAlign.Center
            )
        }
        HorizontalDivider(thickness = 1.dp, color = CrimsonPrimary)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = team2Score,
                style = rowTextStyle,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun CompleteTableData(
    gameData: GameData,
    rowTextStyle: TextStyle,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val periodScores = mapToPeriodScores(gameData)

        if (periodScores.isNotEmpty()) {
            mapToPeriodScores(gameData).map { periodScore ->
                TableDataColumn(
                    header = periodScore.header,
                    team1Score = periodScore.teamOneScore,
                    team2Score = periodScore.teamTwoScore,
                    rowTextStyle = rowTextStyle,
                    modifier = Modifier.weight(1f)
                )
            }
        } else {
            for (i in 1..4) {
                TableDataColumn(
                    header = i,
                    team1Score = "-",
                    team2Score = "-",
                    rowTextStyle = rowTextStyle,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        TotalsColumn(
            teamOneScores = gameData.teamScores.first,
            teamTwoScores = gameData.teamScores.second,
            modifier = Modifier.width(IntrinsicSize.Min),
            //.weight(1f),
            rowTextStyle = rowTextStyle
        )
    }
}

@Composable
private fun TotalScoreCell(
    teamScore: TeamScore,
    totalTextColor: Color,
    rowTextStyle: TextStyle,
    modifier: Modifier = Modifier
) {
    val showEmpty = teamScore.scoresByPeriod.isEmpty()
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 8.dp, bottom = 8.dp, end = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (showEmpty) "-" else teamScore.totalScore.toString(),
            style = rowTextStyle,
            color = if (showEmpty) Color.Gray else totalTextColor,
            fontWeight = if (showEmpty) FontWeight.Normal else FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun TotalsColumn(
    teamOneScores: TeamScore,
    teamTwoScores: TeamScore,
    rowTextStyle: TextStyle,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = CrimsonPrimary)
                .padding(end = 8.dp)
                .weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total",
                color = Color.White,
                style = rowTextStyle,
                textAlign = TextAlign.Center
            )
        }

        TotalScoreCell(
            teamScore = teamOneScores,
            totalTextColor = saturatedGreen,
            rowTextStyle = rowTextStyle,
            modifier = Modifier.weight(1f)
        )
        HorizontalDivider(thickness = 1.dp, color = CrimsonPrimary)
        TotalScoreCell(
            teamScore = teamTwoScores,
            totalTextColor = GrayMedium,
            rowTextStyle = rowTextStyle,
            modifier = Modifier.weight(1f)
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
private fun PreviewBoxScoreForShortGame() = ScorePreview {
    BoxScore(shortGameData)
}

@Preview
@Composable
private fun PreviewBoxScoreEmpty() = ScorePreview {
    BoxScore(gameData = emptyGameData())
}