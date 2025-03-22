package com.cornellappdev.score.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.cornellappdev.score.theme.GrayStroke

@Composable
fun LoadingStateBox(cornerRoundness: Int, height: Dp) {
    Box(
        modifier = Modifier
            .background(color = GrayStroke, shape = RoundedCornerShape(cornerRoundness))
            .height(height)
            .fillMaxWidth()
    )
}