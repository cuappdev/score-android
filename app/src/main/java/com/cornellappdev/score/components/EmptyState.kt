package com.cornellappdev.score.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.R
import com.cornellappdev.score.theme.GrayMedium
import com.cornellappdev.score.theme.GrayPrimary
import com.cornellappdev.score.theme.Style.bodyNormal
import com.cornellappdev.score.theme.Style.heading2

@Composable
fun EmptyState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_speaker_gray),
            contentDescription = "score speaker icon"
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No games yet.",
            style = heading2.copy(color = GrayPrimary)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Check back here later!",
            style = bodyNormal.copy(color = GrayMedium)
        )
    }
}

@Preview
@Composable
private fun EmptyStatePreview() = ScorePreview {
    EmptyState()
}