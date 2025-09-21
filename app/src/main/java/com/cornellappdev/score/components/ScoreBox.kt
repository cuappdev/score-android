package com.cornellappdev.score.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.cornellappdev.score.util.extraLongGameData
import com.cornellappdev.score.util.gameData
import com.cornellappdev.score.util.longGameData
import com.cornellappdev.score.util.mediumGameData
import com.cornellappdev.score.util.shortGameData

private val HEADER_HEIGHT = 35.dp

@Composable
fun BoxScore(
    gameData: GameData,
    modifier: Modifier = Modifier
) {
    val maxPeriods = maxOf(
        gameData.teamScores.first.scoresByPeriod.size,
        gameData.teamScores.second.scoresByPeriod.size
    )

    val rowTextStyle = if (maxPeriods > 4) {
        metricSmallNormal
    } else {
        metricNormal
    }

    Surface(
        modifier = modifier
            //set height required for CompleteLazyTableData case
            .height(160.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        border = (BorderStroke(width = 1.dp, color = CrimsonPrimary)),
        shadowElevation = 8.dp,
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TeamNameColumn(
                gameData.teamScores.first.team.name,
                gameData.teamScores.second.team.name,
                rowTextStyle,
                maxPeriods
            )
            if (maxPeriods > 10) {
                CompleteLazyTableData(
                    gameData = gameData,
                    rowTextStyle = rowTextStyle
                )
            } else {
                CompleteTableData(
                    gameData = gameData,
                    rowTextStyle = rowTextStyle,
                    maxPeriods = maxPeriods
                )
            }
        }
    }
}

@Composable
private fun TeamNameColumn(
    teamOneName: String,
    teamTwoName: String,
    rowTextStyle: TextStyle,
    maxPeriods: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = if (maxPeriods > 4) {
            //set to approximation of minimum width for "Cornell"
            modifier.widthIn(max = 70.dp)
        } else {
            //can take up as much space as is available
            modifier.width(IntrinsicSize.Max)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = CrimsonPrimary)
                .height(HEADER_HEIGHT)
                .padding(top = 6.dp, bottom = 4.dp),
        ) {}
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 8.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
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
                .padding(top = 8.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
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
    teamOneScore: String,
    teamTwoScore: String,
    rowTextStyle: TextStyle,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = CrimsonPrimary)
                .height(HEADER_HEIGHT),
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
                text = teamOneScore,
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
                text = teamTwoScore,
                style = rowTextStyle,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun CompleteLazyTableData(
    gameData: GameData,
    rowTextStyle: TextStyle,
    modifier: Modifier = Modifier
) {
    val periodScores = mapToPeriodScores(gameData)

    if (periodScores.isNotEmpty()) {
        Row(
            modifier = modifier
        ) {
            LazyRow(
                modifier = Modifier.weight(1f)
            ) {
                items(periodScores) { periodScore ->
                    TableDataColumn(
                        header = periodScore.header,
                        teamOneScore = periodScore.teamOneScore,
                        teamTwoScore = periodScore.teamTwoScore,
                        rowTextStyle = rowTextStyle,
                        modifier = Modifier.width(35.dp)
                    )
                }
            }
            TotalsColumn(
                teamOneScores = gameData.teamScores.first,
                teamTwoScores = gameData.teamScores.second,
                rowTextStyle = rowTextStyle
            )
        }
    }
}

@Composable
private fun CompleteTableData(
    gameData: GameData,
    rowTextStyle: TextStyle,
    maxPeriods: Int,
    modifier: Modifier = Modifier
) {
    val periodScores = mapToPeriodScores(gameData)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        if (periodScores.isNotEmpty()) {
            mapToPeriodScores(gameData).map { periodScore ->
                TableDataColumn(
                    header = periodScore.header,
                    teamOneScore = periodScore.teamOneScore,
                    teamTwoScore = periodScore.teamTwoScore,
                    rowTextStyle = rowTextStyle,
                    modifier = Modifier.weight(1f)
                )
            }
        } else {
            for (i in 1..4) {
                TableDataColumn(
                    header = i,
                    teamOneScore = "-",
                    teamTwoScore = "-",
                    rowTextStyle = rowTextStyle,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        TotalsColumn(
            teamOneScores = gameData.teamScores.first,
            teamTwoScores = gameData.teamScores.second,
            //if maxPeriods > 8, "Totals" header will wrap to two lines. In this case, don't weight so that space is allocated to TotalsColumn first
            //otherwise, TotalsColumn will fit without wrapping and can be allocated equal width as the data columns
            modifier = if (maxPeriods < 8) {
                Modifier.weight(1f, fill = true)
            } else {
                Modifier
            },
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
            .fillMaxWidth()
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
            .width(IntrinsicSize.Min)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = CrimsonPrimary)
                .padding(end = 8.dp)
                .height(HEADER_HEIGHT),
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

        val totalOne = teamOneScores.totalScore
        val totalTwo = teamTwoScores.totalScore

        TotalScoreCell(
            teamScore = teamOneScores,
            totalTextColor = if (totalOne > totalTwo) {
                saturatedGreen
            } else {
                GrayMedium
            },
            rowTextStyle = rowTextStyle,
            modifier = Modifier.weight(1f)
        )
        HorizontalDivider(thickness = 1.dp, color = CrimsonPrimary)
        TotalScoreCell(
            teamScore = teamTwoScores,
            totalTextColor = if (totalTwo > totalOne) {
                saturatedGreen
            } else {
                GrayMedium
            },
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
private fun PreviewBoxScoreExtraLongGame() = ScorePreview {
    BoxScore(gameData = extraLongGameData)
}

@Preview
@Composable
private fun PreviewBoxScoreEmpty() = ScorePreview {
    BoxScore(gameData = emptyGameData())
}