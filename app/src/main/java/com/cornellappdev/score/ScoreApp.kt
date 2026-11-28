package com.cornellappdev.score

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cornellappdev.score.auth.AuthState
import com.cornellappdev.score.auth.AuthStatusOverlay
import com.cornellappdev.score.auth.AuthViewModel
import com.cornellappdev.score.model.OnboardingPreferencesRepository
import com.cornellappdev.score.nav.root.RootNavigation
import com.cornellappdev.score.screen.OnboardingSequence
import com.cornellappdev.score.theme.CrimsonPrimary
import com.cornellappdev.score.util.toWelcomeName

@Composable
fun ScoreApp(
    authViewModel: AuthViewModel,
    onboardingPreferences: OnboardingPreferencesRepository,
    onSignIn: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val authState by authViewModel.authState.collectAsStateWithLifecycle()
    val signInState by authViewModel.signInState.collectAsStateWithLifecycle()
    var skippedWelcome by rememberSaveable {
        mutableStateOf(onboardingPreferences.isOnboardingCompleted())
    }
    var profileSetupComplete by rememberSaveable {
        mutableStateOf(onboardingPreferences.isProfileSetupCompleted())
    }
    val signedInState = authState as? AuthState.SignedIn
    val signedIn = signedInState != null
    val showMainApp = if (signedIn) profileSetupComplete else skippedWelcome

    Box(modifier.fillMaxSize().background(Color.White)) {
        when {
            authState == AuthState.Loading ->
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = CrimsonPrimary)
                }
            showMainApp -> RootNavigation(
                welcomeName = signedInState?.user?.toWelcomeName(),
                isLoggedIn = signedIn,
            )
            else ->
                OnboardingSequence(
                    signedIn = signedIn,
                    onSignIn = onSignIn,
                    onSkipSignIn = {
                        onboardingPreferences.markOnboardingCompleted()
                        skippedWelcome = true
                    },
                    onOnboardingComplete = {
                        onboardingPreferences.markProfileSetupCompleted()
                        profileSetupComplete = true
                        skippedWelcome = true
                    },
                )
        }
        AuthStatusOverlay(signInState, authViewModel::dismissError)
    }
}
