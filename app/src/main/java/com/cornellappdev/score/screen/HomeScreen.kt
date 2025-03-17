package com.cornellappdev.score.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.score.components.SportCard
import com.cornellappdev.score.components.SportSelectorHeader
import com.cornellappdev.score.components.UpcomingGamesCarousel
import com.cornellappdev.score.model.ApiResponse
import com.cornellappdev.score.model.GenderDivision
import com.cornellappdev.score.model.SportSelection
import com.cornellappdev.score.theme.Style.title
import com.cornellappdev.score.util.gameList
import com.cornellappdev.score.viewmodel.HomeUiState
import com.cornellappdev.score.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val uiState = homeViewModel.collectUiStateValue()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
        modifier = Modifier.statusBarsPadding()
    ) {
        when (uiState.loadedState) {
            is ApiResponse.Loading -> {
                //TODO: Add loading screen
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is ApiResponse.Error -> {
                //TODO: Add Error screen
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Failed to load games. Please try again.",
                    )
                }
            }

            is ApiResponse.Success -> {
                HomeContent(
                    uiState = uiState,
                    onGenderSelected = { homeViewModel.onGenderSelected(it) },
                    onSportSelected = { homeViewModel.onSportSelected(it) }
                )
            }
        }
    }
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onGenderSelected: (GenderDivision) -> Unit,
    onSportSelected: (SportSelection) -> Unit
) {
    UpcomingGamesCarousel(uiState.upcomingGames)
    Column {
        Text(
            text = "Game Schedule",
            style = title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        SportSelectorHeader(
            sports = uiState.selectionList,
            selectedGender = uiState.selectedGender,
            selectedSport = uiState.sportSelect,
            onGenderSelected = onGenderSelected,
            onSportSelected = onSportSelected
        )
        LazyColumn(modifier = Modifier.padding(horizontal = 24.dp)) {
            items(uiState.filteredGames) {
                val game = it
                SportCard(
                    teamLogo = game.teamLogo,
                    team = game.team,
                    date = game.dateString,
                    isLive = game.isLive,
                    genderIcon = painterResource(game.genderIcon),
                    sportIcon = painterResource(game.sportIcon),
                    location = game.location,
                    topCornerRound = true
                )
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        HomeContent(
            HomeUiState(
                selectedGender = GenderDivision.ALL,
                sportSelect = SportSelection.All,
                selectionList = emptyList(),
                loadedState = ApiResponse.Success(gameList)
            ),
            onGenderSelected = {},
            onSportSelected = {}
        )
    }
}

