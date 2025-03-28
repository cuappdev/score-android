package com.cornellappdev.score.screen

import ScoringSummary
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.score.R
import com.cornellappdev.score.components.BoxScore
import com.cornellappdev.score.components.ButtonPrimary
import com.cornellappdev.score.components.GameScoreHeader
import com.cornellappdev.score.components.NavigationHeader
import com.cornellappdev.score.components.TimeUntilStartCard
import com.cornellappdev.score.model.ApiResponse
import com.cornellappdev.score.model.DetailsCardData
import com.cornellappdev.score.model.GameDetailsGame
import com.cornellappdev.score.theme.GrayMedium
import com.cornellappdev.score.theme.GrayPrimary
import com.cornellappdev.score.theme.Style.bodyNormal
import com.cornellappdev.score.theme.Style.heading1
import com.cornellappdev.score.theme.Style.heading3
import com.cornellappdev.score.theme.White
import com.cornellappdev.score.viewmodel.GameDetailsUiState
import com.cornellappdev.score.viewmodel.GameDetailsViewModel
import com.cornellappdev.score.viewmodel.HomeUiState
import com.cornellappdev.score.viewmodel.HomeViewModel

@Composable
fun GameDetailsScreen(
    gameId: String,
    gameDetailsViewModel: GameDetailsViewModel = hiltViewModel(),
    onBackArrow: () -> Unit = {})
{
    val uiState by gameDetailsViewModel.uiStateFlow.collectAsState()
    when (val state = uiState.loadedState) {
        is ApiResponse.Loading, ApiResponse.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = GrayPrimary)
            }
        }

        is ApiResponse.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Failed to load game.")
            }
        }

        is ApiResponse.Success -> {
            GameDetailsContent(
                gameCard = state.data, onBackArrow = onBackArrow
                )
        }
    }
}

@Composable
private fun GameDetailsContent(
    gameCard: DetailsCardData,
    onBackArrow: () -> Unit = {}){
    Column(
        modifier = Modifier
            .background(White)
            .fillMaxSize(),
    ) {
        NavigationHeader(title = "Game Details", onBackPressed = onBackArrow)
        Box(modifier = Modifier.height(27.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "<", // TODO correct back button
                fontSize = 18.sp,
                modifier = Modifier
                    .weight(1f)
            )
            Text(
                text = "Game Details",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(2f)
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        GameScoreHeader(
            leftTeamLogo = painterResource(R.drawable.cornell_logo),
            rightTeamLogo = gameCard.opponentLogo,
            gradientColor1 = Color(0xFFE1A69F),
            gradientColor2 = gameCard.opponentColor,
            leftScore = 0, //TODO Score
            rightScore = 0, //TODO Score
            modifier = Modifier.height(185.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = gameCard.sport,
                style = heading3.copy(color = GrayPrimary)
            )
            Text(
                text = gameCard.title,
                style = heading1.copy(color = GrayPrimary)
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = "Location Icon",
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp),
                    colorFilter = ColorFilter.tint(GrayMedium)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = gameCard.locationString, style = bodyNormal.copy(color = GrayPrimary))
                Spacer(modifier = Modifier.width(12.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_time),
                    contentDescription = "Time Icon",
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp),
                    colorFilter = ColorFilter.tint(GrayMedium)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = gameCard.dateString, style = bodyNormal.copy(color = GrayPrimary))
            }

            //render the below if the game is in the future
            if(gameCard.isPastStartTime){
                if(!gameCard.scoreBreakdown.isNullOrEmpty()){
                    BoxScore(gameCard.gameData)
                }
                if(gameCard.boxScore.isNotEmpty()){
                    ScoringSummary(gameCard.scoreEvent)
                }
                else{
                    Text("No Scoring Summary") // TODO: Make state when there are no scores
                }
            }
            else{
                Spacer(modifier = Modifier.height(40.dp))
                TimeUntilStartCard(2, 0) //TODO Timer
            }
        }
        if(!gameCard.isPastStartTime){
            Spacer(modifier = Modifier.height(84.dp))
            ButtonPrimary("Add to Calendar", painterResource(R.drawable.ic_calendar)) //TODO Calendar
        }

    }
}
@Preview
@Composable
private fun GameDetailsScreenPreview() {}