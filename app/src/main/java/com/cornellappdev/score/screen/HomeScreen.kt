package com.cornellappdev.score.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.score.components.ButtonPrimary
import com.cornellappdev.score.components.EmptyStateBox
import com.cornellappdev.score.components.ErrorState
import com.cornellappdev.score.components.ExpandableSection
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navigateToGameDetails: (String) -> Unit = {}
) {
    val uiState = homeViewModel.collectUiStateValue()
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
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
                    onRefresh = { homeViewModel.onRefresh() },
                    onAdvFilterClick = { showBottomSheet = true }
                )
            }
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(
                        top = 32.dp,
                        bottom = 24.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    item {
                        ExpandableSection(
                            title = "Price",
                            options = listOf("Unticketed", "Under $20", "Under $50", "Over $50")
                        )
                    }
                    item {
                        ExpandableSection(
                            title = "Location",
                            options = listOf("On Campus", "1-2 Hours", "2-4 Hours", "Over 4 Hours")
                        )
                    }
                    item {
                        ExpandableSection(
                            title = "Date of Game",
                            options = listOf(
                                "Today",
                                "Within 7 Days",
                                "Within a Month",
                                "Over a Month"
                            )
                        )
                    }
                    item {
                        ButtonPrimary(
                            text = "Apply",
                            icon = null,
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                // TODO: Apply filter logic via ViewModel
                                showBottomSheet = false
                            }
                        )
                    }
                    item {
                        Text(
                            "Reset", fontSize = 14.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // TODO: Reset filter logic
                                    showBottomSheet = false
                                },
                            textAlign = TextAlign.Center
                        )
                    }
                }
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
    navigateToGameDetails: (String) -> Unit = {},
    onAdvFilterClick: () -> Unit
) {
    ScorePullToRefreshBox(isRefreshing = uiState.loadedState == ApiResponse.Loading, onRefresh) {
        HomeLazyColumn(
            uiState,
            onGenderSelected,
            onSportSelected,
            navigateToGameDetails,
            onAdvFilterClick
        )
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun HomeLazyColumn(
    uiState: HomeUiState,
    onGenderSelected: (GenderDivision) -> Unit,
    onSportSelected: (SportSelection) -> Unit,
    navigateToGameDetails: (String) -> Unit,
    onAdvFilterClick: () -> Unit
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Game Schedule",
                        style = title,
                    )
                    ButtonPrimary(
                        "",
                        painterResource(id = com.cornellappdev.score.R.drawable.advanced_filter)
                    ) {
                        onAdvFilterClick()
                    }
                }
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
            onAdvFilterClick = {}
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
        onRefresh = {},
        onAdvFilterClick = {}
    )
}

