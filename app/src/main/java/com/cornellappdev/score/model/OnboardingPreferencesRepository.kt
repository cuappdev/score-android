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

    private companion object {
        const val PREFERENCES_NAME = "score_app_preferences"
        const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
    }
}
