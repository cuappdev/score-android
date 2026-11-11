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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.cornellappdev.score.model.OnboardingPreferencesRepository
import com.cornellappdev.score.nav.root.RootNavigation
import com.cornellappdev.score.screen.OnboardingScreen
import com.cornellappdev.score.screen.OnboardingProfileSelectScreen
import com.cornellappdev.score.screen.OnboardingScreen
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val APP_PREFERENCES = "score_app_preferences"
private const val KEY_ONBOARDING_PROFILE_PICTURE = "onboarding_profile_picture"

private lateinit var firebaseAnalytics: FirebaseAnalytics

private enum class OnboardingStep {
    WELCOME,
    PROFILE_SELECT,
    COMPLETE
}

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var onboardingPreferencesRepository: OnboardingPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val onboardingCompleted = onboardingPreferencesRepository.isOnboardingCompleted()
        val preferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)

        setContent {
            var onboardingStep by rememberSaveable {
                mutableStateOf(
                    if (onboardingCompleted) OnboardingStep.COMPLETE else OnboardingStep.WELCOME
                )
            }

            val completeOnboarding = remember {
                {
                    onboardingPreferencesRepository.markOnboardingCompleted()
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
