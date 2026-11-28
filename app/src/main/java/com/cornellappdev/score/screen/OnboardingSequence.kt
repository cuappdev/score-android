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
    PROFILE_SELECT,
    SPORT_SELECT
}

@Composable
fun OnboardingSequence(
    signedIn: Boolean,
    onSignIn: () -> Unit,
    onSkipSignIn: () -> Unit,
    onOnboardingComplete: () -> Unit,
) {
    if (!signedIn) {
        OnboardingScreen(
            onSignUp = onSignIn,
            onLogIn = onSignIn,
            onLater = onSkipSignIn,
        )
        return
    }

    ProfileSetupSequence(onOnboardingComplete = onOnboardingComplete)
}

@Composable
private fun ProfileSetupSequence(
    onOnboardingComplete: () -> Unit,
    viewModel: OnboardingSequenceViewModel = hiltViewModel(),
) {
    var onboardingStep by rememberSaveable {
        mutableStateOf(OnboardingStep.PROFILE_SELECT)
    }

    when (onboardingStep) {
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
                    onOnboardingComplete()
                }
            )
        }
    }
}
