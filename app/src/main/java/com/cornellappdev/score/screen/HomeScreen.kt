package com.cornellappdev.score.screen

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.cornellappdev.score.theme.Style.title
import com.cornellappdev.score.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val uiState = homeViewModel.collectUiStateValue()
    val filteredGames = uiState.filteredGames

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
        modifier = Modifier.statusBarsPadding()
    ) {
        when (uiState.loadedState) {
            is ApiResponse.Loading -> {
                // Show a loading indicator
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is ApiResponse.Error -> {
                // Show an error message
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
                UpcomingGamesCarousel(filteredGames)
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
                        onGenderSelected = { homeViewModel.onGenderSelected(it) },
                        onSportSelected = { homeViewModel.onSportSelected(it) }
                    )
                    LazyColumn(modifier = Modifier.padding(horizontal = 24.dp)) {
                        items(filteredGames.size) { page ->
                            val game = filteredGames[page]
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
        }
    }
//    Column(
//        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
//        modifier = Modifier.statusBarsPadding()
//    )
//    {
//        UpcomingGamesCarousel(
//            uiState.filteredGames
//        )
//        Column {
//            Text(
//                text = "Game Schedule",
//                style = title,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 24.dp, vertical = 8.dp)
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            SportSelectorHeader(
//                sports = uiState.selectionList,
//                selectedGender = uiState.selectedGender,
//                selectedSport = uiState.sportSelect,
//                onGenderSelected = {
//                    homeViewModel.onGenderSelected(it)
//                },
//                onSportSelected = {
//                    homeViewModel.onSportSelected(it)
//                }
//            )
//            LazyColumn(modifier = Modifier.padding(horizontal = 24.dp)) {
//                items(uiState.filteredGames.size) { page ->
//                    val game = uiState.filteredGames[page]
//                    SportCard(
//                        teamLogo = game.teamLogo,
//                        team = game.team,
//                        date = game.dateString,
//                        isLive = game.isLive,
//                        genderIcon = painterResource(game.genderIcon),
//                        sportIcon = painterResource(game.sportIcon),
//                        location = game.location,
//                        topCornerRound = true
//                    )
//                }
//            }
//        }
//    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
