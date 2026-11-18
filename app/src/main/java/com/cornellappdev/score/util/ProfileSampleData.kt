package com.cornellappdev.score.util

import com.cornellappdev.score.model.GenderDivision
import com.cornellappdev.score.model.HighlightData
import com.cornellappdev.score.model.ProfileData
import com.cornellappdev.score.model.ProfileFavorite
import com.cornellappdev.score.model.Sport

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
            sampleVideoHighlightData
        ),
        HighlightData.Article(
            sampleArticleHighlightData
        )
    )
)
