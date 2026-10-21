package com.cornellappdev.score.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.theme.CrimsonPrimary
import com.cornellappdev.score.theme.Style.heading3
import com.cornellappdev.score.theme.Style.labelsNormal
import com.cornellappdev.score.theme.White
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun OnboardingButtons(
    onSignUp: () -> Unit,
    onLogIn: () -> Unit,
    onLater: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showSignUp by remember { mutableStateOf(false) }
    var showLogIn by remember { mutableStateOf(false) }
    var showLater by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        showSignUp = true
        delay(160.milliseconds)
        showLogIn = true
        delay(180.milliseconds)
        showLater = true
    }

    OnboardingButtonsContent(
        onSignUp = onSignUp,
        onLogIn = onLogIn,
        onLater = onLater,
        showSignUp = showSignUp,
        showLogIn = showLogIn,
        showLater = showLater,
        animateEntrance = true,
        modifier = modifier
    )
}

@Composable
private fun OnboardingButtonsContent(
    onSignUp: () -> Unit,
    onLogIn: () -> Unit,
    onLater: () -> Unit,
    showSignUp: Boolean,
    showLogIn: Boolean,
    showLater: Boolean,
    animateEntrance: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OnboardingButtonSlot(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            visible = showSignUp,
            animateEntrance = animateEntrance,
            initialOffset = 26.dp,
            durationMillis = 340
        ) {
            Button(
                onClick = onSignUp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(100),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = CrimsonPrimary
                )
            ) {
                Text(
                    text = "Sign Up",
                    style = heading3,
                    color = CrimsonPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OnboardingButtonSlot(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            visible = showLogIn,
            animateEntrance = animateEntrance,
            initialOffset = 24.dp,
            durationMillis = 340
        ) {
            OutlinedButton(
                onClick = onLogIn,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(100),
                border = BorderStroke(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.9f)
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Log In",
                    style = heading3,
                    color = White
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OnboardingButtonSlot(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            visible = showLater,
            animateEntrance = animateEntrance,
            initialOffset = 18.dp,
            durationMillis = 320
        ) {
            TextButton(
                onClick = onLater,
                colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
            ) {
                Text(
                    text = "Sign Up Later",
                    style = labelsNormal
                )
            }
        }
    }
}

@Composable
private fun OnboardingButtonSlot(
    visible: Boolean,
    animateEntrance: Boolean,
    initialOffset: androidx.compose.ui.unit.Dp,
    durationMillis: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val initialOffsetPx = with(androidx.compose.ui.platform.LocalDensity.current) {
        initialOffset.roundToPx()
    }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (animateEntrance) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(durationMillis)) + slideInVertically(
                    initialOffsetY = { initialOffsetPx },
                    animationSpec = tween(durationMillis)
                )
            ) {
                content()
            }
        } else if (visible) {
            content()
        }
    }
}

@Preview(showBackground = true, widthDp = 393)
@Composable
private fun OnboardingButtonsPreview() = ScorePreview(
    padding = 24.dp,
    backgroundColor = CrimsonPrimary
) {
    OnboardingButtonsContent(
        onSignUp = {},
        onLogIn = {},
        onLater = {},
        showSignUp = true,
        showLogIn = true,
        showLater = true,
        animateEntrance = false
    )
}