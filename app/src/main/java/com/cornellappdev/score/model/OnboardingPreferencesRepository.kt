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

    fun getFavoriteSportNames(): Set<String> = preferences
        .getStringSet(KEY_FAVORITE_SPORTS, emptySet())
        .orEmpty()
        .mapNotNull { it.substringBefore(':').takeIf(String::isNotBlank) }
        .toSet()

    fun saveFavoriteSportKeys(keys: Set<String>) {
        preferences.edit {
            putStringSet(KEY_FAVORITE_SPORTS, keys.toSet())
        }
    }

    private companion object {
        const val PREFERENCES_NAME = "score_app_preferences"
        const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
        const val KEY_FAVORITE_SPORTS = "favorite_sports"
    }
}
