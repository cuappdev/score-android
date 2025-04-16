package com.cornellappdev.score.util

import com.cornellappdev.score.model.GameData
import com.cornellappdev.score.model.TeamBoxScore
import com.cornellappdev.score.model.TeamScore

/**
 * Converts a list of score strings into a list of integers by period and a total score.
 *
 * Handles null values and non-numeric values:
 * - Nulls or "X" are 0
 * - If the sport is baseball, only the first 9 periods counted
 * - For other sports, the last item in the list is treated as the total score.
 *
 * @param scoreList the list of score strings, where each item represents a period score and the last item may be the total
 * @param sport the sport type, used to apply specific rules
 * @return a pair where the first value is a list of parsed period scores and the second is the total score (or null if invalid)
 */
// TODO: ASK ABOUT OT. Other sports might be added.
fun convertScores(scoreList: List<String?>?, sport: String): Pair<List<Int>, Int?> {
    if (scoreList == null || scoreList.size < 2) return Pair(emptyList(), null)

    var scoresByPeriod = scoreList
        .subList(0, scoreList.size - 1)
        .map {
            when {
                it == null -> 0
                it.uppercase() == "X" -> 0
                else -> it.toIntOrNull() ?: 0
            }
        }

    if (sport.lowercase() == "baseball") {
        scoresByPeriod = scoresByPeriod.take(9)
        val totalScore = scoresByPeriod.sum()
        return Pair(scoresByPeriod, totalScore)
    }

    val totalScore = scoreList.last()?.toIntOrNull()
    return Pair(scoresByPeriod, totalScore)
}

/**
 * Converts score breakdowns and team box scores into a GameData object.
 *
 * Uses convertScores to parse individual team scores. If a score breakdown is missing or invalid,
 * returns empty scores and zero totals.
 *
 * @param scoreBreakdown a list containing two lists of score strings, one for each team
 * @param team1 the first team's box score information
 * @param team2 the second team's box score information
 * @param sport the sport type, passed through to convertScores
 * @return a GameData object containing structured scores for both teams
 */
fun toGameData(
    scoreBreakdown: List<List<String?>?>?,
    team1: TeamBoxScore,
    team2: TeamBoxScore,
    sport: String
): GameData {
    val (team1Scores, team1Total) = scoreBreakdown?.getOrNull(0)?.let {
        convertScores(it, sport)
    } ?: (emptyList<Int>() to null)

    val (team2Scores, team2Total) = scoreBreakdown?.getOrNull(1)?.let {
        convertScores(it, sport)
    } ?: (emptyList<Int>() to null)

    val team1Score =
        TeamScore(team = team1, scoresByPeriod = team1Scores, totalScore = team1Total ?: 0)
    val team2Score =
        TeamScore(team = team2, scoresByPeriod = team2Scores, totalScore = team2Total ?: 0)

    return GameData(teamScores = Pair(team1Score, team2Score))
}

/**
 * Parses a result string into a pair of home and opponent scores.
 *
 * Expected format: "Some text,HOME-OPP", where HOME and OPP are integers.
 * If the format is invalid or parsing fails, returns null.
 *
 * @param result the result string to parse, e.g., "L,3-2"
 * @return a pair of (homeScore, oppScore) if parsing succeeds, or null if the format is invalid
 */
fun parseResultScore(result: String?): Pair<Int, Int>? {
    if (result.isNullOrBlank()) return null

    val parts = result.split(",")
    if (parts.size != 2) return null

    val scorePart = parts[1].split("-")
    if (scorePart.size != 2) return null

    val homeScore = scorePart[0].toIntOrNull()
    val oppScore = scorePart[1].toIntOrNull()

    if (homeScore != null && oppScore != null) {
        return Pair(homeScore, oppScore)
    } else {
        return null
    }
}

val validSports = setOf(
    "Baseball", "Basketball", "Field Hockey",
    "Football", "Ice Hockey", "Lacrosse", "Soccer"
)

fun isValidSport(sportName: String): Boolean {
    return sportName in validSports
}

