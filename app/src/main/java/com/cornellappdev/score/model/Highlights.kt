package com.cornellappdev.score.model

data class VideoHighlightData(
    val title: String,
    val thumbnailImageUrl: String,
    val videoUrl: String,
    val date: String,
    val sport: Sport,
    val gender: GenderDivision
)

data class ArticleHighlightData(
    val title: String,
    val imageUrl: String,
    val articleUrl: String,
    val date: String,
    val sport: Sport
)

sealed class HighlightData {
    data class Video(val data: VideoHighlightData) : HighlightData()
    data class Article(val data: ArticleHighlightData) : HighlightData()
}