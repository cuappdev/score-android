package com.cornellappdev.score.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Preview host for replaying the same onboarding animation used by the real screen.
 */
@Composable
fun OnboardingAnimationPreviewScreen(
    onSignUp: () -> Unit,
    onLogIn: () -> Unit,
    onLater: () -> Unit,
    modifier: Modifier = Modifier
) {
    var animationKey by remember { mutableIntStateOf(0) }

    Box(modifier = modifier.fillMaxSize()) {
        OnboardingScreen(
            onSignUp = onSignUp,
            onLogIn = onLogIn,
            onLater = onLater,
            animationKey = animationKey
        )

        Button(
            onClick = { animationKey++ },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 56.dp, end = 16.dp)
        ) {
            Text("Replay")
        }
    }
}
