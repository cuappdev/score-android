package com.cornellappdev.score.model

// TODO Refactor to make easier to filter... actual gender, etc.
data class GameCardData(
    val teamLogo: Int,
    val team: String,
    val date: String,
    val location: String,
    val genderIcon: Int,
    val sportIcon: Int
)

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

// Individual scoring event details for the scoring summary
data class ScoreEvent(
    val id: Int,             // Unique identifier for each event
    val time: String,        // Event time (e.g., "6:21")
    val quarter: String,     // Quarter (e.g., "1st Quarter")
    val team: Team,          // Team object that includes name and logo URL
    val eventType: String,   // Event type (e.g., "Field Goal", "Touchdown")
    val score: String,        // Current score after the event (e.g., "10 - 7")
    val description: String? = null )

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