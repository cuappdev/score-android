package com.cornellappdev.score

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.cornellappdev.score.auth.AuthViewModel
import com.cornellappdev.score.model.OnboardingPreferencesRepository
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject lateinit var onboardingPreferences: OnboardingPreferencesRepository
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Firebase.analytics
        setContent {
            ScoreApp(
                authViewModel = authViewModel,
                onboardingPreferences = onboardingPreferences,
                onSignIn = { authViewModel.signIn(this) },
            )
        }
    }
}
