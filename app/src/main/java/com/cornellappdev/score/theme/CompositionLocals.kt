package com.cornellappdev.score.theme

import androidx.compose.runtime.compositionLocalOf

val LocalInfiniteLoading = compositionLocalOf<Float> { error("No infinite loading provided") }