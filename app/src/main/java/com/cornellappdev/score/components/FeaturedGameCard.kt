package com.cornellappdev.score.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.cornellappdev.score.R
import com.cornellappdev.score.theme.CornellRed
import com.cornellappdev.score.theme.PennBlue
import com.cornellappdev.score.theme.Style.losingScoreText
import com.cornellappdev.score.theme.Style.vsText
import com.cornellappdev.score.theme.Style.winningScoreText

@Composable
fun FeaturedGameHeader(
    leftTeamLogo: Painter,
    rightTeamLogo: String,
    leftScore: Int? = null,
    rightScore: Int? = null,
    gradientColor1: Color,
    gradientColor2: Color,
    modifier: Modifier = Modifier
) {
    val isPast = (leftScore != null && rightScore != null)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(gradientColor1, gradientColor2)
                )
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if(isPast) Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally) else Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 36.dp)
        ) {
            Image(
                painter = leftTeamLogo,
                contentDescription = "Left Team Logo",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.size(60.dp)
            )
            if(leftScore != null && rightScore != null) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                            text = leftScore.toString(),
                            style = if(leftScore > rightScore) winningScoreText else losingScoreText,
                        modifier = Modifier.width(52.dp)
                            .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    Text(
                        text = "-",
                        style = vsText.copy(fontSize = 32.sp, fontStyle = FontStyle.Normal)
                    )
                    Text(
                        text = rightScore.toString(),
                        style = if(leftScore < rightScore) winningScoreText else losingScoreText,
                                modifier = Modifier.width(52.dp)
                                    .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }
            }
            else {
                Text(
                    text = "VS",
                    style = vsText
                )
            }
            AsyncImage(
                model = rightTeamLogo,
                contentDescription = "Right Team Logo",
                modifier = Modifier.height(62.dp)
            )
        }
    }
}

@Preview
@Composable
private fun FeaturedGameCardPreview() {
    FeaturedGameHeader(
        leftTeamLogo = painterResource(R.drawable.cornell_logo),
        rightTeamLogo = "https://cornellbigred.com/images/logos/YALE_LOGO_2020.png?width=80&height=80&mode=max",
        gradientColor1 = CornellRed,
        gradientColor2 = PennBlue,
        modifier = Modifier
    )
}

@Composable
fun FeaturedGameCard(
    leftTeamLogo: Painter,
    rightTeamLogo: String,
    leftScore: Int? = null,
    rightScore: Int? = null,
    team: String,
    location: String,
    isLive: Boolean,
    genderIcon: Painter,
    sportIcon: Painter,
    date: String,
    modifier: Modifier = Modifier,
    headerModifier: Modifier,
    gradientColor1: Color,
    gradientColor2: Color
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {

        FeaturedGameHeader(
            leftTeamLogo = leftTeamLogo,
            rightTeamLogo = rightTeamLogo,
            leftScore = leftScore,
            rightScore = rightScore,
            gradientColor1 = gradientColor1,
            gradientColor2 = gradientColor2,
            modifier = headerModifier
        )

        GameCard(
            teamLogo = rightTeamLogo,
            team = team,
            date = date,
            isLive = isLive,
            location = location,
            genderIcon = genderIcon,
            sportIcon = sportIcon,
            topCornerRound = false,
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    )
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GameScheduleScreen() {
    FeaturedGameCard(
        leftTeamLogo = painterResource(R.drawable.cornell_logo),
        rightTeamLogo = "https://cornellbigred.com/images/logos/penn_200x200.png?width=80&height=80&mode=max",//painterResource(R.drawable.penn_logo),
        leftScore = 32,
        rightScore = 30,
        team = "Penn",
        location = "Philadelphia, NJ",
        date = "5/20/2024",
        isLive = true,
        genderIcon = painterResource(id = R.drawable.ic_gender_men),
        sportIcon = painterResource(id = R.drawable.ic_baseball),
        modifier = Modifier,
        headerModifier = Modifier,
        gradientColor1 = CornellRed,
        gradientColor2 = PennBlue
    )
}
