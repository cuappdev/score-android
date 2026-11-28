package com.cornellappdev.score.auth

import android.app.Activity
import android.util.Log
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
    private companion object {
        const val TAG = "AuthViewModel"
    }

    val authState = repository.state
    private val mutableSignInState = MutableStateFlow(SignInState(busy = true))
    val signInState = mutableSignInState.asStateFlow()

    init {
        viewModelScope.launch {
            Log.d(TAG, "Restoring saved Score session")
            repository.restoreSession()
            Log.d(
                TAG,
                "Session restoration complete; signedIn=${repository.state.value is AuthState.SignedIn}"
            )
            mutableSignInState.value = SignInState()
        }
    }

    fun signIn(activity: Activity) {
        if (mutableSignInState.value.busy) return
        viewModelScope.launch {
            mutableSignInState.value = SignInState(busy = true)
            try {
                Log.i(TAG, "Google sign-in started")
                val identity = google.signIn(activity)
                Log.i(TAG, "Google/Firebase sign-in completed; starting backend authentication")
                repository.signIn(identity)
                Log.i(TAG, "Backend authentication completed; Score session saved")
                mutableSignInState.value = SignInState()
            } catch (error: GetCredentialCancellationException) {
                Log.w(TAG, "Google credential flow was cancelled: ${error.message}", error)
                mutableSignInState.value = SignInState()
            } catch (_: NoCredentialException) {
                Log.w(TAG, "No Google account is available for sign-in")
                mutableSignInState.value = SignInState(error = R.string.auth_no_google_account)
            } catch (_: CornellAccountRequiredException) {
                Log.w(TAG, "Google sign-in rejected: account is not a Cornell account")
                mutableSignInState.value = SignInState(error = R.string.auth_cornell_required)
            } catch (error: CancellationException) {
                Log.i(TAG, "Sign-in coroutine was cancelled")
                mutableSignInState.value = SignInState()
                throw error
            } catch (error: Exception) {
                Log.e(TAG, "Google sign-in failed", error)
                mutableSignInState.value = SignInState(error = R.string.auth_sign_in_failed)
                try {
                    google.signOut()
                } catch (cleanupError: Exception) {
                    Log.w(TAG, "Could not clear Google credential state", cleanupError)
                }
            }
        }
    }

    fun dismissError() {
        mutableSignInState.value = mutableSignInState.value.copy(error = null)
    }
}
