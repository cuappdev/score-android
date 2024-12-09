package com.cornellappdev.score.components

import android.app.GameState
import android.graphics.Paint.Align
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.R
import com.cornellappdev.score.theme.Style.scoreHeaderText
import com.cornellappdev.score.theme.Style.vsText

@Composable
fun GameScoreHeader(
    leftTeamLogo: Painter,
    rightTeamLogo: Painter,
    gradientColor1: Color,
    gradientColor2: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(gradientColor1, gradientColor2)
                )
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = leftTeamLogo,
                contentDescription = "Left Team Logo",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.size(70.dp)
            )

            Row {
                Text(
                    text = "0",
                    style = scoreHeaderText,
                    modifier = Modifier.width(52.dp),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "-",
                    style = scoreHeaderText,
                )

                Text(
                    text = "0",
                    style = scoreHeaderText,
                    modifier = Modifier.width(52.dp),
                    textAlign = TextAlign.Center
                )
            }

            Image(
                painter = rightTeamLogo,
                contentDescription = "Right Team Logo",
                modifier = Modifier.height(70.dp)
            )
        }
    }
}

@Preview
@Composable
fun GameScoreHeaderPreview() {
    GameScoreHeader(
        leftTeamLogo = painterResource(R.drawable.cornell_logo),
        rightTeamLogo = painterResource(R.drawable.penn_logo),
        gradientColor1 = Color(0xFFE1A69F),
        gradientColor2 = Color(0xFF011F5B),
        modifier = Modifier.height(185.dp)
    )
}