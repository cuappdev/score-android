package com.yourpackage.ui.onboarding

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import com.cornellappdev.score.R
import com.cornellappdev.score.components.OnboardingButtons
import com.cornellappdev.score.theme.CrimsonPrimary
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

private enum class OnboardingAnimationPhase {
    Initial,
    Icons,
    IconsFading,
    SpeakerTransition,
    Complete
}

@Composable
fun OnboardingScreen(
    onSignUp: () -> Unit,
    onLogIn: () -> Unit,
    onLater: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showIcons by remember { mutableStateOf(false) }
    var showRings by remember { mutableStateOf(true) }
    var showButtons by remember { mutableStateOf(false) }
    var moveSpeakerUp by remember { mutableStateOf(false) }

    /*
     * Timeline based on the Figma animation:
     *
     * 0ms       -> red background
     * ~300ms    -> megaphone/rings start
     * ~400ms    -> sports icons start appearing
     * ~2500ms   -> icons finish
     * ~3000ms   -> icons fade out
     * ~3200ms   -> megaphone moves upward
     * ~3400ms   -> buttons appear
     */
    LaunchedEffect(Unit) {
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

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFB52A12),
                        CrimsonPrimary,
                        Color(0xFF941B0A)
                    )
                )
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
                onSignUp = onSignUp,
                onLogIn = onLogIn,
                onLater = onLater,
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

@Composable
fun Speaker(
    moveUp: Boolean,
    modifier: Modifier = Modifier,
    showRings: Boolean = true
) {
    val transition = updateTransition(
        targetState = moveUp,
        label = "speakerTransition"
    )

    val verticalOffset by transition.animateFloat(
        transitionSpec = {
            tween(
                durationMillis = 700,
                easing = FastOutSlowInEasing
            )
        },
        label = "speakerY"
    ) { isUp ->
        if (isUp) -120f else 0f
    }

    val scale by transition.animateFloat(
        transitionSpec = {
            tween(
                durationMillis = 700,
                easing = FastOutSlowInEasing
            )
        },
        label = "speakerScale"
    ) { isUp ->
        if (isUp) 1.45f else 1f // this is centered
    }

    val iconSize by transition.animateDp(
        transitionSpec = {
            tween(
                durationMillis = 700,
                easing = FastOutSlowInEasing
            )
        },
        label = "speakerIconSize"
    ) { isUp ->
        if (isUp) 136.dp else 132.dp
    }

    Box(
        modifier = modifier
            .offset {
                IntOffset(
                    x = 0,
                    y = verticalOffset.roundToInt()
                )
            }
            .scale(scale)
            .size(190.dp),
        contentAlignment = Alignment.Center
    ) {

        // Expanding circles
        val infiniteTransition = rememberInfiniteTransition(
            label = "rings"
        )

        val scale1 by infiniteTransition.animateFloat(
            initialValue = 0.75f,
            targetValue = 1.25f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1800,
                    easing = LinearOutSlowInEasing
                ),
                repeatMode = RepeatMode.Restart
            ),
            label = "ring1"
        )

        val scale2 by infiniteTransition.animateFloat(
            initialValue = 0.65f,
            targetValue = 1.15f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1800,
                    delayMillis = 450,
                    easing = LinearOutSlowInEasing
                ),
                repeatMode = RepeatMode.Restart
            ),
            label = "ring2"
        )

        val scale3 by infiniteTransition.animateFloat(
            initialValue = 0.55f,
            targetValue = 1.05f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1800,
                    delayMillis = 900,
                    easing = LinearOutSlowInEasing
                ),
                repeatMode = RepeatMode.Restart
            ),
            label = "ring3"
        )

        AnimatedVisibility(
            visible = showRings,
            enter = fadeIn(tween(250)),
            exit = fadeOut(tween(700))
        ) {
            Box(contentAlignment = Alignment.Center) {
                Ring(
                    scale = scale1,
                    modifier = Modifier.size(190.dp)
                )

                Ring(
                    scale = scale2,
                    modifier = Modifier.size(190.dp)
                )

                Ring(
                    scale = scale3,
                    modifier = Modifier.size(190.dp)
                )
            }
        }

        Image(
            painter = painterResource(R.drawable.ic_speaker),
            contentDescription = null,
            modifier = Modifier.size(iconSize),
            colorFilter = ColorFilter.tint(Color.White)
        )
    }
}

@Composable
private fun Ring(
    scale: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .scale(scale)
            .clip(shape = CircleShape)
            .background(color = Color.White.copy(alpha = 0.055f))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.15f),
                shape = CircleShape
            )
    )
}

private data class SportsIcon(
    @DrawableRes val drawable: Int,
    val x: Float,
    val y: Float,
    val delay: Int,
    val rotation: Float = 0f
)

private val sportsIcons = listOf(
    // Top row
    SportsIcon(
        drawable = R.drawable.ic_rowing_lightweight,
        x = -0.22f,
        y = -0.43f,
        delay = 0
    ),
    SportsIcon(
        drawable = R.drawable.ic_field_hockey,
        x = 0.10f,
        y = -0.47f,
        delay = 100
    ),
    SportsIcon(
        drawable = R.drawable.ic_wrestling,
        x = 0.38f,
        y = -0.40f,
        delay = 200
    ),

    // Upper-middle
    SportsIcon(
        drawable = R.drawable.ic_basketball,
        x = -0.03f,
        y = -0.29f,
        delay = 250
    ),
    SportsIcon(
        drawable = R.drawable.ic_tennis,
        x = 0.27f,
        y = -0.27f,
        delay = 350
    ),
    SportsIcon(
        drawable = R.drawable.ic_volleyball,
        x = -0.40f,
        y = -0.22f,
        delay = 400
    ),
    SportsIcon(
        drawable = R.drawable.ic_soccer,
        x = 0.47f,
        y = -0.17f,
        delay = 500
    ),

    // Middle
    SportsIcon(
        drawable = R.drawable.ic_track_field,
        x = -0.48f,
        y = 0.02f,
        delay = 550
    ),
    SportsIcon(
        drawable = R.drawable.ic_lacrosse,
        x = 0.48f,
        y = 0.05f,
        delay = 650
    ),

    // Lower-middle
    SportsIcon(
        drawable = R.drawable.ic_swim_dive,
        x = -0.40f,
        y = 0.22f,
        delay = 700
    ),
    SportsIcon(
        drawable = R.drawable.ic_field_hockey,
        x = 0.23f,
        y = 0.22f,
        delay = 800
    ),
    SportsIcon(
        drawable = R.drawable.ic_baseball,
        x = 0.43f,
        y = 0.27f,
        delay = 900
    ),

    // Bottom
    SportsIcon(
        drawable = R.drawable.ic_tennis,
        x = -0.34f,
        y = 0.40f,
        delay = 1000
    ),
    SportsIcon(
        drawable = R.drawable.ic_golf,
        x = 0.02f,
        y = 0.42f,
        delay = 1100
    ),
    SportsIcon(
        drawable = R.drawable.ic_football,
        x = 0.35f,
        y = 0.43f,
        delay = 1200
    )
)

@Composable
fun SportsIconAnimation(
    modifier: Modifier = Modifier,
    visible: Boolean = true
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val animationSize = with(LocalDensity.current) { maxWidth.toPx() }

        sportsIcons.forEach { icon ->
            AnimatedSportsIcon(
                icon = icon,
                animationSize = animationSize,
                visible = visible
            )
        }
    }
}

@Composable
private fun AnimatedSportsIcon(
    icon: SportsIcon,
    animationSize: Float,
    previewProgress: Float? = null,
    visible: Boolean = true
) {
    var started by remember { mutableStateOf(false) }

    LaunchedEffect(visible) {
        if (visible && !started) {
            delay(icon.delay.toLong())
            started = true
        }
    }

    val iconState = when {
        !started -> 0
        visible -> 1
        else -> 2
    }

    val transition = updateTransition(
        targetState = iconState,
        label = "iconTransition"
    )

    val animatedProgress by transition.animateFloat(
        transitionSpec = {
            if (targetState == 1) {
                spring(dampingRatio = 0.55f, stiffness = 500f)
            } else {
                tween(
                    durationMillis = if (targetState == 2) 1300 else 900,
                    easing = FastOutSlowInEasing
                )
            }
        },
        label = "iconProgress"
    ) { state ->
        when (state) {
            0 -> 0f
            1 -> 1f
            else -> 1.6f
        }
    }

    val entranceWiggle = if ((icon.delay / 100) % 2 == 0) -18f else 18f

    val animatedScale by transition.animateFloat(
        transitionSpec = {
            if (targetState == 1) {
                spring(dampingRatio = 0.45f, stiffness = 600f)
            } else {
                tween(durationMillis = 250)
            }
        },
        label = "iconScale"
    ) { state ->
        when (state) {
            0 -> 0.35f
            else -> 1f
        }
    }

    val animatedRotation by transition.animateFloat(
        transitionSpec = {
            if (targetState == 1) {
                spring(dampingRatio = 0.4f, stiffness = 500f)
            } else {
                tween(durationMillis = 250)
            }
        },
        label = "iconRotation"
    ) { state ->
        if (state == 0) icon.rotation + entranceWiggle else icon.rotation
    }

    val animatedAlpha by transition.animateFloat(
        transitionSpec = {
            if (targetState == 2) {
                keyframes {
                    durationMillis = 1300
                    1f at 0
                    1f at 350
                    0f at 1300
                }
            } else {
                tween(durationMillis = 500)
            }
        },
        label = "iconAlpha"
    ) { state ->
        if (state == 1) 1f else 0f
    }

    val progress = previewProgress ?: animatedProgress
    val alpha = if (previewProgress != null) {
        if (previewProgress > 0f) 1f else 0f
    } else {
        animatedAlpha
    }
    val scale = if (previewProgress != null) {
        if (previewProgress > 0f) 1f else 0.35f
    } else {
        animatedScale
    }
    val rotation = if (previewProgress != null) icon.rotation else animatedRotation

    val x = icon.x * animationSize * progress
    val y = icon.y * 1.15f * animationSize * progress

    Image(
        painter = painterResource(icon.drawable),
        contentDescription = null,
        colorFilter = ColorFilter.tint(Color.White),
        modifier = Modifier
            .size(28.5.dp)
            .alpha(alpha)
            .scale(scale)
            .rotate(rotation)
            .offset {
                IntOffset(
                    x = x.roundToInt(),
                    y = y.roundToInt()
                )
            }
    )
}

@Preview(
    name = "Speaker - Center",
    showBackground = true,
    backgroundColor = 0xFFA3200C
)
@Composable
private fun SpeakerPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CrimsonPrimary),
        contentAlignment = Alignment.Center
    ) {
        Speaker(
            moveUp = false
        )
    }
}

@Preview(
    name = "Speaker - Moved Up",
    showBackground = true,
    backgroundColor = 0xFFA3200C
)
@Composable
private fun SpeakerMovedPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CrimsonPrimary),
        contentAlignment = Alignment.Center
    ) {
        Speaker(
            moveUp = true
        )
    }
}

@Preview(
    name = "All Sports Icons",
    showBackground = true,
    backgroundColor = 0xFFA3200C,
    widthDp = 394,
    heightDp = 852
)
@Composable
private fun SportsIconAnimationPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CrimsonPrimary),
        contentAlignment = Alignment.Center
    ) {
        sportsIcons.forEach { icon ->
            AnimatedSportsIcon(
                icon = icon,
                1f
            )
        }

        Image(
            painter = painterResource(R.drawable.ic_speaker),
            contentDescription = null,
            modifier = Modifier.size(132.dp),
            colorFilter = ColorFilter.tint(Color.White)
        )
    }
}