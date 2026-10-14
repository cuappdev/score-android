package com.cornellappdev.score

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.cornellappdev.score.nav.root.RootNavigation
import com.cornellappdev.score.screen.OnboardingAnimationPreviewScreen
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import dagger.hilt.android.AndroidEntryPoint

private lateinit var firebaseAnalytics: FirebaseAnalytics

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            //RootNavigation()
            OnboardingAnimationPreviewScreen(
                onSignUp = {},
                onLogIn = {},
                onLater = {}
            )
        }
        firebaseAnalytics = Firebase.analytics
    }
}
