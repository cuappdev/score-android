package com.cornellappdev.score.model

// TODO Refactor to make easier to filter... actual gender, etc.
data class GameCardData(
    val teamLogo: Int,
    val team: String,
    val date: String,
    val location: String,
    val genderIcon: Int,
    val sportIcon: Int,
)
