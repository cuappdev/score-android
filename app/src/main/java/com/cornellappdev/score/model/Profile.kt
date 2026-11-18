package com.cornellappdev.score.model

/** UI data for the populated profile; independent of a future account API response. */
data class ProfileData(
    val name: String,
    val favorites: List<ProfileFavorite>,
    val bookmarks: List<HighlightData>
)

/** A favorite identifies a sport and division, allowing both golf teams to appear separately. */
data class ProfileFavorite(val sport: Sport, val gender: GenderDivision) {
    val storageKey: String get() = "${sport.name}:${gender.name}"

    companion object {
        fun fromStorageKey(key: String): ProfileFavorite? {
            val parts = key.split(':', limit = 2)
            if (parts.size != 2) return null
            val sport = runCatching { Sport.valueOf(parts[0]) }.getOrNull() ?: return null
            val gender = runCatching { GenderDivision.valueOf(parts[1]) }.getOrNull()
                ?: return null
            if (gender == GenderDivision.ALL) return null
            return ProfileFavorite(sport, gender)
        }
    }
}
