package com.cornellappdev.score

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.cornellappdev.score.nav.root.RootNavigation
import com.cornellappdev.score.model.OnboardingPreferencesRepository
import com.cornellappdev.score.screen.OnboardingScreen
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private lateinit var firebaseAnalytics: FirebaseAnalytics

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var onboardingPreferencesRepository: OnboardingPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val onboardingCompleted = onboardingPreferencesRepository.isOnboardingCompleted()

        setContent {
            var showOnboarding by rememberSaveable {
                mutableStateOf(!onboardingCompleted)
            }

            val completeOnboarding = remember {
                {
                    onboardingPreferencesRepository.markOnboardingCompleted()
                    showOnboarding = false
                }
            }

            if (showOnboarding) {
                OnboardingScreen(
                    onSignUp = completeOnboarding,
                    onLogIn = completeOnboarding,
                    onLater = completeOnboarding
                )
            } else {
                RootNavigation()
            }
        }

        firebaseAnalytics = Firebase.analytics
    }
}
