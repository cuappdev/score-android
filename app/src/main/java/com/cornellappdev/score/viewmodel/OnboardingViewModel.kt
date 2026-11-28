package com.cornellappdev.score.viewmodel

import androidx.lifecycle.ViewModel
import com.cornellappdev.score.model.OnboardingPreferencesRepository
import com.cornellappdev.score.model.ProfileFavorite
import com.cornellappdev.score.screen.ProfilePicture
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingSequenceViewModel
@Inject
constructor(private val onboardingPreferencesRepository: OnboardingPreferencesRepository) :
    ViewModel() {
    fun getFavoriteSports(): List<ProfileFavorite> =
        onboardingPreferencesRepository.getFavoriteSports().toList()

    fun getProfilePicture(): ProfilePicture =
        ProfilePicture.entries.find {
            it.name == onboardingPreferencesRepository.getProfilePicture()
        } ?: ProfilePicture.BLANK

    fun saveProfilePicture(pictureName: String) {
        onboardingPreferencesRepository.saveProfilePicture(pictureName)
    }

    fun saveFavoriteSports(favorites: Set<ProfileFavorite>) {
        onboardingPreferencesRepository.saveFavoriteSports(favorites)
    }
}
