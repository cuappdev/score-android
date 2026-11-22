package com.cornellappdev.score.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.cornellappdev.score.viewmodel.OnboardingSequenceViewModel

private enum class OnboardingStep {
    WELCOME,
    PROFILE_SELECT,
    SPORT_SELECT
}

@Composable
fun OnboardingSequence(
    onOnboardingComplete: () -> Unit,
    viewModel: OnboardingSequenceViewModel = hiltViewModel()
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
                viewModel.saveProfilePicture(selectedPicture.name)
                onboardingStep = OnboardingStep.SPORT_SELECT
            }
        )

        OnboardingStep.SPORT_SELECT -> {
            var selectedFavorites by remember {
                mutableStateOf(viewModel.getFavoriteSports())
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
                    viewModel.saveFavoriteSports(selectedFavorites.toSet())
                    completeOnboarding()
                }
            )
        }
    }
}
