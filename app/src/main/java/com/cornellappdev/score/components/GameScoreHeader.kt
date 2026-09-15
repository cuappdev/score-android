package com.cornellappdev.score.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.cornellappdev.score.R
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.theme.Style.scoreHeaderText
import com.cornellappdev.score.theme.rankHeaderNeutral
import com.cornellappdev.score.theme.rankHeaderRed
import com.cornellappdev.score.util.toPlacement

@Composable
fun GameScoreHeader(
    leftTeamLogo: Painter,
    rightTeamLogo: String,
    gradientColor1: Color,
    gradientColor2: Color,
    leftScore: Int,
    rightScore: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(gradientColor1, gradientColor2)
                )
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = leftTeamLogo,
                contentDescription = "Left Team Logo",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.size(70.dp)
            )

            Row {
                Text(
                    text = leftScore.toString(),
                    style = scoreHeaderText,
                    modifier = Modifier.width(52.dp),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "-",
                    style = scoreHeaderText,
                )

                Text(
                    text = rightScore.toString(),
                    style = scoreHeaderText,
                    modifier = Modifier.width(52.dp),
                    textAlign = TextAlign.Center
                )
            }

            AsyncImage(
                model = rightTeamLogo,
                contentDescription = "Right Team Logo",
                modifier = Modifier.height(70.dp)
            )
        }
    }
}

@Composable
fun AlternativeScoreHeader(
    rank: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(rankHeaderRed, rankHeaderNeutral)
                )
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {

            Image(
                painter = painterResource(R.drawable.cornell_logo),
                contentDescription = "Left Team Logo",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.size(70.dp)
            )
            Spacer(modifier = Modifier.width(11.dp))

            //nth place
            Text(
                text = "${rank.toPlacement()} Place",
                style = scoreHeaderText,
            )
        }
    }
}

@Composable
fun NthPlaceScoreHeader(
    resultString: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(rankHeaderRed, rankHeaderNeutral)
                )
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {

            Image(
                painter = painterResource(R.drawable.cornell_logo),
                contentDescription = "Left Team Logo",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.size(70.dp)
            )
            Spacer(modifier = Modifier.width(11.dp))

            //nth place
            Text(
                text = resultString.split("(")[0],
                style = scoreHeaderText,
            )
        }
    }
}

@Composable
fun WinLoseHeaderContent(
    teamOneLogo: Painter,
    teamTwoLogo: Painter,
) {
    //parse result (remove score in parentheses)
}

@Preview
@Composable
private fun GameScoreHeaderPreview() = ScorePreview {
    GameScoreHeader(
        leftTeamLogo = painterResource(R.drawable.cornell_logo),
        rightTeamLogo = "https://images.sidearmdev.com/fit?url=https%3a%2f%2fdxbhsrqyrr690.cloudfront.net%2fsidearm.nextgen.sites%2fcornellbigred.com%2fimages%2flogos%2fpenn_200x200.png&height=80&width=80&type=webp",
        gradientColor1 = Color(0xFFE1A69F),
        gradientColor2 = Color(0xFF011F5B),
        leftScore = 0,
        rightScore = 0,
        modifier = Modifier.height(185.dp)
    )
}

@Preview
@Composable
private fun AlternativeScoreHeaderPreview() = ScorePreview {
    AlternativeScoreHeader(6, Modifier.height(185.dp))
}

@Preview
@Composable
private fun NthPlaceScoreHeaderPreview() = ScorePreview {
    NthPlaceScoreHeader("11th of 18 (881)", Modifier.height(185.dp))
    NthPlaceScoreHeader("High Point", Modifier.height(185.dp))
}