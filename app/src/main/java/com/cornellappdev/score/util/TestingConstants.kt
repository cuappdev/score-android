package com.cornellappdev.score.util

import androidx.compose.ui.graphics.Color
import com.cornellappdev.score.R
import com.cornellappdev.score.model.GameCardData
import com.cornellappdev.score.model.GameData
import com.cornellappdev.score.model.ScoreEvent
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.model.SportSelection
import com.cornellappdev.score.model.Team
import com.cornellappdev.score.model.TeamScore
import java.time.LocalDate

val PENN_GAME = GameCardData(
    teamLogo = "https://cornellbigred.com/images/logos/penn_200x200.png?width=80&height=80&mode=max",
    team = "Penn",
    teamColor = Color(0x66B31B1B),
    date = LocalDate.now(),
    dateString = "3/1/25",
    isLive = false,
    isPast = true,
    location = "Philadelphia, PA",
    gender = "Male",
    genderIcon = R.drawable.ic_gender_men,
    sport = "Baseball",
    sportIcon = R.drawable.ic_baseball,
)

val PRINCETON_GAME = GameCardData(
    teamLogo = "https://cornellbigred.com/images/logos/Princeton_Tigers.png?width=80&height=80&mode=max",
    team = "Princeton",
    teamColor = Color(0x66FF6000),
    date = LocalDate.now(),
    dateString = "3/1/25",
    isLive = false,
    isPast = true,
    location = "Boston, MA",
    gender = "Female",
    genderIcon = R.drawable.ic_gender_men,
    sport = "Swim Dive",
    sportIcon = R.drawable.ic_swim_dive,
)
val gameList = listOf(
    PENN_GAME,
    PRINCETON_GAME,
    PENN_GAME,
    PRINCETON_GAME,
    PENN_GAME,
    PRINCETON_GAME,
    PENN_GAME,
    PRINCETON_GAME
)

val team1 = Team(name = "Cornell", R.drawable.cornell_logo)
val team2 = Team(name = "Yale", R.drawable.yale_logo)

val teamScore1 = TeamScore(
    team = team1,
    scoresByPeriod = listOf(13, 14, 6, 14),
    totalScore = 47
)
val teamScore2 = TeamScore(
    team = team2,
    scoresByPeriod = listOf(7, 7, 9, 0),
    totalScore = 23
)

val gameData = GameData(teamScores = Pair(teamScore1, teamScore2))

val scoreEvents1 = listOf(
    ScoreEvent(
        id = 1,
        time = "6:21",
        quarter = "1st Quarter",
        team = team1,
        eventType = "Field Goal",
        score = "10 - 7"
    ),
    ScoreEvent(
        id = 2,
        time = "8:40",
        quarter = "1st Quarter",
        team = team2,
        eventType = "Touchdown",
        score = "7 - 7"
    ),
    ScoreEvent(
        id = 3,
        time = "11:29",
        quarter = "1st Quarter",
        team = team1,
        eventType = "Touchdown",
        score = "7 - 0"
    )
)
val scoreEvents2 = listOf(
    ScoreEvent(
        id = 1,
        time = "6:21",
        quarter = "1st Quarter",
        team = team1,
        eventType = "Field Goal",
        score = "10 - 7",
        description = "Zhao, Alan field goal attempt from 24 GOOD"
    ),
    ScoreEvent(
        id = 2,
        time = "8:40",
        quarter = "1st Quarter",
        team = team2,
        eventType = "Touchdown",
        score = "7 - 7",
        description = "McCaughey, Brogan right pass complete to Yates, Ry for 8 yards to the COROO, TOUCHDOWN. (Conforti, Nick kick attempt good.)"
    ),
    ScoreEvent(
        id = 3,
        time = "11:29",
        quarter = "1st Quarter",
        team = team1,
        eventType = "Touchdown",
        score = "7 - 0",
        description = "Wang, Jameson left pass complete to Lee, Brendan for 34 yards to the YALOO, TOUCHDOWN. (Zhao, Alan kick attempt good.)"
    )
)

fun emptyGameData(): GameData {
    return GameData(
        teamScores = Pair(
            TeamScore(
                team = team1,
                scoresByPeriod = emptyList(),
                totalScore = 0
            ),
            TeamScore(
                team = team2,
                scoresByPeriod = emptyList(),
                totalScore = 0
            )
        )
    )
}

val sportList = listOf(
    Sport.BASEBALL,
    Sport.BASKETBALL,
    Sport.CROSS_COUNTRY,
)

val sportSelectionList = listOf(
    SportSelection.All,
    SportSelection.SportSelect(Sport.BASEBALL),
    SportSelection.SportSelect(Sport.BASKETBALL),
    SportSelection.SportSelect(Sport.CROSS_COUNTRY),
)
