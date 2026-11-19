package com.cornellappdev.score.viewmodel

import androidx.lifecycle.viewModelScope
import com.cornellappdev.score.R
import com.cornellappdev.score.auth.SignInState
import com.cornellappdev.score.model.AccountRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import com.cornellappdev.score.model.OnboardingPreferencesRepository
import com.cornellappdev.score.model.ProfileFavorite
import com.cornellappdev.score.screen.ProfilePicture
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingSequenceViewModel
@Inject
constructor(
    private val onboardingPreferencesRepository: OnboardingPreferencesRepository,
    private val accounts: AccountRepository,
) :
    ViewModel() {
    private val mutableStatus = MutableStateFlow(SignInState())
    val status = mutableStatus.asStateFlow()

    fun dismissError() { mutableStatus.value = mutableStatus.value.copy(error = null) }

    fun getFavoriteSports(): List<ProfileFavorite> =
        onboardingPreferencesRepository.getFavoriteSports().toList()

    fun getProfilePicture(): ProfilePicture =
        ProfilePicture.entries.find {
            it.name == onboardingPreferencesRepository.getProfilePicture()
        } ?: ProfilePicture.BLANK

    fun saveProfilePicture(pictureName: String) {
        onboardingPreferencesRepository.saveProfilePicture(pictureName)
    }

    fun saveFavoriteSports(favorites: Set<ProfileFavorite>, onSaved: () -> Unit) {
        if (mutableStatus.value.busy) return
        mutableStatus.value = SignInState(busy = true)
        viewModelScope.launch {
            try {
                accounts.saveFavorites(favorites)
                mutableStatus.value = SignInState()
                onSaved()
            } catch (error: CancellationException) {
                mutableStatus.value = SignInState()
                throw error
            } catch (_: Exception) {
                mutableStatus.value = SignInState(error = R.string.account_request_failed)
            }
        }
    }
}
