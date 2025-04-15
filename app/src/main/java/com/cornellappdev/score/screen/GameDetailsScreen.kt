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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.score.R
import com.cornellappdev.score.components.BoxScore
import com.cornellappdev.score.components.ButtonPrimary
import com.cornellappdev.score.components.GameScoreHeader
import com.cornellappdev.score.components.NavigationHeader
import com.cornellappdev.score.components.ScorePullToRefreshBox
import com.cornellappdev.score.components.TimeUntilStartCard
import com.cornellappdev.score.model.ApiResponse
import com.cornellappdev.score.model.DetailsCardData
import com.cornellappdev.score.model.GameData
import com.cornellappdev.score.model.GameDetailsBoxScore
import com.cornellappdev.score.model.ScoreEvent
import com.cornellappdev.score.model.TeamBoxScore
import com.cornellappdev.score.model.TeamGameSummary
import com.cornellappdev.score.model.TeamScore
import com.cornellappdev.score.theme.GrayMedium
import com.cornellappdev.score.theme.GrayPrimary
import com.cornellappdev.score.theme.Style.bodyNormal
import com.cornellappdev.score.theme.Style.heading1
import com.cornellappdev.score.theme.Style.heading2
import com.cornellappdev.score.theme.Style.heading3
import com.cornellappdev.score.theme.White
import com.cornellappdev.score.util.addToCalendar
import com.cornellappdev.score.util.toCalendarEvent
import com.cornellappdev.score.viewmodel.GameDetailsViewModel
import java.time.LocalDate

@Composable
fun GameDetailsScreen(
    gameDetailsViewModel: GameDetailsViewModel = hiltViewModel(),
    onBackArrow: () -> Unit = {}
) {
    val uiState = gameDetailsViewModel.collectUiStateValue()
    ScorePullToRefreshBox(
        uiState.loadedState == ApiResponse.Loading,
        { gameDetailsViewModel.onRefresh() }) { }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .verticalScroll(rememberScrollState())
    ) {
        NavigationHeader(
            title = "Game Details",
            onBackPressed = onBackArrow
        )
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
                    gameCard = state.data
                )
            }
        }
    }
}

@Composable
fun GameDetailsContent(gameCard: DetailsCardData) {
    Column(
        modifier = Modifier
            .background(White)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TODO: add navigation
        GameScoreHeader(
            leftTeamLogo = painterResource(R.drawable.cornell_logo),
            rightTeamLogo = gameCard.opponentLogo,
            gradientColor1 = Color(0xFFE1A69F),
            gradientColor2 = gameCard.opponentColor,
            leftScore = gameCard.homeScore,
            rightScore = gameCard.oppScore,
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
            Spacer(modifier = Modifier.height(13.5.dp))

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
                Icon(
                    painter = painterResource(id = R.drawable.ic_time),
                    contentDescription = "Time Icon",
                    modifier = Modifier
                        .size(24.dp),
                    tint = GrayMedium
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = gameCard.dateString, style = bodyNormal.copy(color = GrayPrimary))
            }

            // render the below if the game is in the future
            // TODO: MESSY, is it every the case when there is a boxscore but no scoring summary
            if (gameCard.isPastStartTime) {
                if (gameCard.scoreBreakdown?.isNotEmpty() == true) {
                    Spacer(modifier = Modifier.height(24.dp))
                    BoxScore(gameCard.gameData)
                    Spacer(modifier = Modifier.height(24.dp))
                }
                if (gameCard.boxScore.isNotEmpty()) {
                    Text(
                        "Scoring Summary", fontSize = 18.sp,
                        style = heading2,
                    ) // TODO: NAVIGATION
                    Spacer(modifier = Modifier.height(16.dp))
                    ScoringSummary(gameCard.scoreEvent)
                } else {
                    Text("No Scoring Summary") // TODO: Make state when there are no scores
                }
            } else {
                val context = LocalContext.current
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(40.dp))

                    if (gameCard.daysUntilGame != null && gameCard.hoursUntilGame != null) {
                        TimeUntilStartCard(
                            gameCard.daysUntilGame,
                            gameCard.hoursUntilGame
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    ButtonPrimary(
                        "Add to Calendar",
                        painterResource(R.drawable.ic_calendar),
                        onClick = {
                            gameCard.toCalendarEvent()?.let { event ->
                                addToCalendar(context = context, event)
                            }
                        }
                    )
                }

            }
        }
    }
}

@Preview
@Composable
private fun GameDetailsPreview() {
    GameDetailsContent(
        DetailsCardData(
            title = "Championship Game",
            opponentLogo = "https://example.com/logo.png",
            opponent = "Wildcats",
            opponentColor = Color(0xFF123456),
            date = LocalDate.of(2025, 4, 20),
            time = "7:30 PM",
            dateString = "April 20, 2025",
            isPastStartTime = false,
            location = "Main Stadium",
            locationString = "Main Stadium, Cityville",
            gender = "Men's",
            genderIcon = 123, // Dummy resource ID
            sport = "Basketball",
            sportIcon = 456, // Dummy resource ID
            boxScore = listOf(
                GameDetailsBoxScore(
                    team = "Tigers",
                    period = "1st",
                    time = "12:34",
                    description = "3-point shot",
                    scorer = "John Doe",
                    assist = "Mike Smith",
                    scoreBy = "Tigers",
                    corScore = 21,
                    oppScore = 18
                ),
                GameDetailsBoxScore(
                    team = "Wildcats",
                    period = "1st",
                    time = "10:01",
                    description = "Layup",
                    scorer = "Jane Roe",
                    assist = "Tom Lee",
                    scoreBy = "Wildcats",
                    corScore = 21,
                    oppScore = 20
                )
            ),
            scoreBreakdown = listOf(
                listOf("10", "15", "20", "18"), // Tigers per quarter
                listOf("12", "10", "18", "22")  // Wildcats per quarter
            ),
            gameData = GameData(
                Pair(
                    TeamScore(
                        team = TeamBoxScore(
                            name = "Tigers",
                        ),
                        scoresByPeriod = listOf(20, 18, 22, 18),
                        totalScore = 78
                    ),
                    TeamScore(
                        team = TeamBoxScore(
                            name = "Wildcats",
                        ),
                        scoresByPeriod = listOf(18, 20, 16, 21),
                        totalScore = 75
                    )
                )
            ),
            scoreEvent = listOf(
                ScoreEvent(
                    id = 1,
                    time = "11:11",
                    quarter = "2nd",
                    team = TeamGameSummary(
                        name = "Tigers",
                        logo = "https://example.com/tigers.png"
                    ),
                    eventType = "3PT",
                    score = "36-34",
                    description = "Three-pointer by John Doe"
                ),
                ScoreEvent(
                    id = 2,
                    time = "08:45",
                    quarter = "3rd",
                    team = TeamGameSummary(
                        name = "Wildcats",
                        logo = "https://example.com/wildcats.png"
                    ),
                    eventType = "FT",
                    score = "36-35",
                    description = "Free throw by Jane Roe"
                )
            ),
            daysUntilGame = 6,
            hoursUntilGame = 144,
            homeScore = 78,
            oppScore = 75
        )
    )
}
