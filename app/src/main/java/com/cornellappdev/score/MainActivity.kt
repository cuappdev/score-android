package com.cornellappdev.score

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.content.edit
import com.cornellappdev.score.nav.root.RootNavigation
import com.cornellappdev.score.screen.OnboardingProfileSelectScreen
import com.cornellappdev.score.screen.OnboardingScreen
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import dagger.hilt.android.AndroidEntryPoint

private const val APP_PREFERENCES = "score_app_preferences"
private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
private const val KEY_ONBOARDING_PROFILE_PICTURE = "onboarding_profile_picture"
private const val KEY_ONBOARDING_FAVORITE_SPORTS = "onboarding_favorite_sports"

private lateinit var firebaseAnalytics: FirebaseAnalytics

private enum class OnboardingStep {
    WELCOME,
    PROFILE_SELECT,
    COMPLETE
}

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val preferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        val onboardingCompleted = preferences.getBoolean(KEY_ONBOARDING_COMPLETED, false)
        val savedFavoriteSportKeys = preferences
            .getStringSet(KEY_ONBOARDING_FAVORITE_SPORTS, emptySet())
            .orEmpty()
            .joinToString("|")

        setContent {
            var onboardingStep by rememberSaveable {
                mutableStateOf(if (onboardingCompleted) OnboardingStep.COMPLETE else OnboardingStep.WELCOME)
            }

            val completeOnboarding = remember {
                {
                    preferences.edit {
                        putBoolean(KEY_ONBOARDING_COMPLETED, true)
                    }
                    onboardingStep = OnboardingStep.COMPLETE
                }
            }

            when (onboardingStep) {
                OnboardingStep.WELCOME -> OnboardingScreen(
                    onSignUp = { onboardingStep = OnboardingStep.PROFILE_SELECT },
                    onLogIn = completeOnboarding,
                    onLater = completeOnboarding
                )

                OnboardingStep.PROFILE_SELECT -> OnboardingProfileSelectScreen(
                    onNext = { selectedPicture ->
                        preferences.edit {
                            putString(KEY_ONBOARDING_PROFILE_PICTURE, selectedPicture.name)
                        }
                        completeOnboarding()
                    }
                )

                OnboardingStep.COMPLETE -> RootNavigation()
            }
        }

        firebaseAnalytics = Firebase.analytics
    }
}
