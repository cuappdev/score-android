package com.cornellappdev.score.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornellappdev.score.R
import com.cornellappdev.score.auth.AuthRepository
import com.cornellappdev.score.auth.AuthState
import com.cornellappdev.score.auth.GoogleSignInClient
import com.cornellappdev.score.auth.SignInState
import com.cornellappdev.score.model.AccountRepository
import com.cornellappdev.score.model.HighlightData
import com.cornellappdev.score.model.OnboardingPreferencesRepository
import com.cornellappdev.score.model.ProfileData
import com.cornellappdev.score.model.ProfileFavorite
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class AccountViewModel
@Inject
constructor(
    private val accounts: AccountRepository,
    private val auth: AuthRepository,
    private val google: GoogleSignInClient,
    private val preferences: OnboardingPreferencesRepository,
) : ViewModel() {
    private val mutableProfile = MutableStateFlow(ProfileData("Guest", emptyList(), emptyList()))
    val profile = mutableProfile.asStateFlow()
    private val mutableHighlights = MutableStateFlow<List<HighlightData>>(emptyList())
    val highlights = mutableHighlights.asStateFlow()
    private val mutableStatus = MutableStateFlow(SignInState())
    val status = mutableStatus.asStateFlow()
    val authState = auth.state

    init {
        viewModelScope.launch {
            auth.state.collectLatest { state ->
                mutableProfile.value = ProfileData("Guest", emptyList(), emptyList())
                if (state is AuthState.SignedIn) {
                    mutableProfile.value = mutableProfile.value.copy(name = state.user.name)
                }
                if (state == AuthState.SignedOut) preferences.clearAccountPreferences()
                if (state != AuthState.Loading) {
                    perform {
                        if (state is AuthState.SignedIn) refresh()
                        mutableHighlights.value = accounts.highlights()
                    }
                }
            }
        }
    }

    fun refreshAccount() = runAction {
        if (auth.state.value is AuthState.SignedIn) refresh()
        mutableHighlights.value = accounts.highlights()
    }

    fun saveFavorites(favorites: List<ProfileFavorite>) = runAction {
        try {
            accounts.saveFavorites(favorites.toSet())
        } finally {
            // Also reconcile after a partial save; the next attempt can safely retry.
            refresh()
        }
    }

    fun toggleBookmark(highlight: HighlightData) = runAction {
        val id = (highlight as? HighlightData.Video)?.data?.id ?: return@runAction
        accounts.setBookmark(id, id !in mutableProfile.value.bookmarkedHighlightIds)
        refresh()
    }

    fun signOut() = runAction {
        auth.signOut()
        preferences.clearAccountPreferences()
        google.signOut()
    }

    fun deleteAccount() = runAction {
        auth.deleteAccount()
        preferences.clearAccountPreferences()
        google.signOut()
    }

    fun dismissError() {
        mutableStatus.value = mutableStatus.value.copy(error = null)
    }

    private suspend fun refresh() {
        mutableProfile.value = accounts.profile()
    }

    private fun runAction(action: suspend () -> Unit) {
        if (mutableStatus.value.busy) return
        mutableStatus.value = SignInState(busy = true)
        viewModelScope.launch { perform(action) }
    }

    private suspend fun perform(action: suspend () -> Unit) {
        mutableStatus.value = SignInState(busy = true)
        try {
            action()
            mutableStatus.value = SignInState()
        } catch (error: CancellationException) {
            mutableStatus.value = SignInState()
            throw error
        } catch (error: Exception) {
            Log.e("AccountViewModel", "Account request failed", error)
            mutableStatus.value = SignInState(error = R.string.account_request_failed)
        }
    }
}
