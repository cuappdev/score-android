package com.cornellappdev.score.model

/** UI data for the populated profile; independent of a future account API response. */
data class ProfileData(
    val name: String,
    val favorites: List<ProfileFavorite>,
    val bookmarks: List<HighlightData>
)

/** A favorite identifies a sport and division, allowing both golf teams to appear separately. */
data class ProfileFavorite(val sport: Sport, val gender: GenderDivision)
