package com.cornellappdev.score.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.R
import com.cornellappdev.score.theme.GrayLight
import com.cornellappdev.score.theme.GrayMedium
import com.cornellappdev.score.theme.GrayPrimary
import com.cornellappdev.score.theme.Style.bodyNormal
import com.cornellappdev.score.theme.Style.heading2

@Composable
fun EmptyStateMessage(modifier: Modifier = Modifier) {
    Column(modifier = modifier.width(158.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.ic_speaker),
            contentDescription = "Speaker Icon",
            modifier = Modifier
                .padding(2.dp)
                .width(96.dp)
                .height(96.dp),
            colorFilter = ColorFilter.tint(GrayLight)
        )
        Spacer(modifier = Modifier.height(12.dp))
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
fun PreviewEmptyStateMessage() {
    Box(modifier = Modifier.background(Color.White)) {
        EmptyStateMessage()
    }
}