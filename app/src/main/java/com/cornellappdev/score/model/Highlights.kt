package com.cornellappdev.score.model

import com.cornellappdev.score.util.isValidSport
import com.example.score.HighlightsQuery

data class VideoHighlightData(
    val title: String,
    val thumbnailImageUrl: String,
    val videoUrl: String,
    val date: String,
    val sport: Sport?,
    val gender: GenderDivision?,
    val duration: String?
)

data class ArticleHighlightData(
    val title: String,
    val imageUrl: String,
    val articleUrl: String,
    val date: String,
    val sport: Sport?
)

sealed class HighlightData {
    abstract val title: String
    abstract val date: String
    abstract val sport: Sport?

    data class Video(val data: VideoHighlightData) : HighlightData() {
        override val title = data.title
        override val date = data.date
        override val sport = data.sport
    }

    data class Article(val data: ArticleHighlightData) : HighlightData() {
        override val title = data.title
        override val date = data.date
        override val sport = data.sport
    }
}

fun HighlightsQuery.YoutubeVideo.toHighlightData(): HighlightData? {
    val sportName = sportsType ?: return null
    if (!isValidSport(sportName)) return null

    return HighlightData.Video(
        VideoHighlightData(
            title = title,
            thumbnailImageUrl = thumbnail,
            videoUrl = url,
            date = publishedAt,
            sport = Sport.fromDisplayName(sportName),
            gender = if (title.contains("Men's")) {
                GenderDivision.MALE
            } else {
                GenderDivision.FEMALE
            },
            duration = duration
        )
    )
}

fun HighlightsQuery.Article.toHighlightData(): HighlightData? {
    val sportName = sportsType
    if (!isValidSport(sportName)) return null

    return HighlightData.Article(
        ArticleHighlightData(
            title = title,
            imageUrl = image ?: return null,
            articleUrl = url,
            date = publishedAt,
            sport = Sport.fromDisplayName(sportName)
        )
    )
}