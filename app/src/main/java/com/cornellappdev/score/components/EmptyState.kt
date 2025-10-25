package com.cornellappdev.score.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.R
import com.cornellappdev.score.theme.GrayMedium
import com.cornellappdev.score.theme.GrayPrimary
import com.cornellappdev.score.theme.Style.bodyNormal
import com.cornellappdev.score.theme.Style.heading2

@Composable
fun EmptyStateBox(
    @DrawableRes icon: Int,
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String = "Check back here later!",
    height: Dp = 550.dp //(appx height when full-screen error)
) {
    Box(
        modifier = modifier
            .height(height)
            .fillMaxWidth(), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = "empty state icon"
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                style = heading2.copy(color = GrayPrimary)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = subtitle,
                style = bodyNormal.copy(color = GrayMedium)
            )
        }
    }
}


@Preview
@Composable
private fun EmptyStateBoxPreview() = ScorePreview {
    EmptyStateBox(R.drawable.ic_speaker_gray, "No games yet.")
}
