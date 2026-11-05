package com.cornellappdev.score.auth

import android.app.Activity
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornellappdev.score.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SignInState(val busy: Boolean = false, val error: Int? = null)

@HiltViewModel
class AuthViewModel
@Inject
constructor(private val repository: AuthRepository, private val google: GoogleSignInClient) :
    ViewModel() {
    val authState = repository.state
    private val mutableSignInState = MutableStateFlow(SignInState(busy = true))
    val signInState = mutableSignInState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.restoreSession()
            mutableSignInState.value = SignInState()
        }
    }

    fun signIn(activity: Activity) {
        if (mutableSignInState.value.busy) return
        viewModelScope.launch {
            mutableSignInState.value = SignInState(busy = true)
            try {
                val identity = google.signIn(activity)
                repository.signIn(identity)
                mutableSignInState.value = SignInState()
            } catch (_: GetCredentialCancellationException) {
                mutableSignInState.value = SignInState()
            } catch (_: NoCredentialException) {
                mutableSignInState.value = SignInState(error = R.string.auth_no_google_account)
            } catch (_: CornellAccountRequiredException) {
                mutableSignInState.value = SignInState(error = R.string.auth_cornell_required)
            } catch (error: CancellationException) {
                mutableSignInState.value = SignInState()
                throw error
            } catch (_: Exception) {
                google.signOut()
                mutableSignInState.value = SignInState(error = R.string.auth_sign_in_failed)
            }
        }
    }

    fun dismissError() {
        mutableSignInState.value = mutableSignInState.value.copy(error = null)
    }
}
