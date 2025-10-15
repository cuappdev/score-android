package com.cornellappdev.score.model

data class HighlightCard(
    val title: String,
    val image: String,
    val url: String,
    val date: String,
    val sport: Sport,
    val gender: GenderDivision
)