package com.cornellappdev.score.model

import androidx.compose.ui.graphics.Color
import com.cornellappdev.score.R
import com.cornellappdev.score.util.convertScores
import com.cornellappdev.score.util.formatDateTimeDisplay
import com.cornellappdev.score.util.getTimeUntilStart
import com.cornellappdev.score.util.outputFormatter
import com.cornellappdev.score.util.parseDateOrNull
import com.cornellappdev.score.util.parseDateTimeOrNull
import com.cornellappdev.score.util.parseResultScore
import com.cornellappdev.score.util.toGameData
import java.time.LocalDate
import java.time.LocalDateTime

// TODO Refactor to make easier to filter... actual gender, etc.

data class Game(
    val id: String,
    val teamName: String,
    val teamLogo: String,
    val teamColor: Color,
    val gender: String,
    val sport: String,
    val date: String,
    val city: String,
    val cornellScore: Number? = null,
    val otherScore: Number? = null,
) {
    val isPast: Boolean
        get() {
            val parsedDate = parseDateOrNull(date) ?: LocalDate.MAX
            return parsedDate < LocalDate.now()
        }
}

data class GameDetailsTeam(
    val id: String?,
    val color: Color,
    val image: String?,
    val name: String
)

data class GameDetailsBoxScore(
    val team: String?,
    val period: String?,
    val time: String?,
    val description: String?,
    val scorer: String?,
    val assist: String?,
    val scoreBy: String?,
    val corScore: Int?,
    val oppScore: Int?
)

data class GameDetailsGame(
    val id: String?,
    val city: String,
    val date: String,
    val gender: String,
    val location: String?,
    val opponentId: String,
    val result: String?,
    val sport: String,
    val state: String,
    val time: String?,
    val scoreBreakdown: List<List<String?>?>?,
    val team: GameDetailsTeam?,
    val boxScore: List<GameDetailsBoxScore>?
)


// Data for HomeScreen game displays
data class GameCardData(
    val id: String,
    val teamLogo: String,
    val team: String,
    val teamColor: Color,
    val date: LocalDate?,
    val dateString: String,
    val isLive: Boolean,
    val isPast: Boolean,
    val location: String,
    val gender: String,
    val genderIcon: Int,
    val sport: String,
    val sportIcon: Int,
    val isHome: Boolean = location == "Ithaca, NY",
    val cornellScore: Number? = null,
    val otherScore: Number? = null,
) {
    val firstTeamListedWins: Boolean
        get() {
            if (cornellScore == null && otherScore == null) return false

            val cornellWins = (cornellScore?.toFloat() ?: 0f) > (otherScore?.toFloat() ?: 0f)
            val firstWins = (cornellWins && isHome) || (!cornellWins && !isHome)
            return firstWins
        }
    val secondTeamListedWins: Boolean
        get() =
            (cornellScore != null || otherScore != null) && !firstTeamListedWins
}

// Data for GameDetailsScreen
data class DetailsCardData(
    val title: String,
    val opponentLogo: String,
    val opponent: String,
    val opponentColor: Color,
    val date: LocalDate?,
    val time: String,
    val dateString: String,
    val isPastStartTime: Boolean,
    val location: String,
    val locationString: String,
    val gender: String,
    val genderIcon: Int,
    val sport: String,
    val sportIcon: Int,
    val boxScore: List<GameDetailsBoxScore?>,
    val scoreBreakdown: List<List<String?>?>?,
    val gameData: GameData,
    val scoreEvent: List<ScoreEvent>,
    val daysUntilGame: Int?,
    val hoursUntilGame: Int?,
    val homeScore: Int,
    val oppScore: Int
)

// Scoring information for a specific team, used in the box score
data class TeamScore(
    val team: TeamBoxScore,
    val scoresByPeriod: List<Int>,
    val totalScore: Int
)

// Aggregated game data showing scores for both teams
data class GameData(
    val teamScores: Pair<TeamScore, TeamScore>
)

/**
 * Represents a scoring event in a game.
 *
 * @property id Unique identifier for each event.
 * @property time Event time (e.g., "6:21").
 * @property quarter Quarter of the game (e.g., "1st Quarter").
 * @property team Team object that includes name and logo URL.
 * @property eventType Type of scoring event (e.g., "Field Goal", "Touchdown").
 * @property score Current score after the event (e.g., "10 - 7").
 * @property description Optional detailed description of the event.
 */
data class ScoreEvent(
    val id: Int,
    val time: String,
    val quarter: String,
    val team: TeamGameSummary,
    val eventType: String,
    val score: String,
    val description: String? = null
) {
    private val scoreTuple get() = score.split(" - ")
    val homeScore get() = scoreTuple[0]
    val awayScore get() = scoreTuple[1]
}

data class TeamBoxScore(
    val name: String
)

data class TeamGameSummary(
    val name: String,
    val logo: String
)

data class GameSummary(
    val gameData: GameData,
    val scoreEvents: List<ScoreEvent>
)

data class GameDetail(
    val gameId: String,
    val teamScores: Pair<TeamScore, TeamScore>,
    val location: String,
    val date: String,
    val status: GameStatus,
    val countdown: Countdown? = null,
    val scoreEvents: List<ScoreEvent> = emptyList()
)

// Countdown data for the pre-game countdown timer
data class Countdown(
    val days: Int,
    val hours: Int
)

// Game status enum to represent the game's current state
enum class GameStatus {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED
}

enum class GamesCarouselVariant {
    UPCOMING,
    PAST
}

fun Game.toGameCardData(): GameCardData {
    return GameCardData(
        id = id,
        teamLogo = teamLogo,
        team = teamName,
        teamColor = teamColor,
        date = parseDateOrNull(date),
        dateString = parseDateOrNull(date)?.format(outputFormatter)
            ?: date,
        isLive = (LocalDate.now() == parseDateOrNull(date)),
        isPast = isPast,
        location = city,
        gender = gender,
        genderIcon = if (gender == "Mens") R.drawable.ic_gender_men else R.drawable.ic_gender_women,
        sport = sport,
        sportIcon = Sport.fromDisplayName(sport)?.emptyIcon
            ?: R.drawable.ic_empty_placeholder,
        cornellScore = cornellScore,
        otherScore = otherScore
    )
}

fun GameDetailsGame.toGameCardData(): DetailsCardData {
    val (daysUntil, hoursUntil) = getTimeUntilStart(date, time ?: "") ?: (null to null)
    val parsedScores = parseResultScore(result)
    return DetailsCardData(
        title = "Cornell Vs. ${team?.name ?: ""}",
        opponentLogo = team?.image ?: "",
        opponent = team?.name ?: "",
        opponentColor = team?.color ?: Color.White,
        date = parseDateOrNull(date),
        time = time ?: "",
        dateString = formatDateTimeDisplay(date, time ?: ""),
        isPastStartTime = parseDateTimeOrNull(date, time ?: "")?.let {
            !LocalDateTime.now().isBefore(it)
        } ?: false,
        location = city,
        locationString = "${city}, ${state}",
        gender = gender,
        genderIcon = if (gender == "Mens") R.drawable.ic_gender_men else R.drawable.ic_gender_women,
        sport = sport,
        sportIcon = Sport.fromDisplayName(sport)?.emptyIcon
            ?: R.drawable.ic_empty_placeholder,
        boxScore = boxScore ?: emptyList(),
        scoreBreakdown = scoreBreakdown ?: emptyList(),
        gameData = toGameData(
            scoreBreakdown = scoreBreakdown,
            team1 = TeamBoxScore("Cornell"),
            team2 = TeamBoxScore(team?.name ?: ""),
            sport = sport
        ),
        scoreEvent = boxScore?.toScoreEvents(team?.image ?: "") ?: emptyList(),
        daysUntilGame = daysUntil,
        hoursUntilGame = hoursUntil,
        homeScore = convertScores(scoreBreakdown?.getOrNull(0), sport).second
            ?: parsedScores?.first ?: 0,
        oppScore = convertScores(scoreBreakdown?.getOrNull(1), sport).second
            ?: parsedScores?.second ?: 0
    )
}

fun List<GameDetailsBoxScore>.toScoreEvents(teamLogo: String): List<ScoreEvent> {
    return this.mapIndexed { index, boxScore ->
        val teamName = boxScore.team ?: ""
        val corScore = boxScore.corScore ?: 0
        val oppScore = boxScore.oppScore ?: 0

        ScoreEvent(
            id = index,
            time = boxScore.time ?: "",
            quarter = boxScore.period ?: "",
            team = TeamGameSummary(teamName, logo = teamLogo),
            eventType = "Score", // TODO: Change to what ios has and not figma
            score = "$corScore - $oppScore",
            description = boxScore.description
        )
    }
}