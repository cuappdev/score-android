package com.cornellappdev.score.util

import com.cornellappdev.score.R
import com.cornellappdev.score.model.GameCardData
import com.cornellappdev.score.model.GameData
import com.cornellappdev.score.model.ScoreEvent
import com.cornellappdev.score.model.Team
import com.cornellappdev.score.model.TeamScore

val PENN_GAME = GameCardData(
    teamLogo = R.drawable.penn_logo,
    team = "Penn",
    date = "5/20/2024",
    location = "Philadelphia, PA",
    genderIcon = R.drawable.ic_gender_men,
    sportIcon = R.drawable.ic_baseball
)

val PRINCETON_GAME = GameCardData(
    teamLogo = R.drawable.princeton_logo,
    team = "Princeton",
    date = "5/21/2024",
    location = "Princeton, NY",
    genderIcon = R.drawable.ic_gender_men,
    sportIcon = R.drawable.ic_baseball
)
val gameList = listOf(PENN_GAME, PRINCETON_GAME)

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
    ))
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