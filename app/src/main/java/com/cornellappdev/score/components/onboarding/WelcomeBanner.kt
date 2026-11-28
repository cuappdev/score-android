package com.cornellappdev.score.components.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.R
import com.cornellappdev.score.theme.Style.heading2

@Composable
fun WelcomeBanner(
    name: String?,
    modifier: Modifier = Modifier
) {
    val gradientRed = Brush.horizontalGradient(0f to Color(0xFFBC2811), 1000f to Color(0xFF62180C))
    Row(
        modifier = modifier
            .clip(shape = RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .background(gradientRed)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.ic_speaker),
            colorFilter = ColorFilter.tint(Color.White),
            contentDescription = "Score logo",
            modifier = Modifier.size(50.dp)
        )
        Text(
            text = buildAnnotatedString {
                append("Welcome to Score")
                if (name.isNullOrBlank()) {
                    append("!")
                } else {
                    append(" ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("$name!")
                    }
                }
            },
            modifier = Modifier.weight(1f),
            style = heading2,
            color = Color.White,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
private fun WelcomeBannerPreview() {
    WelcomeBanner("Claire")
}

@Preview
@Composable
private fun WelcomeBannerLongNamePreview() {
    WelcomeBanner("Maximilian-Alexander")
}