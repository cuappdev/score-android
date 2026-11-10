package com.cornellappdev.score.screen

import com.cornellappdev.score.R
import com.cornellappdev.score.model.ArticleHighlightData
import com.cornellappdev.score.model.GenderDivision
import com.cornellappdev.score.model.HighlightData
import com.cornellappdev.score.model.ProfileData
import com.cornellappdev.score.model.ProfileFavorite
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.model.VideoHighlightData

// Temporary design fixtures. Replace ProfileScreen.data when account data is available.
val sampleProfileData = ProfileData(
    name = "Claire Yu",
    favorites = listOf(
        ProfileFavorite(Sport.GOLF, GenderDivision.MALE),
        ProfileFavorite(Sport.GOLF, GenderDivision.FEMALE),
        ProfileFavorite(Sport.GYMNASTICS, GenderDivision.FEMALE),
        ProfileFavorite(Sport.LACROSSE, GenderDivision.MALE)
    ),
    bookmarks = listOf(
        HighlightData.Video(
            VideoHighlightData(
                title = "vs. Columbia",
                thumbnailImageUrl = "android.resource://com.cornellappdev.score/${R.drawable.profile_highlight_soccer}",
                videoUrl = "https://www.youtube.com/@CornellAthletics",
                date = "11/09",
                sport = Sport.BASEBALL,
                gender = GenderDivision.MALE,
                duration = "1:25",
                finalScore = "3-0",
                photoCredit = "Photo By: Caroline Sherman"
            )
        ),
        HighlightData.Article(
            ArticleHighlightData(
                title = "Late Goal Lifts No. 6 Men’s Hockey Over Brown",
                imageUrl = "android.resource://com.cornellappdev.score/${R.drawable.profile_highlight_hockey}",
                articleUrl = "https://cornellsun.com/",
                date = "11/09",
                sport = Sport.ICE_HOCKEY
            )
        )
    )
)
