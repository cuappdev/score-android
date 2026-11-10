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
import com.cornellappdev.score.util.SportMotion
import com.cornellappdev.score.util.sportMotions
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlin.math.min

private const val TIMELINE_DURATION_MS = 3_850
private const val ICONS_HOLD_END_MS = 2_420
private const val ICONS_FADE_END_MS = 3_020
private const val ICONS_EXIT_END_MS = 3_080

private const val DESIGN_FRAME_WIDTH = 393f
private const val DESIGN_FRAME_HEIGHT = 852f
private const val DESIGN_CENTER_X = DESIGN_FRAME_WIDTH / 2f
private const val DESIGN_CENTER_Y = DESIGN_FRAME_HEIGHT / 2f
private const val ICON_SIZE = 36f
private const val ICON_CENTER_OFFSET = ICON_SIZE / 2f
private const val HALO_SIZE = 188f
private const val HALO_CENTER_OFFSET_X = -1.5f
private const val HALO_CENTER_OFFSET_Y = -30f
private const val ICON_ENTRY_DISTANCE = 450f
private const val ICON_EXIT_DISTANCE = 58f
private const val SPEAKER_REVEAL_START_MS = 2_760f
private const val SPEAKER_REVEAL_END_MS = 3_280f
private val BUTTON_HORIZONTAL_PADDING = 24.dp
private const val SPEAKER_BOUNCE_MS = 420f
private const val SPEAKER_SETTLE_MS = 630f
private const val SPEAKER_LANDED_MS = 820f
private const val SPEAKER_MOVE_UP_MS = 3_220f
private const val HALO_SCALE_EXIT_MS = 2_980f
private const val RING_FIRST_OUT_END_MS = 1_020f
private const val RING_SECOND_OUT_END_MS = 1_260f

private data class MotionKey(
    val timeMs: Float,
    val value: Float,
    val easing: Easing = LinearEasing
)

/**
 * Tells an animated element what value to use at the current point in the onboarding animation.
 * The value is the visual property tracked here, such as opacity, scale, position, or rotation.
 * Between keyframes, it is interpolated using the easing on the starting keyframe; times before
 * or after the track use the first or last keyframe value. Keep [keys] ordered by time.
 */
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
        val designScale = min(maxWidth.value / DESIGN_FRAME_WIDTH, maxHeight.value / DESIGN_FRAME_HEIGHT)
        val designDp: (Float) -> Dp = { (it * designScale).dp }

        AnimatedSpeaker(
            timeMs = timeMs,
            left = 98f,
            top = 229f,
            size = 197f,
            designDp = designDp,
            alphaKeys = persistentListOf(MotionKey(0f, 0f), MotionKey(SPEAKER_REVEAL_START_MS, 0f), MotionKey(SPEAKER_REVEAL_END_MS, 1f)),
            scaleKeys = persistentListOf(MotionKey(0f, .67f), MotionKey(SPEAKER_REVEAL_START_MS, .67f), MotionKey(SPEAKER_REVEAL_END_MS, 1f)),
            yKeys = persistentListOf(
                MotionKey(0f, 68.5f),
                MotionKey(SPEAKER_REVEAL_START_MS, 68.5f),
                MotionKey(SPEAKER_REVEAL_END_MS, 0f, FastOutSlowInEasing)
            )
        )

        if (timeMs >= ICONS_FADE_END_MS) {
            OnboardingButtons(
                onSignUp = onSignUp,
                onLogIn = onLogIn,
                onLater = onLater,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(
                        start = BUTTON_HORIZONTAL_PADDING,
                        end = BUTTON_HORIZONTAL_PADDING,
                        bottom = 40.dp
                    )
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
            opacityOutEnd = RING_FIRST_OUT_END_MS,
            scalePeak = 1.55f,
            scalePeakTime = RING_FIRST_OUT_END_MS,
            scaleExit = 2.092f
        )
        AnimatedRing(
            timeMs = timeMs,
            designDp = designDp,
            delayMs = 420f,
            opacityPeak = .5f,
            opacityInEnd = 620f,
            opacityOutEnd = RING_SECOND_OUT_END_MS,
            scalePeak = 1.72f,
            scalePeakTime = RING_SECOND_OUT_END_MS,
            scaleExit = 2.322f
        )

        AnimatedSpeaker(
            timeMs = timeMs,
            left = 129f,
            top = 330f,
            size = 132f,
            designDp = designDp,
            alphaKeys = persistentListOf(
                MotionKey(0f, 0f),
                MotionKey(180f, 1f, FastOutSlowInEasing),
                MotionKey(3_120f, 1f),
                MotionKey(3_340f, 0f, FastOutLinearInEasing)
            ),
            scaleKeys = persistentListOf(
                MotionKey(0f, .54f),
                MotionKey(SPEAKER_BOUNCE_MS, 1.17f, FastOutSlowInEasing),
                MotionKey(SPEAKER_SETTLE_MS, .94f, FastOutSlowInEasing),
                MotionKey(SPEAKER_LANDED_MS, 1f, FastOutSlowInEasing),
                MotionKey(ICONS_HOLD_END_MS.toFloat(), 1f),
                MotionKey(SPEAKER_MOVE_UP_MS, 1.492f, FastOutSlowInEasing)
            ),
            yKeys = persistentListOf(
                MotionKey(0f, 34f),
                MotionKey(SPEAKER_BOUNCE_MS, -9f, FastOutSlowInEasing),
                MotionKey(SPEAKER_SETTLE_MS, 4f, FastOutSlowInEasing),
                MotionKey(SPEAKER_LANDED_MS, 0f, FastOutSlowInEasing),
                MotionKey(ICONS_HOLD_END_MS.toFloat(), 0f),
                MotionKey(SPEAKER_MOVE_UP_MS, -68.5f, FastOutSlowInEasing)
            ),
            rotationKeys = persistentListOf(
                MotionKey(0f, 9f),
                MotionKey(SPEAKER_BOUNCE_MS, -6f, FastOutSlowInEasing),
                MotionKey(SPEAKER_SETTLE_MS, 3f, FastOutSlowInEasing),
                MotionKey(SPEAKER_LANDED_MS, 0f, FastOutSlowInEasing)
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
    alphaKeys: ImmutableList<MotionKey>,
    scaleKeys: ImmutableList<MotionKey>,
    yKeys: ImmutableList<MotionKey>,
    rotationKeys: ImmutableList<MotionKey> = persistentListOf(MotionKey(0f, 0f))
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
                    // Normalize the icon's direction from the center, then scale it to get its entry and exit offsets.
                    x = designDp(left + size / 2f - DESIGN_CENTER_X),
                    y = designDp(top + size / 2f - DESIGN_CENTER_Y + y)
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
            MotionKey(ICONS_HOLD_END_MS.toFloat(), .72f),
            MotionKey(2_920f, 0f, FastOutLinearInEasing)
        )
    )
    val scale = sampleTrack(
        timeMs,
        listOf(
            MotionKey(0f, .62f),
            MotionKey(460f, 1.12f, FastOutSlowInEasing),
            MotionKey(840f, 1f, FastOutSlowInEasing),
            MotionKey(ICONS_HOLD_END_MS.toFloat(), 1f),
            MotionKey(HALO_SCALE_EXIT_MS, 1.35f, FastOutSlowInEasing)
        )
    )
    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = designDp(HALO_CENTER_OFFSET_X), y = designDp(HALO_CENTER_OFFSET_Y))
                .size(designDp(HALO_SIZE))
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
            MotionKey(ICONS_HOLD_END_MS.toFloat(), scalePeak),
            MotionKey(HALO_SCALE_EXIT_MS, scaleExit, FastOutSlowInEasing)
        )
    )
    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = designDp(HALO_CENTER_OFFSET_X), y = designDp(HALO_CENTER_OFFSET_Y))
                .size(designDp(HALO_SIZE))
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
    val centerX = motion.left + ICON_CENTER_OFFSET - DESIGN_CENTER_X
    val centerY = motion.top + ICON_CENTER_OFFSET - DESIGN_CENTER_Y
    val radius = kotlin.math.hypot(centerX, centerY).coerceAtLeast(1f)
    val entryX = centerX / radius * ICON_ENTRY_DISTANCE
    val entryY = centerY / radius * ICON_ENTRY_DISTANCE
    val exitX = centerX / radius * ICON_EXIT_DISTANCE
    val exitY = centerY / radius * ICON_EXIT_DISTANCE

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

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(
                    x = designDp(motion.left + ICON_CENTER_OFFSET - DESIGN_CENTER_X),
                    y = designDp(motion.top + ICON_CENTER_OFFSET - DESIGN_CENTER_Y)
                )
                .size(designDp(ICON_SIZE)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(motion.drawable),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .size(designDp(ICON_SIZE))
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
}

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun OnboardingScreenPreview() {
    OnboardingScreen(onSignUp = {}, onLogIn = {}, onLater = {})
}
