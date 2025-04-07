package com.cornellappdev.score.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.theme.GrayStroke
import com.cornellappdev.score.theme.Style.heading1
import com.cornellappdev.score.util.LocalInfiniteLoading
import com.cornellappdev.score.util.Stroke
import com.cornellappdev.score.util.Wash
import com.cornellappdev.score.util.interpolateColorHSV

@Composable
fun LoadingScreen(
    topHeader: String,
    bottomHeader: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = topHeader,
                style = heading1,
                color = GrayStroke,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(16.dp))
            LoadingStateBox(12, 200.dp)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(32.dp),
            ) {
                for (i in 0 until 3) {
                    Canvas(
                        modifier = Modifier
                            .size(10.dp)
                            .padding(2.dp)
                    ) {
                        drawCircle(color = GrayStroke)
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = bottomHeader,
                style = heading1,
                color = GrayStroke,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(16.dp))
            LoadingStateBox(100, 50.dp)
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                for (i in 0 until 5) {
                    LoadingFilter()
                }
            }
        }
        Divider(color = GrayStroke)
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            LoadingStateBox(12, 100.dp)
            Spacer(modifier = Modifier.height(16.dp))
            LoadingStateBox(12, 100.dp)
        }
    }
}

@Composable
private fun LoadingFilter() {
    val shimmerColor = interpolateColorHSV(Wash, Stroke, LocalInfiniteLoading.current)

    Column(
        modifier = Modifier
            .width(54.dp)
            .height(56.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Canvas(
            modifier = Modifier
                .size(24.dp)
                .padding(2.dp)
        ) {
            drawCircle(color = shimmerColor)
        }
        Spacer(modifier = Modifier.height(6.dp))
        LoadingStateBox(100, 12.dp)
    }
}

@Preview
@Composable
private fun LoadingFilterPreview() {
    LoadingFilter()
}

@Preview
@Composable
private fun LoadingScreenPreview() {
    LoadingScreen("Loading Upcoming...", "Loading Schedules...")
}