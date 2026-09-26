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
    val boxScore: List<GameDetailsBoxScore?>? = null
)
