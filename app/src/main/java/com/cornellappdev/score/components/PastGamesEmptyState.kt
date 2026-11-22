package com.cornellappdev.score.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.theme.GrayMedium
import com.cornellappdev.score.theme.GrayStroke
import com.cornellappdev.score.theme.Style.bodySemibold
import com.cornellappdev.score.theme.Style.labelsNormal

@Composable
fun PastGamesEmptyState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(shape = RoundedCornerShape(16.dp))
            .border(width = 1.dp, color = GrayStroke, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 24.dp, vertical = 32.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "No Scores",
            style = bodySemibold,
            color = GrayMedium
        )
        Text(
            text = "Sport is out of season, check back soon!",
            style = labelsNormal,
            color = GrayMedium
        )
    }
}

@Preview
@Composable
private fun PastGamesEmptyStatePreview() {
    PastGamesEmptyState()
}