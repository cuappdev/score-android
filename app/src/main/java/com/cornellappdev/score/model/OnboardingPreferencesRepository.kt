package com.cornellappdev.score.model

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class OnboardingPreferencesRepository @Inject constructor(
    @ApplicationContext context: Context
) {
    private val preferences: SharedPreferences = context.getSharedPreferences(
        PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )

    fun isOnboardingCompleted(): Boolean =
        preferences.getBoolean(KEY_ONBOARDING_COMPLETED, false)

    fun markOnboardingCompleted() {
        preferences.edit {
            putBoolean(KEY_ONBOARDING_COMPLETED, true)
        }
    }

    fun getFavoriteSports(): Set<ProfileFavorite> =
        preferences.getStringSet(KEY_ONBOARDING_FAVORITE_SPORTS, emptySet())
            .orEmpty()
            .mapNotNull { ProfileFavorite.fromStorageKey(it) }
            .toSet()

    fun saveFavoriteSports(favorites: Set<ProfileFavorite>) {
        preferences.edit {
            putStringSet(KEY_ONBOARDING_FAVORITE_SPORTS, favorites.map { it.storageKey }.toSet())
        }
    }

    fun getProfilePicture(): String? =
        preferences.getString(KEY_ONBOARDING_PROFILE_PICTURE, null)

    fun saveProfilePicture(pictureName: String) {
        preferences.edit {
            putString(KEY_ONBOARDING_PROFILE_PICTURE, pictureName)
        }
    }

    private companion object {
        const val PREFERENCES_NAME = "score_app_preferences"
        const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
        const val KEY_ONBOARDING_PROFILE_PICTURE = "onboarding_profile_picture"
        const val KEY_ONBOARDING_FAVORITE_SPORTS = "onboarding_favorite_sports"
    }
}
