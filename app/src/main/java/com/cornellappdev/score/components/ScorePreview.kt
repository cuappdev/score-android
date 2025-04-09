package com.cornellappdev.score.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.theme.LocalInfiniteLoading

@Composable
fun ScorePreview(
    padding: Dp = 0.dp,
    backgroundColor: Color = Color.White,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalInfiniteLoading provides 0f
    ) {
        Column(
            modifier = Modifier
                .background(backgroundColor)
                .padding(padding)
        ) {
            content()
        }
    }
}