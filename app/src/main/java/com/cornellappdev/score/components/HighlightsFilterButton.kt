package com.cornellappdev.score.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.theme.GrayStroke
import com.cornellappdev.score.theme.Style.bodyNormal

@Composable
fun HighlightsFilterButton(
    sport: Sport,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .height(32.dp)
            .border(
                width = 1.dp,
                color = GrayStroke,
                shape = RoundedCornerShape(100)
            )
            .background(color = Color.White, shape = RoundedCornerShape(100))
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clip(shape = RoundedCornerShape(100))
            .clickable { onClick },
        verticalAlignment = Alignment.CenterVertically,
        ) {
        Image(
            painter = painterResource(sport.emptyIcon),
            contentDescription = "sport Icon"
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = sport.displayName,
            style = bodyNormal
        )
    }
}

@Preview
@Composable
private fun HighlightsFilterButtonPreview() {
    HighlightsFilterButton(Sport.BASEBALL, {})
}