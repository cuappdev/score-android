package com.cornellappdev.score.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.score.components.EmptyStateBox
import com.cornellappdev.score.components.ErrorState
import com.cornellappdev.score.components.GameCard
import com.cornellappdev.score.components.GamesCarousel
import com.cornellappdev.score.components.LoadingScreen
import com.cornellappdev.score.components.ScorePreview
import com.cornellappdev.score.components.ScorePullToRefreshBox
import com.cornellappdev.score.components.SportSelectorHeader
import com.cornellappdev.score.model.ApiResponse
import com.cornellappdev.score.model.GenderDivision
import com.cornellappdev.score.model.SportSelection
import com.cornellappdev.score.theme.GrayPrimary
import com.cornellappdev.score.theme.GrayStroke
import com.cornellappdev.score.theme.Style.heading1
import com.cornellappdev.score.theme.Style.title
import com.cornellappdev.score.theme.White
import com.cornellappdev.score.util.gameList
import com.cornellappdev.score.util.sportSelectionList
import com.cornellappdev.score.viewmodel.HomeUiState
import com.cornellappdev.score.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navigateToGameDetails: (String) -> Unit = {}
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
                ErrorState({ homeViewModel.onRefresh() }, "Oops! Schedules failed to load.")
            }

            is ApiResponse.Success -> {
                HomeContent(
                    uiState = uiState,
                    onGenderSelected = { homeViewModel.onGenderSelected(it) },
                    onSportSelected = { homeViewModel.onSportSelected(it) },
                    navigateToGameDetails = navigateToGameDetails,
                    onRefresh = { homeViewModel.onRefresh() }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onGenderSelected: (GenderDivision) -> Unit,
    onSportSelected: (SportSelection) -> Unit,
    onRefresh: () -> Unit,
    navigateToGameDetails: (String) -> Unit = {}
) {
    ScorePullToRefreshBox(isRefreshing = uiState.loadedState == ApiResponse.Loading, onRefresh) {
        HomeLazyColumn(uiState, onGenderSelected, onSportSelected, navigateToGameDetails)
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun HomeLazyColumn(
    uiState: HomeUiState,
    onGenderSelected: (GenderDivision) -> Unit,
    onSportSelected: (SportSelection) -> Unit,
    navigateToGameDetails: (String) -> Unit
) {
    LazyColumn(contentPadding = PaddingValues(top = 24.dp)) {
        if (uiState.filteredGames.isNotEmpty()) {
            item {
                Text(
                    text = "Upcoming",
                    style = heading1,
                    color = GrayPrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp)
                )
            }
            item {
                Spacer(Modifier.height(16.dp))
            }
        }
        if (uiState.filteredGames.isNotEmpty()) {
            item {
                GamesCarousel(uiState.upcomingGames, navigateToGameDetails)
            }
        }
        stickyHeader {
            Column(
                modifier = Modifier
                    .background(White)
                    .padding(horizontal = 24.dp)
            ) {
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
            Box(modifier = Modifier.background(White)) {
                HorizontalDivider(
                    modifier = Modifier.padding(top = 16.dp),
                    color = GrayStroke,
                )
            }
        }

        if (uiState.filteredGames.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
            items(uiState.filteredGames) {
                val game = it
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    GameCard(
                        teamLogo = game.teamLogo,
                        team = game.team,
                        date = game.dateString,
                        isLive = game.isLive,
                        genderIcon = painterResource(game.genderIcon),
                        sportIcon = painterResource(game.sportIcon),
                        location = game.location,
                        topCornerRound = true,
                        onClick = { navigateToGameDetails(game.id) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        } else {
            item {
                EmptyStateBox()
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() = ScorePreview {
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
            onSportSelected = {},
            onRefresh = {},
        )
    }
}

@Preview
@Composable
private fun HomeScreenEmptyStatePreview() = ScorePreview {
    HomeContent(
        HomeUiState(
            selectedGender = GenderDivision.ALL,
            sportSelect = SportSelection.All,
            selectionList = sportSelectionList,
            loadedState = ApiResponse.Success(emptyList())
        ),
        onGenderSelected = {},
        onSportSelected = {},
        onRefresh = {}
    )
}

