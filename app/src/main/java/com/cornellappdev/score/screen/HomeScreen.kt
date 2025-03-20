package com.cornellappdev.score.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.score.components.GameCard
import com.cornellappdev.score.components.SportSelectorHeader
import com.cornellappdev.score.components.GamesCarousel
import com.cornellappdev.score.theme.Style.heading1
import com.cornellappdev.score.theme.White
import com.cornellappdev.score.viewmodel.HomeViewModel

@RequiresApi(Build.VERSION_CODES.O)//TODO - change the manifest or leave this?
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navigateToGameDetails: (Boolean) -> Unit = {}
) {
    val uiState by homeViewModel.uiStateFlow.collectAsState()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
        modifier = Modifier.statusBarsPadding().background(White)
    )
    {
        //TODO: check - displaying the earliest three games
        GamesCarousel(
            uiState.upcomingGameList.subList(
                0,
                minOf(3, uiState.upcomingGameList.size)
            ), true
        )
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
                selectedGender = uiState.selectedGender,
                selectedSport = uiState.sportSelect,
                onGenderSelected = {
                    homeViewModel.onGenderSelected(it)
                },
                onSportSelected = {
                    homeViewModel.onSportSelected(it)
                }
            )
            LazyColumn(modifier = Modifier.padding(horizontal = 24.dp)) {
                items(uiState.filteredGames.size) { page ->
                    val game = uiState.filteredGames[page]
                    GameCard(
                        teamLogo = game.teamLogo,//painterResource(game.teamLogo),
                        team = game.team,
                        date = game.dateString,
                        isLive = game.isLive,
                        genderIcon = painterResource(game.genderIcon),
                        sportIcon = painterResource(game.sportIcon),
                        location = game.location,
                        topCornerRound = true,
                        onClick = navigateToGameDetails
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
