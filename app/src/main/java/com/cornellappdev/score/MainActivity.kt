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
import com.cornellappdev.score.model.OnboardingPreferencesRepository
import com.cornellappdev.score.nav.root.RootNavigation
import com.cornellappdev.score.screen.OnboardingFavoriteSportsScreen
import com.cornellappdev.score.screen.OnboardingProfileSelectScreen
import com.cornellappdev.score.screen.OnboardingScreen
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private lateinit var firebaseAnalytics: FirebaseAnalytics

private enum class OnboardingStep {
    WELCOME,
    PROFILE_SELECT,
    SPORT_SELECT,
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
                        onboardingPreferencesRepository.saveProfilePicture(selectedPicture.name)
                        onboardingStep = OnboardingStep.SPORT_SELECT
                    }
                )

                OnboardingStep.SPORT_SELECT -> {
                    var selectedFavorites by remember {
                        mutableStateOf(onboardingPreferencesRepository.getFavoriteSports().toList())
                    }
                    OnboardingFavoriteSportsScreen(
                        favorites = selectedFavorites,
                        onToggleFavorite = { favorite ->
                            selectedFavorites = if (favorite in selectedFavorites) {
                                selectedFavorites - favorite
                            } else {
                                selectedFavorites + favorite
                            }
                        },
                        onFinish = {
                            onboardingPreferencesRepository.saveFavoriteSports(selectedFavorites.toSet())
                            completeOnboarding()
                        }
                    )
                }

                OnboardingStep.COMPLETE -> RootNavigation()
            }
        }

        firebaseAnalytics = Firebase.analytics
    }
}
