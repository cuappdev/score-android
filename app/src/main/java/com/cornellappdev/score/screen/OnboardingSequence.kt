package com.cornellappdev.score.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.cornellappdev.score.model.OnboardingPreferencesRepository

private enum class OnboardingStep {
    WELCOME,
    PROFILE_SELECT,
    SPORT_SELECT
}

@Composable
fun OnboardingSequence(
    onboardingPreferencesRepository: OnboardingPreferencesRepository,
    onOnboardingComplete: () -> Unit
) {
    var onboardingStep by rememberSaveable {
        mutableStateOf(OnboardingStep.WELCOME)
    }

    val completeOnboarding = remember {
        onOnboardingComplete
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
    }
}
