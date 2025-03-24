package com.cornellappdev.score.util

import com.cornellappdev.score.model.GameData
import com.cornellappdev.score.model.TeamBoxScore
import com.cornellappdev.score.model.TeamScore

fun toGameData(
    scoreBreakdown: List<List<String?>?>?,
    team1: TeamBoxScore,
    team2: TeamBoxScore
): GameData {
    fun convertScores(scoreList: List<String?>?): Pair<List<Int>, Int> {
        if (scoreList == null || scoreList.size < 2) return Pair(emptyList(), 0)

        val scoresByPeriod = scoreList
            .subList(0, scoreList.size - 1)
            .map {
                when {
                    it == null -> 0
                    it.uppercase() == "X" -> 0
                    else -> it.toIntOrNull() ?: 0
                }
            }

        val totalScore = when (val totalLast = scoreList.last()) {
            null -> 0
            else -> totalLast.toIntOrNull() ?: 0
        }

        return Pair(scoresByPeriod, totalScore)
    }

    val (team1Scores, team1Total) = if (!scoreBreakdown.isNullOrEmpty() && scoreBreakdown.isNotEmpty())
        convertScores(scoreBreakdown[0])
    else Pair(emptyList(), 0)

    val (team2Scores, team2Total) = if (scoreBreakdown != null && scoreBreakdown.size > 1)
        convertScores(scoreBreakdown[1])
    else Pair(emptyList(), 0)

    val team1Score = TeamScore(team = team1, scoresByPeriod = team1Scores, totalScore = team1Total)
    val team2Score = TeamScore(team = team2, scoresByPeriod = team2Scores, totalScore = team2Total)

    return GameData(teamScores = Pair(team1Score, team2Score))
}


