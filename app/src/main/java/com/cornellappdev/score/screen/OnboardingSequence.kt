package com.cornellappdev.score.screen

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cornellappdev.score.auth.AuthStatusOverlay
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.cornellappdev.score.auth.OnboardingStage
import com.cornellappdev.score.model.ProfileFavorite
import com.cornellappdev.score.viewmodel.OnboardingSequenceViewModel

@Composable
fun OnboardingSequence(
    stage: OnboardingStage,
    displayName: String,
    onProfileComplete: () -> Unit,
    onOnboardingComplete: () -> Unit,
    viewModel: OnboardingSequenceViewModel = hiltViewModel(),
) {
    val status by viewModel.status.collectAsStateWithLifecycle()
    AuthStatusOverlay(status, viewModel::dismissError)
    when (stage) {
        OnboardingStage.PROFILE ->
            OnboardingProfileSelectScreen(
                displayName = displayName,
                initiallySelectedPicture = viewModel.getProfilePicture(),
                onNext = { selectedPicture ->
                    viewModel.saveProfilePicture(selectedPicture.name)
                    onProfileComplete()
                },
            )

        OnboardingStage.SPORTS -> {
            var selectedKeys by rememberSaveable {
                mutableStateOf(viewModel.getFavoriteSports().joinToString("|") { it.storageKey })
            }
            val selectedFavorites =
                selectedKeys.split('|').mapNotNull(ProfileFavorite::fromStorageKey)
            OnboardingFavoriteSportsScreen(
                favorites = selectedFavorites,
                onToggleFavorite = { favorite ->
                    val updatedFavorites =
                        if (favorite in selectedFavorites) {
                            selectedFavorites - favorite
                        } else {
                            selectedFavorites + favorite
                        }
                    selectedKeys = updatedFavorites.joinToString("|") { it.storageKey }
                },
                onFinish = {
                    viewModel.saveFavoriteSports(selectedFavorites.toSet(), onOnboardingComplete)
                },
            )
        }
        OnboardingStage.COMPLETE -> Unit
    }
}
