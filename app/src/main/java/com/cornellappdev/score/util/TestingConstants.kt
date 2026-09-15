package com.cornellappdev.score.util

import androidx.compose.ui.graphics.Color
import com.cornellappdev.score.R
import com.cornellappdev.score.components.LeaderboardData
import com.cornellappdev.score.model.ArticleHighlightData
import com.cornellappdev.score.model.DetailsCardData
import com.cornellappdev.score.model.GameCardData
import com.cornellappdev.score.model.GameData
import com.cornellappdev.score.model.GameDetailsBoxScore
import com.cornellappdev.score.model.GenderDivision
import com.cornellappdev.score.model.HighlightData
import com.cornellappdev.score.model.ScoreEvent
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.model.SportSelection
import com.cornellappdev.score.model.TeamBoxScore
import com.cornellappdev.score.model.TeamGameSummary
import com.cornellappdev.score.model.TeamScore
import com.cornellappdev.score.model.VideoHighlightData
import java.time.LocalDate

val PENN_GAME = GameCardData(
    id = "",
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
    id = "",
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

val team1 = TeamBoxScore(name = "Cornell")
val team2 = TeamBoxScore(name = "Yale University")

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

val shortGameTeamScore1 = TeamScore(
    team = team1,
    scoresByPeriod = listOf(13, 14),
    totalScore = 27
)

val shortGameTeamScore2 = TeamScore(
    team = team2,
    scoresByPeriod = listOf(7, 7),
    totalScore = 14
)

val mediumGameTeamScore1 = TeamScore(
    team = team1,
    scoresByPeriod = listOf(7, 7, 9, 0, 7, 7),
    totalScore = 37
)
val mediumGameTeamScore2 = TeamScore(
    team = team2,
    scoresByPeriod = listOf(13, 14, 6, 14, 13, 2),
    totalScore = 62
)

val longGameTeamScore1 = TeamScore(
    team = team1,
    scoresByPeriod = listOf(13, 14, 6, 14, 13, 2, 4, 6, 2, 2),
    totalScore = 47
)
val longGameTeamScore2 = TeamScore(
    team = team1,
    scoresByPeriod = listOf(7, 7, 9, 0, 7, 7, 9, 0, 7, 2),
    totalScore = 47
)

val extraLongGameTeamScore1 = TeamScore(
    team = team1,
    scoresByPeriod = listOf(13, 14, 6, 14, 13, 2, 4, 6, 2, 2, 3, 4),
    totalScore = 54
)

val extraLongGameTeamScore2 = TeamScore(
    team = team1,
    scoresByPeriod = listOf(7, 7, 9, 0, 7, 7, 9, 0, 7, 2, 1, 1),
    totalScore = 49
)

val gameData = GameData(teamScores = Pair(teamScore1, teamScore2))

val shortGameData = GameData(teamScores = shortGameTeamScore1 to shortGameTeamScore2)

val mediumGameData = GameData(teamScores = mediumGameTeamScore1 to mediumGameTeamScore2)

val longGameData = GameData(teamScores = longGameTeamScore1 to longGameTeamScore2)

val extraLongGameData = GameData(teamScores = extraLongGameTeamScore1 to extraLongGameTeamScore2)

val team3 = TeamGameSummary(
    name = "Cornell",
    "https://cornellbigred.com/images/logos/penn_200x200.png?width=80&height=80&mode=max"
)
val team4 = TeamGameSummary(
    name = "Yale University",
    "https://cornellbigred.com/images/logos/penn_200x200.png?width=80&height=80&mode=max"
)
val scoreEvents1 = listOf(
    ScoreEvent(
        id = 1,
        time = "6:21",
        quarter = "1st Quarter",
        team = team3,
        eventType = "Field Goal",
        score = "10 - 7"
    ),
    ScoreEvent(
        id = 2,
        time = "8:40",
        quarter = "1st Quarter",
        team = team4,
        eventType = "Touchdown",
        score = "7 - 7"
    ),
    ScoreEvent(
        id = 3,
        time = "11:29",
        quarter = "1st Quarter",
        team = team3,
        eventType = "Touchdown",
        score = "7 - 0"
    )
)
val scoreEvents2 = listOf(
    ScoreEvent(
        id = 1,
        time = "6:21",
        quarter = "1st Quarter",
        team = team3,
        eventType = "Field Goal",
        score = "10 - 7",
        description = "Zhao, Alan field goal attempt from 24 GOOD"
    ),
    ScoreEvent(
        id = 2,
        time = "8:40",
        quarter = "1st Quarter",
        team = team4,
        eventType = "Touchdown",
        score = "7 - 7",
        description = "McCaughey, Brogan right pass complete to Yates, Ry for 8 yards to the COROO, TOUCHDOWN. (Conforti, Nick kick attempt good.)"
    ),
    ScoreEvent(
        id = 3,
        time = "11:29",
        quarter = "1st Quarter",
        team = team3,
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
    SportSelection.SportSelect(Sport.EQUESTRIAN),
    SportSelection.SportSelect(Sport.FENCING),
    SportSelection.SportSelect(Sport.FIELD_HOCKEY)
)

val recentSearchList = listOf("Columbia", "Men's ice hockey", "Late goal lifts No.6 men’s hockey")

//Mixed type
val highlightsList = listOf(
    HighlightData.Video
        (
        VideoHighlightData(
            "vs Columbia",
            "maxresdefault.jpg",
            "https://cornellsun.com/article/london-mcdavid-is-making-a-name-for-herself-at-cornell",
            "11/09",
            Sport.BASEBALL,
            GenderDivision.MALE
        )
    ),
    HighlightData.Article
        (
        ArticleHighlightData(
            "Late Goal Lifts No. 6 Men’s Hockey Over Brown",
            "maxresdefault.jpg",
            "https://cornellsun.com/article/london-mcdavid-is-making-a-name-for-herself-at-cornell",
            "11/09",
            Sport.ICE_HOCKEY
        )
    ),
    HighlightData.Video
        (
        VideoHighlightData(
            "vs Columbia",
            "maxresdefault.jpg",
            "https://cornellsun.com/article/london-mcdavid-is-making-a-name-for-herself-at-cornell",
            "11/9",
            Sport.BASEBALL,
            GenderDivision.MALE
        )
    ),
    HighlightData.Article
        (
        ArticleHighlightData(
            "Late Goal Lifts No. 6 Men’s Hockey Over Brown",
            "maxresdefault.jpg",
            "https://cornellsun.com/article/london-mcdavid-is-making-a-name-for-herself-at-cornell",
            "11/09",
            Sport.ICE_HOCKEY
        )
    ),
    HighlightData.Video
        (
        VideoHighlightData(
            "vs Columbia",
            "maxresdefault.jpg",
            "https://cornellsun.com/article/london-mcdavid-is-making-a-name-for-herself-at-cornell",
            "11/9",
            Sport.BASEBALL,
            GenderDivision.MALE
        )
    )
)

/*GAME DETAILS SCREEN*/
val sampleDetailsCardData = DetailsCardData(
    title = "Championship Game",
    opponentLogo = "https://example.com/logo.png",
    opponent = "Wildcats",
    opponentColor = Color(0xFF123456),
    date = LocalDate.of(2025, 4, 20),
    time = "7:30 PM",
    dateString = "April 20, 2025",
    isPastStartTime = true,
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

val sampleIvyLeaderboardData = listOf(
    LeaderboardData(
        rank = 1,
        icon = R.drawable.cornell_logo,
        school = "Princeton",
        points = 614.5
    ),
    LeaderboardData(
        rank = 2,
        icon = R.drawable.cornell_logo,
        school = "Penn",
        points = 614.5
    ),
    LeaderboardData(
        rank = 3,
        icon = R.drawable.cornell_logo,
        school = "Harvard",
        points = 614.5
    ),
    LeaderboardData(
        rank = 4,
        icon = R.drawable.cornell_logo,
        school = "Brown",
        points = 614.5
    ),
    LeaderboardData(
        rank = 5,
        icon = R.drawable.cornell_logo,
        school = "Yale",
        points = 614.5
    ),
    LeaderboardData(
        rank = 6,
        icon = R.drawable.cornell_logo,
        school = "Cornell",
        points = 614.5
    ),
    LeaderboardData(
        rank = 7,
        icon = R.drawable.cornell_logo,
        school = "Columbia",
        points = 614.5
    )
)