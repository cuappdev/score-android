package com.cornellappdev.score.model

data class VideoHighlightData(
    val title: String,
    val image: String,
    val url: String,
    val date: String,
    val sport: Sport,
    val gender: GenderDivision
)

data class ArticleHighlightData(
    val title: String,
    val image: String,
    val url: String,
    val date: String
)