package com.cornellappdev.score.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.util.LocalInfiniteLoading
import com.cornellappdev.score.util.Stroke
import com.cornellappdev.score.util.Wash
import com.cornellappdev.score.util.interpolateColorHSV

@Composable
fun LoadingStateBox(
    cornerRoundness: Int,
    height: Dp,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    Box(
        modifier = modifier
            .height(height)
            .background(
                color = interpolateColorHSV(
                    Wash,
                    Stroke,
                    LocalInfiniteLoading.current
                ),
                shape = RoundedCornerShape(cornerRoundness)
            )
            .fillMaxWidth()
    )

}

@Preview
@Composable
private fun LoadingStateBoxPreview() {
    LoadingStateBox(12, 16.dp)
}