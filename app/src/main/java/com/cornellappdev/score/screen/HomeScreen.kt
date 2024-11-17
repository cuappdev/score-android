package com.cornellappdev.score.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.components.SportCard
import com.cornellappdev.score.components.UpcomingGamesCarousel
import com.cornellappdev.score.theme.Style.title
import com.cornellappdev.score.util.gameList


@Composable
fun HomeScreen() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top)
    ){
        UpcomingGamesCarousel(gameList)

        Column(verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
            modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = "Game Schedule", style = title, modifier = Modifier.fillMaxWidth()
            )
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(gameList.size) { page ->
                    val game = gameList[page]
                    SportCard(
                        teamLogo = painterResource(game.teamLogo),
                        team = game.team,
                        date = game.date,
                        genderIcon = painterResource(game.genderIcon),
                        sportIcon = painterResource(game.sportIcon),
                        location = game.location,
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

