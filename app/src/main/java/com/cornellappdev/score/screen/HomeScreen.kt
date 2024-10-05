package com.cornellappdev.score.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import com.cornellappdev.android.score.data.models.GameCardData
import com.cornellappdev.score.components.SportCard
import com.cornellappdev.score.components.UpcomingGameCard
import com.cornellappdev.score.components.UpcomingGamesCarousel
import com.cornellappdev.score.theme.poppinsFamily
import com.cornellappdev.score.util.PENN_GAME
import com.cornellappdev.score.util.PRINCETON_GAME
import com.cornellappdev.score.util.gameList
import com.example.score.R
import com.google.accompanist.pager.ExperimentalPagerApi

@Composable
fun HomeScreen() {
    Column( verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top))
    {
        UpcomingGamesCarousel(gameList)
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(text = "Game Schedule", style = TextStyle(
                fontSize = 24.sp,
                fontFamily = poppinsFamily,
                fontWeight = FontWeight(600),
                color = Color(0xFF333333),
                )
            )
            LazyColumn {
                items(gameList.size) { page ->
                    val game = gameList[page]
                    SportCard(
                        teamLogo = painterResource(game.teamLogo),
                        team = game.team,
                        date = game.date,
                        genderIcon = painterResource(game.genderIcon),
                        sportIcon = painterResource(game.sportIcon),
                        location = game.location,
                        modifier = Modifier.padding(vertical = 8.dp),
                        topCornerRound = true
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}

