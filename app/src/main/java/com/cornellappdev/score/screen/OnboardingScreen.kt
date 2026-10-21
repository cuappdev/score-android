package com.cornellappdev.score.screen

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.R
import com.cornellappdev.score.components.OnboardingButtons
import com.cornellappdev.score.theme.CrimsonPrimary
import kotlin.math.min

private const val TIMELINE_DURATION_MS = 3_850
private const val ICONS_HOLD_END_MS = 2_420
private const val ICONS_FADE_END_MS = 3_020
private const val ICONS_EXIT_END_MS = 3_080

private data class MotionKey(val timeMs: Float, val value: Float, val easing: Easing = LinearEasing)

private fun sampleTrack(timeMs: Float, keys: List<MotionKey>): Float {
    if (timeMs <= keys.first().timeMs) return keys.first().value
    for (index in 0 until keys.lastIndex) {
        val start = keys[index]
        val end = keys[index + 1]
        if (timeMs <= end.timeMs) {
            val fraction = ((timeMs - start.timeMs) / (end.timeMs - start.timeMs)).coerceIn(0f, 1f)
            return start.value + (end.value - start.value) * start.easing.transform(fraction)
        }
    }
    return keys.last().value
}

private data class SportMotion(
    @DrawableRes val drawable: Int,
    val left: Float,
    val top: Float,
    val enterAtMs: Float,
    val peakAlpha: Float,
    val entryX: Float,
    val entryY: Float,
    val bounceX: Float,
    val bounceY: Float,
    val settleX: Float,
    val settleY: Float,
    val initialRotation: Float
)

private val sportMotions = listOf(
    SportMotion(
        R.drawable.ic_basketball,
        172f,
        237f,
        340f,
        .94f,
        5f,
        141f,
        13.441f,
        -16.006f,
        -6.071f,
        6.568f,
        -18f
    ),
    SportMotion(
        R.drawable.ic_baseball,
        285f,
        299f,
        385f,
        .94f,
        -108f,
        79f,
        3.615f,
        -19.99f,
        -1.141f,
        8.64f,
        18f
    ),
    SportMotion(
        R.drawable.ic_cross_country,
        20f,
        331f,
        430f,
        .94f,
        157f,
        47f,
        -13.255f,
        -18.582f,
        5.258f,
        8.15f,
        -18f
    ),
    SportMotion(
        R.drawable.ic_football,
        316f,
        441f,
        475f,
        .94f,
        -139f,
        -63f,
        21.069f,
        -5.821f,
        -8.856f,
        2.903f,
        18f
    ),
    SportMotion(
        R.drawable.ic_golf,
        176f,
        526f,
        520f,
        .94f,
        1f,
        -148f,
        -14.11f,
        16.185f,
        6.345f,
        -6.617f,
        -18f
    ),
    SportMotion(
        R.drawable.ic_volleyball,
        20f,
        482f,
        565f,
        .94f,
        157f,
        -104f,
        -9.539f,
        23.112f,
        3.586f,
        -9.932f,
        18f
    ),
    SportMotion(
        R.drawable.ic_ice_hockey,
        220f,
        179f,
        580f,
        .8f,
        -43f,
        199f,
        18.414f,
        -18.933f,
        -8.093f,
        7.624f,
        -18f
    ),
    SportMotion(
        R.drawable.ic_swim_dive,
        259f,
        500f,
        610f,
        .94f,
        -82f,
        -122f,
        -2.599f,
        21.23f,
        1.539f,
        -9.004f,
        -18f
    ),
    SportMotion(
        R.drawable.ic_soccer,
        251f,
        240f,
        625f,
        .8f,
        -74f,
        138f,
        -4.198f,
        -21.796f,
        2.222f,
        9.187f,
        18f
    ),
    SportMotion(
        R.drawable.ic_tennis,
        334f,
        526f,
        670f,
        .8f,
        -157f,
        -148f,
        7.667f,
        26.467f,
        -2.744f,
        -11.244f,
        -18f
    ),
    SportMotion(
        R.drawable.ic_lacrosse,
        255f,
        582f,
        715f,
        .8f,
        -78f,
        -204f,
        21.657f,
        17.44f,
        -9.395f,
        -6.93f,
        18f
    ),
    SportMotion(
        R.drawable.ic_wrestling,
        94f,
        508f,
        760f,
        .8f,
        83f,
        -130f,
        -20.93f,
        6.766f,
        9.045f,
        -2.46f,
        -18f
    ),
    SportMotion(
        R.drawable.ic_fencing,
        23f,
        223f,
        805f,
        .8f,
        154f,
        155f,
        -26.871f,
        -7.183f,
        11.399f,
        2.535f,
        18f
    ),
    SportMotion(
        R.drawable.ic_softball,
        41f,
        575f,
        820f,
        .66f,
        136f,
        -197f,
        -26.481f,
        13.716f,
        11.305f,
        -5.286f,
        -18f
    ),
    SportMotion(
        R.drawable.ic_sailing,
        72f,
        263f,
        850f,
        .8f,
        105f,
        115f,
        -1.211f,
        -22.09f,
        .073f,
        9.423f,
        -18f
    ),
    SportMotion(
        R.drawable.ic_equestrian,
        142f,
        591f,
        865f,
        .66f,
        35f,
        -213f,
        9.965f,
        25.7f,
        -4.642f,
        -10.607f,
        18f
    ),
    SportMotion(
        R.drawable.ic_field_hockey,
        334f,
        355f,
        910f,
        .66f,
        -157f,
        23f,
        19.299f,
        11.322f,
        -7.978f,
        -5.198f,
        -18f
    ),
    SportMotion(
        R.drawable.ic_gymnastics,
        338f,
        257f,
        955f,
        .66f,
        -161f,
        121f,
        9.299f,
        -24.502f,
        -3.46f,
        10.481f,
        18f
    ),
    SportMotion(
        R.drawable.ic_squash,
        61f,
        415f,
        1_000f,
        .66f,
        116f,
        -37f,
        -17.014f,
        -9.268f,
        7.134f,
        4.337f,
        -18f
    ),
    SportMotion(
        R.drawable.ic_rowing_lightweight,
        101f,
        191f,
        1_045f,
        .66f,
        76f,
        187f,
        -21.33f,
        -15.299f,
        9.256f,
        6.043f,
        18f
    ),
    SportMotion(
        R.drawable.ic_track_field,
        302f,
        201f,
        1_090f,
        .66f,
        -125f,
        177f,
        25.186f,
        -11.394f,
        -10.771f,
        4.331f,
        -18f
    )
)

@Composable
fun OnboardingScreen(
    onSignUp: () -> Unit,
    onLogIn: () -> Unit,
    onLater: () -> Unit,
    modifier: Modifier = Modifier,
    animationKey: Int = 0
) {
    val animationTimeMs = remember(animationKey) { Animatable(0f) }
    LaunchedEffect(animationKey) {
        animationTimeMs.snapTo(0f)
        animationTimeMs.animateTo(
            TIMELINE_DURATION_MS.toFloat(),
            animationSpec = tween(TIMELINE_DURATION_MS, easing = LinearEasing)
        )
    }
    val timeMs = animationTimeMs.value

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(CrimsonPrimary)
    ) {
        /*designScale and designDp scale from Figma units to Compose units*/
        val designScale = min(maxWidth.value / 393f, maxHeight.value / 852f)
        val designDp: (Float) -> Dp = { (it * designScale).dp }

        AnimatedSpeaker(
            timeMs = timeMs,
            left = 98f,
            top = 229f,
            size = 197f,
            designDp = designDp,
            alphaKeys = listOf(MotionKey(0f, 0f), MotionKey(2_760f, 0f), MotionKey(3_280f, 1f)),
            scaleKeys = listOf(MotionKey(0f, .67f), MotionKey(2_760f, .67f), MotionKey(3_280f, 1f)),
            yKeys = listOf(
                MotionKey(0f, 68.5f),
                MotionKey(2_760f, 68.5f),
                MotionKey(3_280f, 0f, FastOutSlowInEasing)
            )
        )

        if (timeMs >= 3_020f) {
            OnboardingButtons(
                onSignUp = onSignUp,
                onLogIn = onLogIn,
                onLater = onLater,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(start = 24.dp, end = 24.dp, bottom = 40.dp)
            )
        }

        AnimatedHalo(
            timeMs = timeMs,
            designDp = designDp
        )
        AnimatedRing(
            timeMs = timeMs,
            designDp = designDp,
            delayMs = 240f,
            opacityPeak = .72f,
            opacityInEnd = 420f,
            opacityOutStart = 420f,
            opacityOutEnd = 1_020f,
            scalePeak = 1.55f,
            scalePeakTime = 1_020f,
            scaleExit = 2.092f
        )
        AnimatedRing(
            timeMs = timeMs,
            designDp = designDp,
            delayMs = 420f,
            opacityPeak = .5f,
            opacityInEnd = 620f,
            opacityOutStart = 620f,
            opacityOutEnd = 1_260f,
            scalePeak = 1.72f,
            scalePeakTime = 1_260f,
            scaleExit = 2.322f
        )

        AnimatedSpeaker(
            timeMs = timeMs,
            left = 129f,
            top = 330f,
            size = 132f,
            designDp = designDp,
            alphaKeys = listOf(
                MotionKey(0f, 0f),
                MotionKey(180f, 1f, FastOutSlowInEasing),
                MotionKey(3_120f, 1f),
                MotionKey(3_340f, 0f, FastOutLinearInEasing)
            ),
            scaleKeys = listOf(
                MotionKey(0f, .54f),
                MotionKey(420f, 1.17f, FastOutSlowInEasing),
                MotionKey(630f, .94f, FastOutSlowInEasing),
                MotionKey(820f, 1f, FastOutSlowInEasing),
                MotionKey(2_420f, 1f),
                MotionKey(3_220f, 1.492f, FastOutSlowInEasing)
            ),
            yKeys = listOf(
                MotionKey(0f, 34f),
                MotionKey(420f, -9f, FastOutSlowInEasing),
                MotionKey(630f, 4f, FastOutSlowInEasing),
                MotionKey(820f, 0f, FastOutSlowInEasing),
                MotionKey(2_420f, 0f),
                MotionKey(3_220f, -68.5f, FastOutSlowInEasing)
            ),
            rotationKeys = listOf(
                MotionKey(0f, 9f),
                MotionKey(420f, -6f, FastOutSlowInEasing),
                MotionKey(630f, 3f, FastOutSlowInEasing),
                MotionKey(820f, 0f, FastOutSlowInEasing)
            )
        )

        sportMotions.forEach { sport ->
            AnimatedSportIcon(timeMs = timeMs, motion = sport, designDp = designDp)
        }
    }
}

@Composable
private fun AnimatedSpeaker(
    timeMs: Float,
    left: Float,
    top: Float,
    size: Float,
    designDp: (Float) -> Dp,
    alphaKeys: List<MotionKey>,
    scaleKeys: List<MotionKey>,
    yKeys: List<MotionKey>,
    rotationKeys: List<MotionKey> = listOf(MotionKey(0f, 0f))
) {
    val y = sampleTrack(timeMs, yKeys)
    val alpha = sampleTrack(timeMs, alphaKeys).coerceIn(0f, 1f)
    val scale = sampleTrack(timeMs, scaleKeys)
    val rotation = sampleTrack(timeMs, rotationKeys)

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.ic_speaker),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier
                .align(Alignment.Center)
                .offset(
                    x = designDp(left + size / 2f - 196.5f),
                    y = designDp(top + size / 2f - 426f + y)
                )
                .size(designDp(size))
                .graphicsLayer {
                    this.alpha = alpha
                    scaleX = scale
                    scaleY = scale
                    rotationZ = rotation
                }
        )
    }
}

@Composable
private fun AnimatedHalo(timeMs: Float, designDp: (Float) -> Dp) {
    val alpha = sampleTrack(
        timeMs,
        listOf(
            MotionKey(0f, 0f),
            MotionKey(240f, 1f, FastOutSlowInEasing),
            MotionKey(1_160f, .72f),
            MotionKey(2_420f, .72f),
            MotionKey(2_920f, 0f, FastOutLinearInEasing)
        )
    )
    val scale = sampleTrack(
        timeMs,
        listOf(
            MotionKey(0f, .62f),
            MotionKey(460f, 1.12f, FastOutSlowInEasing),
            MotionKey(840f, 1f, FastOutSlowInEasing),
            MotionKey(2_420f, 1f),
            MotionKey(2_980f, 1.35f, FastOutSlowInEasing)
        )
    )
    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = designDp(-1.5f), y = designDp(-30f))
                .size(designDp(188f))
                .graphicsLayer {
                    this.alpha = alpha
                    scaleX = scale
                    scaleY = scale
                }
        ) {
            drawCircle(Color.White.copy(alpha = .055f))
            drawCircle(Color.White.copy(alpha = .16f), style = Stroke(width = 1.dp.toPx()))
        }
    }
}

@Composable
private fun AnimatedRing(
    timeMs: Float,
    designDp: (Float) -> Dp,
    delayMs: Float,
    opacityPeak: Float,
    opacityInEnd: Float,
    opacityOutStart: Float,
    opacityOutEnd: Float,
    scalePeak: Float,
    scalePeakTime: Float,
    scaleExit: Float
) {
    val alpha = sampleTrack(
        timeMs,
        listOf(
            MotionKey(0f, 0f),
            MotionKey(delayMs, 0f),
            MotionKey(opacityInEnd, opacityPeak, FastOutSlowInEasing),
            MotionKey(opacityOutEnd, 0f, FastOutLinearInEasing)
        )
    )
    val scale = sampleTrack(
        timeMs,
        listOf(
            MotionKey(0f, .28f),
            MotionKey(delayMs, .28f),
            MotionKey(scalePeakTime, scalePeak, FastOutSlowInEasing),
            MotionKey(2_420f, scalePeak),
            MotionKey(2_980f, scaleExit, FastOutSlowInEasing)
        )
    )
    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = designDp(-1.5f), y = designDp(-30f))
                .size(designDp(188f))
                .graphicsLayer {
                    this.alpha = alpha.coerceIn(0f, 1f)
                    scaleX = scale
                    scaleY = scale
                }
        ) {
            drawCircle(
                color = Color.White.copy(alpha = .18f),
                style = Stroke(width = 1.dp.toPx())
            )
        }
    }
}

@Composable
private fun AnimatedSportIcon(
    timeMs: Float,
    motion: SportMotion,
    designDp: (Float) -> Dp
) {
    val peakTime = motion.enterAtMs + 160f
    val bounceTime = motion.enterAtMs + 310f
    val settleTime = motion.enterAtMs + 520f
    val landedTime = motion.enterAtMs + 700f
    val wiggleTime = motion.enterAtMs + 860f
    val settledTime = motion.enterAtMs + 1_020f
    val centerX = motion.left + 18f - 196.5f
    val centerY = motion.top + 18f - 426f
    val radius = kotlin.math.hypot(centerX, centerY).coerceAtLeast(1f)
    val entryX = centerX / radius * 450f
    val entryY = centerY / radius * 450f
    val exitX = centerX / radius * 58f
    val exitY = centerY / radius * 58f

    val alpha = sampleTrack(
        timeMs,
        listOf(
            MotionKey(0f, 0f),
            MotionKey(motion.enterAtMs, 0f),
            MotionKey(peakTime, motion.peakAlpha),
            MotionKey(ICONS_HOLD_END_MS.toFloat(), motion.peakAlpha),
            MotionKey(ICONS_FADE_END_MS.toFloat(), 0f, FastOutLinearInEasing)
        )
    )
    val x = sampleTrack(
        timeMs,
        listOf(
            MotionKey(0f, entryX),
            MotionKey(motion.enterAtMs, entryX),
            MotionKey(bounceTime, motion.bounceX, FastOutSlowInEasing),
            MotionKey(settleTime, motion.settleX, FastOutSlowInEasing),
            MotionKey(landedTime, 0f, FastOutSlowInEasing),
            MotionKey(ICONS_HOLD_END_MS.toFloat(), 0f),
            MotionKey(ICONS_EXIT_END_MS.toFloat(), exitX, FastOutLinearInEasing)
        )
    )
    val y = sampleTrack(
        timeMs,
        listOf(
            MotionKey(0f, entryY),
            MotionKey(motion.enterAtMs, entryY),
            MotionKey(bounceTime, motion.bounceY, FastOutSlowInEasing),
            MotionKey(settleTime, motion.settleY, FastOutSlowInEasing),
            MotionKey(landedTime, 0f, FastOutSlowInEasing),
            MotionKey(wiggleTime, -5f, FastOutSlowInEasing),
            MotionKey(settledTime, 0f, FastOutSlowInEasing),
            MotionKey(ICONS_HOLD_END_MS.toFloat(), 0f),
            MotionKey(ICONS_EXIT_END_MS.toFloat(), exitY, FastOutLinearInEasing)
        )
    )
    val scale = sampleTrack(
        timeMs,
        listOf(
            MotionKey(0f, .45f),
            MotionKey(motion.enterAtMs, .45f),
            MotionKey(bounceTime, 1.13f, FastOutSlowInEasing),
            MotionKey(settleTime, .94f, FastOutSlowInEasing),
            MotionKey(landedTime, 1f, FastOutSlowInEasing),
            MotionKey(wiggleTime, 1.04f, FastOutSlowInEasing),
            MotionKey(settledTime, 1f, FastOutSlowInEasing),
            MotionKey(ICONS_HOLD_END_MS.toFloat(), 1f),
            MotionKey(ICONS_EXIT_END_MS.toFloat(), .68f, FastOutLinearInEasing)
        )
    )
    val rotation = sampleTrack(
        timeMs,
        listOf(
            MotionKey(0f, motion.initialRotation),
            MotionKey(motion.enterAtMs, motion.initialRotation),
            MotionKey(bounceTime, motion.initialRotation * -.5f, FastOutSlowInEasing),
            MotionKey(settleTime, motion.initialRotation * .22f, FastOutSlowInEasing),
            MotionKey(landedTime, 0f, FastOutSlowInEasing),
            MotionKey(wiggleTime, motion.initialRotation * .22f, FastOutSlowInEasing),
            MotionKey(settledTime, 0f, FastOutSlowInEasing),
            MotionKey(ICONS_HOLD_END_MS.toFloat(), 0f),
            MotionKey(
                ICONS_EXIT_END_MS.toFloat(),
                motion.initialRotation * .55f,
                FastOutLinearInEasing
            )
        )
    )

    Box(
        modifier = Modifier
            .offset(x = designDp(motion.left), y = designDp(motion.top))
            .size(designDp(36f)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(motion.drawable),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier
                .size(36.dp)
                .graphicsLayer {
                    this.alpha = alpha.coerceIn(0f, 1f)
                    scaleX = scale
                    scaleY = scale
                    rotationZ = rotation
                    translationX = designDp(x).toPx()
                    translationY = designDp(y).toPx()
                }
        )
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun OnboardingScreenPreview() {
    OnboardingScreen(onSignUp = {}, onLogIn = {}, onLater = {})
}
