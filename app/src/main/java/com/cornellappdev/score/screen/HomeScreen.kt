package com.cornellappdev.score.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.score.components.ErrorState
import com.cornellappdev.score.components.LoadingScreen
import com.cornellappdev.score.components.GamesCarousel
import com.cornellappdev.score.components.GameCard
import com.cornellappdev.score.components.SportSelectorHeader
import com.cornellappdev.score.model.ApiResponse
import com.cornellappdev.score.model.GamesCarouselVariant
import com.cornellappdev.score.model.GenderDivision
import com.cornellappdev.score.model.SportSelection
import com.cornellappdev.score.theme.Style.title
import com.cornellappdev.score.theme.White
import com.cornellappdev.score.util.gameList
import com.cornellappdev.score.util.sportSelectionList
import com.cornellappdev.score.viewmodel.HomeUiState
import com.cornellappdev.score.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navigateToGameDetails: (Boolean) -> Unit = {}
) {
    val uiState = homeViewModel.collectUiStateValue()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
        modifier = Modifier
            .background(Color.White)
    ) {
        when (uiState.loadedState) {
            is ApiResponse.Loading -> {
                LoadingScreen("Loading Upcoming...", "Loading Schedules...")
            }

            is ApiResponse.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    ErrorState({ homeViewModel.onRefresh() }, "Oops! Schedules failed to load.")
                    Spacer(modifier = Modifier.height(70.dp))
                }
            }

            is ApiResponse.Success -> {
                HomeContent(
                    uiState = uiState,
                    onGenderSelected = { homeViewModel.onGenderSelected(it) },
                    onSportSelected = { homeViewModel.onSportSelected(it) },
                    navigateToGameDetails = navigateToGameDetails
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onGenderSelected: (GenderDivision) -> Unit,
    onSportSelected: (SportSelection) -> Unit,
    navigateToGameDetails: (Boolean) -> Unit = {}
) {
    LazyColumn(contentPadding = PaddingValues(top = 24.dp, start = 24.dp, end = 24.dp)) {
        item {
            GamesCarousel(uiState.upcomingGames, GamesCarouselVariant.UPCOMING)
        }
        stickyHeader {
            Column(modifier = Modifier.background(White)) {
                Spacer(Modifier.height(24.dp))
                Text(
                    text = "Game Schedule",
                    style = title,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                SportSelectorHeader(
                    sports = uiState.selectionList,
                    selectedGender = uiState.selectedGender,
                    selectedSport = uiState.sportSelect,
                    onGenderSelected = onGenderSelected,
                    onSportSelected = onSportSelected,
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
        items(uiState.filteredGames) {
            val game = it
            GameCard(
                teamLogo = game.teamLogo,
                team = game.team,
                date = game.dateString,
                isLive = game.isLive,
                genderIcon = painterResource(game.genderIcon),
                sportIcon = painterResource(game.sportIcon),
                location = game.location,
                topCornerRound = true,
                onClick = navigateToGameDetails
            )
            Spacer(modifier = Modifier.height(16.dp))
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
                selectionList = sportSelectionList,
                loadedState = ApiResponse.Success(gameList)
            ),
            onGenderSelected = {},
            onSportSelected = {}
        )
    }
}

