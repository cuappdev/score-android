package com.cornellappdev.score.util

import com.cornellappdev.score.model.GameData
import com.cornellappdev.score.model.TeamBoxScore
import com.cornellappdev.score.model.TeamScore

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

fun toGameData(
    scoreBreakdown: List<List<String?>?>?,
    team1: TeamBoxScore,
    team2: TeamBoxScore,
    sport: String
): GameData {
    val (team1Scores, team1Total) = if (!scoreBreakdown.isNullOrEmpty())
        convertScores(scoreBreakdown[0], sport)
    else Pair(emptyList(), null)

    val (team2Scores, team2Total) = if (scoreBreakdown != null && scoreBreakdown.size > 1)
        convertScores(scoreBreakdown[1], sport)
    else Pair(emptyList(), null)

    val team1Score =
        TeamScore(team = team1, scoresByPeriod = team1Scores, totalScore = team1Total ?: 0)
    val team2Score =
        TeamScore(team = team2, scoresByPeriod = team2Scores, totalScore = team2Total ?: 0)

    return GameData(teamScores = Pair(team1Score, team2Score))
}

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

