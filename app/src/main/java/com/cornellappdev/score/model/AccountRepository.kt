package com.cornellappdev.score.model

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.http.HttpHeader
import com.cornellappdev.score.auth.AuthRepository
import com.example.score.AccountProfileQuery
import com.example.score.AddBookmarkedHighlightMutation
import com.example.score.AddFavoriteTeamMutation
import com.example.score.HighlightCatalogQuery
import com.example.score.RemoveBookmarkedHighlightMutation
import com.example.score.RemoveFavoriteTeamMutation
import com.example.score.fragment.AccountHighlight
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Singleton
class AccountRepository
@Inject
constructor(
    private val client: ApolloClient,
    private val auth: AuthRepository,
    private val preferences: OnboardingPreferencesRepository,
) {
    private val favoritesMutex = Mutex()

    suspend fun profile(): ProfileData {
        val result = client.query(AccountProfileQuery()).httpHeaders(headers()).execute().checked()
        val data = checkNotNull(result.data)
        val favorites =
            data.me.favoriteTeams.filterNotNull().mapNotNull {
                val sport = sportFromBackend(it.sport) ?: return@mapNotNull null
                val gender =
                    when (it.gender) {
                        "Mens" -> GenderDivision.MALE
                        "Womens" -> GenderDivision.FEMALE
                        else -> return@mapNotNull null
                    }
                ProfileFavorite(sport, gender)
            }
        preferences.saveFavoriteSports(favorites.toSet())
        return ProfileData(
            name = data.me.name.orEmpty(),
            favorites = favorites,
            bookmarks =
                data.myBookmarkedHighlights.orEmpty().filterNotNull().mapNotNull {
                    it.accountHighlight.toHighlight()
                },
            bookmarkedHighlightIds = data.me.bookmarkedHighlightIds.filterNotNull().toSet(),
        )
    }

    // Read the server first so retries and partially completed saves converge to the chosen set.
    suspend fun saveFavorites(favorites: Set<ProfileFavorite>) =
        favoritesMutex.withLock {
            val current = profile().favorites.toSet()
            for (favorite in current - favorites) {
                val response =
                    client
                        .mutation(
                            RemoveFavoriteTeamMutation(
                                favorite.backendGender,
                                favorite.sport.backendName,
                            )
                        )
                        .httpHeaders(headers())
                        .execute()
                        .checked()
                check(response.data?.removeFavoriteTeam?.success == true)
            }
            for (favorite in favorites - current) {
                val response =
                    client
                        .mutation(
                            AddFavoriteTeamMutation(
                                favorite.backendGender,
                                favorite.sport.backendName,
                            )
                        )
                        .httpHeaders(headers())
                        .execute()
                        .checked()
                check(response.data?.addFavoriteTeam?.success == true)
            }
            preferences.saveFavoriteSports(favorites)
        }

    suspend fun highlights(): List<HighlightData> =
        client
            .query(HighlightCatalogQuery())
            .execute()
            .checked()
            .data
            ?.youtubeVideos
            .orEmpty()
            .filterNotNull()
            .sortedByDescending { it.accountHighlight.publishedAt }
            .mapNotNull { it.accountHighlight.toHighlight() }

    suspend fun setBookmark(id: String, bookmarked: Boolean) {
        val success =
            if (bookmarked) {
                client
                    .mutation(AddBookmarkedHighlightMutation(id))
                    .httpHeaders(headers())
                    .execute()
                    .checked()
                    .data
                    ?.addBookmarkedHighlight
                    ?.success
            } else {
                client
                    .mutation(RemoveBookmarkedHighlightMutation(id))
                    .httpHeaders(headers())
                    .execute()
                    .checked()
                    .data
                    ?.removeBookmarkedHighlight
                    ?.success
            }
        check(success == true)
    }

    private suspend fun headers() =
        listOf(
            HttpHeader(
                "Authorization",
                "Bearer ${checkNotNull(auth.accessToken()) { "Sign in to manage your account" }}",
            )
        )
}

private fun <T : Operation.Data> ApolloResponse<T>.checked():
    ApolloResponse<T> {
    exception?.let { throw it }
    check(errors.isNullOrEmpty()) { "Account request failed" }
    check(data != null) { "Missing account response" }
    return this
}

private val ProfileFavorite.backendGender: String
    get() = if (gender == GenderDivision.FEMALE) "Womens" else "Mens"

private val Sport.backendName: String
    get() =
        when (this) {
            Sport.ROWING_HEAVYWEIGHT -> "Rowing - Heavyweight"
            Sport.ROWING_LIGHTWEIGHT -> "Rowing - Lightweight"
            Sport.SWIM_DIVE -> "Swimming & Diving"
            Sport.TRACK_FIELD -> "Track & Field"
            else -> displayName
        }

private fun sportFromBackend(name: String): Sport? =
    Sport.entries.find {
        it.backendName.equals(name.trim(), ignoreCase = true) ||
            it.displayName.equals(name.trim(), ignoreCase = true)
    }

private fun AccountHighlight.toHighlight(): HighlightData.Video? {
    val sport = sportFromBackend(sportsType.orEmpty())
    return HighlightData.Video(
        VideoHighlightData(
            title = title,
            thumbnailImageUrl = thumbnail,
            videoUrl = url,
            date =
                runCatching {
                        OffsetDateTime.parse(publishedAt).format(DateTimeFormatter.ofPattern("M/d"))
                    }
                    .getOrDefault(publishedAt.substringBefore('T')),
            sport = sport,
            gender =
                when {
                    title.contains("women", ignoreCase = true) -> GenderDivision.FEMALE
                    title.contains("men", ignoreCase = true) -> GenderDivision.MALE
                    else -> sport?.gender ?: GenderDivision.ALL
                },
            duration = duration,
            id = id ?: return null,
        )
    )
}
