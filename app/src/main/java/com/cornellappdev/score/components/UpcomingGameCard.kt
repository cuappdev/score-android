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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.theme.Style.vsText
import com.example.score.R

@Composable
fun UpcomingGameHeader(
    leftTeamLogo: Painter,
    rightTeamLogo: Painter,
    gradientColor1: Color,
    gradientColor2: Color,
    modifier: Modifier = Modifier
) {
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
            horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally),
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

            Text(
                text = "VS",
                style = vsText
            )

            Image(
                painter = rightTeamLogo,
                contentDescription = "Right Team Logo",
                modifier = Modifier.height(62.dp)
            )
        }
    }
}

@Preview
@Composable
fun UpcomingGameHeaderPreview() {
    UpcomingGameHeader(
        leftTeamLogo = painterResource(R.drawable.cornell_logo),
        rightTeamLogo = painterResource(R.drawable.penn_logo),
        gradientColor1 = Color(0xE1A69F),
        gradientColor2 = Color(0x011F5B),
        modifier = Modifier
    )
}

@Composable
fun UpcomingGameCard(
    leftTeamLogo: Painter,
    rightTeamLogo: Painter,
    team: String,
    location: String,
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

        UpcomingGameHeader(
            leftTeamLogo = leftTeamLogo,
            rightTeamLogo = rightTeamLogo,
            gradientColor1 = gradientColor1,
            gradientColor2 = gradientColor2,
            modifier = headerModifier
        )

        SportCard(
            teamLogo = rightTeamLogo,
            team = team,
            date = date,
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
    UpcomingGameCard(
        leftTeamLogo = painterResource(R.drawable.cornell_logo),
        rightTeamLogo = painterResource(R.drawable.penn_logo),
        team = "Penn",
        location = "Philadelphia, NJ",
        date = "5/20/2024",
        genderIcon = painterResource(id = R.drawable.ic_gender_men),
        sportIcon = painterResource(id = R.drawable.ic_baseball),
        modifier = Modifier,
        headerModifier = Modifier,
        gradientColor1 = Color(0xE1A69F),
        gradientColor2 = Color(0x011F5B)
    )
}
