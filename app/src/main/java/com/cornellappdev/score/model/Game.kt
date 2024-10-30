package com.cornellappdev.score.model

data class GameCardData(
    val teamLogo: Int,
    val team: String,
    val date: String,
    val location: String,
    val genderIcon: Int,
    val sportIcon: Int
)

data class Team(
    val name: String,
    val logoUrl: String
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
    val time: String,
    val quarter: String,
    val team: Team,
    val score: String,
    val description: String
)

// Complete game summary with game data and scoring events
data class GameSummary(
    val gameData: GameData,
    val scoreEvents: List<ScoreEvent>
)

// Detailed game data structure containing additional information
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
