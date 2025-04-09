package com.cornellappdev.score.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.R
import com.cornellappdev.score.theme.GrayStroke
import com.cornellappdev.score.theme.LocalInfiniteLoading
import com.cornellappdev.score.theme.Stroke
import com.cornellappdev.score.theme.Wash
import com.cornellappdev.score.util.interpolateColorHSV

@Composable
fun GameDetailsLoadingScreen(
    modifier: Modifier = Modifier
) {
    val shimmerColor = interpolateColorHSV(Wash, Stroke, LocalInfiniteLoading.current)
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .background(color = shimmerColor)
                .fillMaxWidth()
                .height(185.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Canvas(
                modifier = Modifier
                    .size(72.dp)
            ) {
                drawCircle(color = shimmerColor)
            }
            Spacer(modifier = Modifier.width(24.dp))
            LoadingStateBox(100, 33.dp, Modifier.width(100.dp))
            Spacer(modifier = Modifier.width(24.dp))
            Canvas(
                modifier = Modifier
                    .size(72.dp)
            ) {
                drawCircle(color = shimmerColor)
            }
        }
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            LoadingStateBox(12, 16.dp, Modifier.width(100.dp))
            Spacer(modifier = Modifier.height(12.dp))
            LoadingStateBox(100, 32.dp, Modifier.width(200.dp))
            Spacer(modifier = Modifier.height(16.dp))
            LoadingStateBox(12, 16.dp)

            Spacer(modifier = Modifier.height(24.dp))
            LoadingStateBox(8, 125.dp)
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Loading Score Summary",
                    color = GrayStroke,
                    style = typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                Image(
                    painter = painterResource(R.drawable.ic_right_chevron),
                    contentDescription = "right chevron"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            LoadingStateBox(8, 48.dp)
            Spacer(modifier = Modifier.height(16.dp))
            LoadingStateBox(8, 48.dp)
            Spacer(modifier = Modifier.height(16.dp))
            LoadingStateBox(8, 48.dp)
        }
    }
}

@Preview
@Composable
private fun GameDetailsLoadingStatePreview() = ScorePreview{
    GameDetailsLoadingScreen()
}