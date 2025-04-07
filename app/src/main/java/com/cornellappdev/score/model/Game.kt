package com.cornellappdev.score.model

import androidx.compose.ui.graphics.Color
import com.cornellappdev.score.R
import com.cornellappdev.score.util.outputFormatter
import com.cornellappdev.score.util.parseDateOrNull
import java.time.LocalDate

// TODO Refactor to make easier to filter... actual gender, etc.

data class Game(
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

//Data for HomeScreen game displays
data class GameCardData(
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
            val cornellWins = (cornellScore?.toFloat() ?: 0f) > (otherScore?.toFloat() ?: 0f)
            val firstWins = (cornellWins && isHome) || (!cornellWins && !isHome)
            return firstWins
        }
}

// Scoring information for a specific team, used in the box score
data class TeamScore(
    val team: Team,
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
    val team: Team,
    val eventType: String,
    val score: String,
    val description: String? = null
) {
    private val scoreTuple get() = score.split(" - ")
    val homeScore get() = scoreTuple[0]
    val awayScore get() = scoreTuple[1]
}

data class Team(
    val name: String,
    val logo: Int
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
            ?: R.drawable.ic_empty_placeholder
    )
}