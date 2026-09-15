package com.cornellappdev.score.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.score.R
import com.cornellappdev.score.components.BoxScore
import com.cornellappdev.score.components.EmptyStateBox
import com.cornellappdev.score.components.ErrorState
import com.cornellappdev.score.components.GameDetailsLoadingScreen
import com.cornellappdev.score.components.GameScoreHeader
import com.cornellappdev.score.components.Leaderboard
import com.cornellappdev.score.components.NavigationHeader
import com.cornellappdev.score.components.ScorePreview
import com.cornellappdev.score.components.ScorePullToRefreshBox
import com.cornellappdev.score.components.ScoringSummary
import com.cornellappdev.score.components.TimeUntilStartCard
import com.cornellappdev.score.components.highlights.ArticleHighlightCard
import com.cornellappdev.score.model.ApiResponse
import com.cornellappdev.score.model.ArticleHighlightData
import com.cornellappdev.score.model.DetailsCardData
import com.cornellappdev.score.model.ScoreEvent
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.theme.GrayMedium
import com.cornellappdev.score.theme.GrayPrimary
import com.cornellappdev.score.theme.Style.bodyNormal
import com.cornellappdev.score.theme.Style.heading1
import com.cornellappdev.score.theme.Style.heading2
import com.cornellappdev.score.theme.Style.heading3
import com.cornellappdev.score.theme.White
import com.cornellappdev.score.util.sampleDetailsCardData
import com.cornellappdev.score.util.sampleIvyLeaderboardData
import com.cornellappdev.score.viewmodel.GameDetailsViewModel

@Composable
fun GameDetailsScreen(
    modifier: Modifier = Modifier,
    gameDetailsViewModel: GameDetailsViewModel = hiltViewModel(),
    onBackArrow: () -> Unit = {},
    navigateToGameScoreSummary: (List<ScoreEvent>) -> Unit
) {
    val uiState = gameDetailsViewModel.collectUiStateValue()
    ScorePullToRefreshBox(
        // We have a separate loading state for this screen so we don't want the refresh indicator
        // to persist as the screen loads.
        false,
        gameDetailsViewModel::onRefresh,
        modifier = modifier
    ) {
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
                    GameDetailsLoadingScreen()
                }

                is ApiResponse.Error -> {
                    ErrorState(
                        { gameDetailsViewModel.onRefresh() },
                        "Oops! Details failed to load."
                    )
                }

                is ApiResponse.Success -> {
                    GameDetailsContent(
                        gameCard = state.data,
                        navigateToGameScoreSummary = navigateToGameScoreSummary
                    )
                }
            }
        }
    }
}

@Composable
fun GameDetailsContent(
    gameCard: DetailsCardData,
    modifier: Modifier = Modifier,
    navigateToGameScoreSummary: (List<ScoreEvent>) -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
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
            GameDetailsInformation(
                gameCard,
                scrollState
            )

            // render the below if the game is in the future
            // TODO: MESSY, is it every the case when there is a boxscore but no scoring summary
            if (gameCard.isPastStartTime == true) {
                Spacer(modifier = Modifier.height(24.dp))

                //TODO switch to matching for cleaner code
                if (gameCard.sport == "Swim and Dive") {
                    Leaderboard(sampleIvyLeaderboardData)
                } else if (gameCard.sport == "Equestrian") {
                    //todo extract this into another component
                    Text("Highlights", style = heading2)
                    Spacer(modifier = Modifier.height(13.dp))
                    ArticleHighlightCard(
                        ArticleHighlightData(
                            "Late Goal Lifts No. 6 Men’s Hockey Over Brown",
                            "maxresdefault.jpg",
                            "https://cornellsun.com/article/london-mcdavid-is-making-a-name-for-herself-at-cornell",
                            "11/09",
                            Sport.ICE_HOCKEY
                        ),
                        isWideFormat = false
                    )
                } else {
                    //if (gameCard.scoreBreakdown?.isNotEmpty() == true) {
                    BoxScore(gameCard.gameData)
                    Spacer(modifier = Modifier.height(24.dp))
                    // }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Scoring Summary", fontSize = 18.sp,
                            style = heading2,
                        )
                        if (gameCard.boxScore.isNotEmpty()) {
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(onClick = { navigateToGameScoreSummary(gameCard.scoreEvent) }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_right_chevron),
                                    contentDescription = "Back button",
                                    modifier = Modifier
                                        .width(24.dp)
                                        .height(24.dp),
                                )
                            }
                        }
                    }
                    if (gameCard.boxScore.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        ScoringSummary(gameCard.scoreEvent)
                    } else {
                        EmptyStateBox(
                            icon = R.drawable.ic_speaker_gray,
                            title = "No scores yet.",
                            height = 200.dp
                        )
                    }
                }
            } else {
                //val context = LocalContext.current
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

//                    ButtonPrimary(
//                        "Add to Calendar",
//                        painterResource(R.drawable.ic_calendar),
//                        onClick = {
//                            gameCard.toCalendarEvent()?.let { event ->
//                                addToCalendar(context = context, event)
//                            }
//                        }
//                    )

                }
            }
        }
    }
}

@Composable
private fun GameDetailsInformation(
    gameCard: DetailsCardData,
    scrollState: ScrollState
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = gameCard.sport,
            style = heading3.copy(color = GrayPrimary)
        )
        Text(
            text = gameCard.title,
            style = heading1.copy(color = GrayPrimary),
            maxLines = 1,
            modifier = Modifier
                .horizontalScroll(scrollState)
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
    }
}

@Preview
@Composable
private fun GameDetailsInfoPreview() = ScorePreview {
    val scrollState = rememberScrollState()
    GameDetailsInformation(
        sampleDetailsCardData, scrollState = scrollState
    )
}


@Preview
@Composable
private fun GameDetailsPreStartPreview() {
    GameDetailsContent(
        sampleDetailsCardData.copy(isPastStartTime = false), navigateToGameScoreSummary = {}
    )
}

@Preview
@Composable
private fun GameDetailsAfterStartPreview() {
    GameDetailsContent(
        sampleDetailsCardData, navigateToGameScoreSummary = {}
    )
}

@Preview
@Composable
private fun EmptyGameDetailsLeaderboardPreview() {
    GameDetailsContent(
        sampleDetailsCardData.copy(
            sport = "Swim and Dive",
            boxScore = emptyList(),
            scoreBreakdown = listOf(
                emptyList(),
                emptyList()
            ),
            scoreEvent = emptyList()
        ), navigateToGameScoreSummary = {}
    )
}

@Preview
@Composable
private fun EmptyGameDetailsHighlightPreview() {
    GameDetailsContent(
        sampleDetailsCardData.copy(
            sport = "Equestrian",
            boxScore = emptyList(),
            scoreBreakdown = listOf(
                emptyList(),
                emptyList()
            ),
            scoreEvent = emptyList()
        ), navigateToGameScoreSummary = {}
    )
}