package com.cornellappdev.score.screen

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.graphics.Color
import com.cornellappdev.score.components.GameCard
import com.cornellappdev.score.components.SportSelectorHeader
import com.cornellappdev.score.components.GamesCarousel
import com.cornellappdev.score.components.PastGameCard
import com.cornellappdev.score.model.ApiResponse
import com.cornellappdev.score.model.GameCardData
import com.cornellappdev.score.model.GamesCarouselVariant
import com.cornellappdev.score.model.GenderDivision
import com.cornellappdev.score.model.SportSelection
import com.cornellappdev.score.theme.Style.heading1
import com.cornellappdev.score.theme.Style.title
import com.cornellappdev.score.theme.White
import com.cornellappdev.score.viewmodel.HomeUiState
import com.cornellappdev.score.viewmodel.HomeViewModel
import com.cornellappdev.score.viewmodel.PastGamesUiState
import com.cornellappdev.score.viewmodel.PastGamesViewModel

@Composable
fun PastGamesScreen(
    pastGamesViewModel: PastGamesViewModel = hiltViewModel(),
    navigateToGameDetails: (Boolean) -> Unit = {}
) {
    val uiState = pastGamesViewModel.collectUiStateValue()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
        modifier = Modifier
            .statusBarsPadding()
            .background(Color.White)
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
                PastGamesContent(
                    uiState = uiState,
                    onGenderSelected = { pastGamesViewModel.onGenderSelected(it) },
                    onSportSelected = { pastGamesViewModel.onSportSelected(it) },
                    navigateToGameDetails = navigateToGameDetails
                )
            }
        }
    }
}

@Composable
private fun PastGamesContent(
    uiState: PastGamesUiState,
    onGenderSelected: (GenderDivision) -> Unit,
    onSportSelected: (SportSelection) -> Unit,
    navigateToGameDetails: (Boolean) -> Unit = {}
) {
    GamesCarousel(uiState.pastGames, GamesCarouselVariant.PAST)
    Column {
        Text(
            text = "All Scores",
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
                PastGameCard(
                    data = game,
                    onClick = navigateToGameDetails
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}