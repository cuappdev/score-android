package com.cornellappdev.score.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.score.R
import com.cornellappdev.score.components.AdvancedFilterBottomSheet
import com.cornellappdev.score.components.EmptyStateBox
import com.cornellappdev.score.components.ErrorState
import com.cornellappdev.score.components.GameCard
import com.cornellappdev.score.components.GamesCarousel
import com.cornellappdev.score.components.LoadingScreen
import com.cornellappdev.score.components.ScorePreview
import com.cornellappdev.score.components.ScorePullToRefreshBox
import com.cornellappdev.score.components.SportSelectorHeader
import com.cornellappdev.score.components.onboarding.WelcomeBanner
import com.cornellappdev.score.model.ApiResponse
import com.cornellappdev.score.model.GameFilter
import com.cornellappdev.score.model.GenderDivision
import com.cornellappdev.score.model.ProfileFavorite
import com.cornellappdev.score.model.Sport
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
    navigateToProfile: () -> Unit,
    modifier: Modifier = Modifier,
    welcomeName: String? = null,
    isLoggedIn: Boolean = false,
    homeViewModel: HomeViewModel = hiltViewModel(),
    navigateToGameDetails: (String) -> Unit = {}
) {
    val uiState = homeViewModel.collectUiStateValue()
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
        modifier = modifier
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
                    welcomeName = welcomeName,
                    isLoggedIn = isLoggedIn,
                    onFilterSelected = { homeViewModel.onFilterSelected(it) },
                    onSportSelected = { homeViewModel.onSportSelected(it) },
                    navigateToGameDetails = navigateToGameDetails,
                    onRefresh = { homeViewModel.onRefresh() },
                    navigateToProfile = navigateToProfile
                )
            }
        }
        if (showBottomSheet) {
            AdvancedFilterBottomSheet(
                sheetState = sheetState,
                onDismiss = { showBottomSheet = false },
                onApply = { price, location, date ->
                    homeViewModel.onApplyAdvancedFilters(price, location, date)
                },
                onReset = {
                    homeViewModel.onResetAdvancedFilters()
                }
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onFilterSelected: (GameFilter) -> Unit,
    onSportSelected: (SportSelection) -> Unit,
    onRefresh: () -> Unit,
    navigateToProfile: () -> Unit,
    welcomeName: String? = null,
    isLoggedIn: Boolean = false,
    navigateToGameDetails: (String) -> Unit = {}
) {
    ScorePullToRefreshBox(isRefreshing = uiState.loadedState == ApiResponse.Loading, onRefresh) {
        HomeLazyColumn(
            uiState,
            onFilterSelected,
            onSportSelected,
            navigateToGameDetails,
            navigateToProfile,
            welcomeName,
            isLoggedIn
        )
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun HomeLazyColumn(
    uiState: HomeUiState,
    onFilterSelected: (GameFilter) -> Unit,
    onSportSelected: (SportSelection) -> Unit,
    navigateToGameDetails: (String) -> Unit,
    toProfile: () -> Unit,
    welcomeName: String? = null,
    isLoggedIn: Boolean = false
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 24.dp)
    ) {
        if (isLoggedIn) {
            item {
                WelcomeBanner(
                    name = welcomeName,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
            item {
                Spacer(Modifier.height(24.dp))
            }
        }
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

        item {
            GamesCarousel(uiState.upcomingGames, navigateToGameDetails)
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
                }
                Spacer(modifier = Modifier.height(16.dp))
                SportSelectorHeader(
                    sports = uiState.selectionList,
                    selectedFilter = uiState.selectedFilter,
                    selectedSport = uiState.sportSelect,
                    onFilterSelected = onFilterSelected,
                    onSportSelected = onSportSelected,
                    toProfile = toProfile,
                    showFavoritesInfoBanner = uiState.selectedFilter == GameFilter.FAVORITES &&
                            uiState.favoriteSports.isEmpty()
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(top = 16.dp),
                color = GrayStroke,
            )
        }

        if (uiState.filteredGames.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
            items(uiState.filteredGames) {
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    GameCard(
                        teamLogo = it.teamLogo,
                        team = it.team,
                        date = it.dateString,
                        isLive = it.isLive,
                        genderIcon = painterResource(it.genderIcon),
                        sportIcon = painterResource(it.sportIcon),
                        location = it.location,
                        topCornerRound = true,
                        onClick = { navigateToGameDetails(it.id) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
        if (uiState.filteredGames.isEmpty()) {
            item {
                EmptyStateBox(
                    icon = R.drawable.ic_speaker_gray,
                    title = "No games yet.",
                    modifier = Modifier
                        .padding(bottom = 24.dp),
                    height = 200.dp
                )
            }
        }
    }
}


@Preview(name = "Signed out · no favorites")
@Composable
private fun HomeScreenSignedOutNoFavoritesPreview() = HomeScreenPreviewContent(
    isLoggedIn = false,
    hasFavorites = false
)

@Preview(name = "Signed out · has favorites")
@Composable
private fun HomeScreenSignedOutWithFavoritesPreview() = HomeScreenPreviewContent(
    isLoggedIn = false,
    hasFavorites = true
)

@Preview(name = "Signed in · no favorites")
@Composable
private fun HomeScreenSignedInNoFavoritesPreview() = HomeScreenPreviewContent(
    isLoggedIn = true,
    hasFavorites = false
)

@Preview(name = "Signed in · has favorites")
@Composable
private fun HomeScreenSignedInWithFavoritesPreview() = HomeScreenPreviewContent(
    isLoggedIn = true,
    hasFavorites = true
)

@Composable
private fun HomeScreenPreviewContent(
    isLoggedIn: Boolean,
    hasFavorites: Boolean
) = ScorePreview {
    val favorites = if (hasFavorites) {
        setOf(ProfileFavorite(Sport.FOOTBALL, GenderDivision.MALE))
    } else {
        emptySet()
    }
    val favoriteSportSelections = listOf(SportSelection.All) +
            favorites.map { SportSelection.SportSelect(it.sport) }

    HomeContent(
        uiState = HomeUiState(
            selectedFilter = GameFilter.FAVORITES,
            sportSelect = SportSelection.All,
            selectionList = if (hasFavorites) favoriteSportSelections else sportSelectionList,
            favoriteSports = favorites,
            loadedState = ApiResponse.Success(gameList)
        ),
        onFilterSelected = {},
        onSportSelected = {},
        onRefresh = {},
        navigateToProfile = {},
        welcomeName = if (isLoggedIn) "Claire" else null,
        isLoggedIn = isLoggedIn
    )
}

@Preview(name = "Empty filtered schedule")
@Composable
private fun HomeScreenEmptyStatePreview() = ScorePreview {
    HomeContent(
        HomeUiState(
            selectedFilter = GameFilter.FAVORITES,
            sportSelect = SportSelection.SportSelect(Sport.FOOTBALL),
            selectionList = sportSelectionList,
            favoriteSports = emptySet(),
            loadedState = ApiResponse.Success(gameList)
        ),
        onFilterSelected = {},
        onSportSelected = {},
        onRefresh = {},
        navigateToProfile = {}
    )
}
