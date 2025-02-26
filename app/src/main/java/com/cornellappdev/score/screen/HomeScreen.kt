package com.cornellappdev.score.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.score.components.SportCard
import com.cornellappdev.score.components.SportSelectorHeader
import com.cornellappdev.score.components.UpcomingGamesCarousel
import com.cornellappdev.score.theme.Style.heading1
import com.cornellappdev.score.theme.Style.title
import com.cornellappdev.score.util.gameList
import com.cornellappdev.score.viewmodel.HomeViewModel


@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val uiState = homeViewModel.collectUiStateValue()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
        modifier = Modifier.statusBarsPadding())
    {
        UpcomingGamesCarousel(uiState.upcomingGameList)
        Column {
            Text(
                text = "Game Schedule",
                style = heading1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            SportSelectorHeader(
                sports = uiState.selectionList,
                selectedGender =  uiState.selectedGender,
                selectedSport = uiState.sportSelect,
                onGenderSelected = {
                    homeViewModel.onGenderSelected(it)
                },
                onSportSelected = {
                    homeViewModel.onSportSelected(it)
                }
            )
            LazyColumn(modifier = Modifier.padding(horizontal = 24.dp)) {
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

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
