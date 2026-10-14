package com.cornellappdev.score.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.components.OnboardingButtons
import com.cornellappdev.score.theme.CrimsonPrimary
import com.yourpackage.ui.onboarding.Speaker
import com.yourpackage.ui.onboarding.SportsIconAnimation
import kotlinx.coroutines.delay

@Composable
fun OnboardingAnimationPreviewScreen(
    onSignUp: () -> Unit,
    onLogIn: () -> Unit,
    onLater: () -> Unit,
    modifier: Modifier = Modifier
) {
    var animationKey by remember { mutableIntStateOf(0) }

    Box(modifier = modifier.fillMaxSize()) {
        OnboardingAnimation(
            animationKey = animationKey
        )

        Button(
            onClick = {
                animationKey++
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 56.dp, end = 16.dp)
        ) {
            Text("Replay")
        }
    }
}

@Composable
private fun OnboardingAnimation(
    animationKey: Int
) {
    var showIcons by remember(animationKey) {
        mutableStateOf(false)
    }

    var showRings by remember(animationKey) {
        mutableStateOf(true)
    }

    var showButtons by remember(animationKey) {
        mutableStateOf(false)
    }

    var moveSpeakerUp by remember(animationKey) {
        mutableStateOf(false)
    }

    LaunchedEffect(animationKey) {
        showIcons = false
        showRings = true
        showButtons = false
        moveSpeakerUp = false

        delay(350)
        showIcons = true

        delay(2550)
        showIcons = false
        showRings = false

        delay(1300)
        moveSpeakerUp = true

        delay(700)
        showButtons = true
    }

    // Your existing Box containing:
    //
    // SportsIconAnimation(...)
    // Megaphone(...)
    // OnboardingButtons(...)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = CrimsonPrimary
            )
    ) {

        // Sports icons spread outward and fade as the megaphone grows.
        SportsIconAnimation(
            modifier = Modifier.fillMaxSize(),
            visible = showIcons
        )

        // Speaker + expanding circles
        Speaker(
            moveUp = moveSpeakerUp,
            showRings = showRings,
            modifier = Modifier.align(Alignment.Center)
        )

        if (showButtons) {
            OnboardingButtons(
                onSignUp = { },
                onLogIn = { },
                onLater = { },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(
                        start = 24.dp,
                        end = 24.dp,
                        bottom = 48.dp
                    )
            )
        }
    }
}