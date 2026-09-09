package com.cornellappdev.score.model

import kotlinx.serialization.Serializable

@Serializable
data class SocketGameUpdateEnvelope(
    val type: String,
    val gameId: String,
    val timestamp: String,
    val data: SocketGameUpdateData
)

@Serializable
data class SocketGameUpdateData(
    val homeScore: Int? = null,
    val oppScore: Int? = null,
    val scoreBreakdown: List<List<String?>?>? = null,
    val boxScore: List<SocketBoxScoreEntry?>? = null
)

@Serializable
data class SocketBoxScoreEntry(
    val team: String? = null,
    val period: String? = null,
    val time: String? = null,
    val description: String? = null,
    val scorer: String? = null,
    val assist: String? = null,
    val scoreBy: String? = null,
    val corScore: Int? = null,
    val oppScore: Int? = null
)

fun SocketBoxScoreEntry.toGameDetailsBoxScore() = GameDetailsBoxScore(
    team = team, period = period, time = time, description = description,
    scorer = scorer, assist = assist, scoreBy = scoreBy, corScore = corScore, oppScore = oppScore
)
