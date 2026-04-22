package com.cornellappdev.score.components.highlights

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.components.LoadingStateBox
import com.cornellappdev.score.screen.HighlightsSubScreenHeader
import com.cornellappdev.score.theme.GrayStroke
import com.cornellappdev.score.theme.Style.heading1
import com.cornellappdev.score.theme.Style.heading2

@Composable
fun HighlightsLoadingScreen(
    topHeader: String, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
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
            LoadingStateBox(100, 40.dp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.padding(start = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            repeat(4) {
                LoadingStateBox(
                    100, 30.dp, modifier = Modifier.width(85.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Loading Today...", style = heading2, color = GrayStroke
            )
            LoadingStateBox(
                100, 15.dp, modifier = Modifier.width(
                    50.dp
                )
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.padding(start = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LoadingStateBox(
                12, 192.dp, modifier = Modifier.width(
                    241.dp
                )
            )
            LoadingStateBox(
                12, 192.dp, modifier = Modifier.width(
                    241.dp
                )
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Loading Past 3 Days...", style = heading2, color = GrayStroke
            )
            LoadingStateBox(
                100, 15.dp, modifier = Modifier.width(
                    50.dp
                )
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.padding(start = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LoadingStateBox(
                12, 192.dp, modifier = Modifier.width(
                    241.dp
                )
            )
            LoadingStateBox(
                12, 192.dp, modifier = Modifier.width(
                    241.dp
                )
            )
        }
    }
}

@Composable
fun SubHighlightsLoadingScreen(
    header: String, modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        HighlightsSubScreenHeader(
            header = header, navigateBack = {})
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoadingStateBox(
                100, height = 40.dp
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
        Row(
            modifier = Modifier.padding(start = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            repeat(4) {
                LoadingStateBox(
                    100, 30.dp, modifier = Modifier.width(85.dp)
                )
            }
        }
        Spacer(modifier.height(24.dp))
        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            repeat(4) {
                LoadingStateBox(
                    12, 192.dp
                )
            }
        }
    }
}


@Preview
@Composable
private fun HighlightsLoadingScreenPreview() =
    _root_ide_package_.com.cornellappdev.score.components.ScorePreview {
        HighlightsLoadingScreen("Loading Highlights...")
    }

@Preview
@Composable
private fun SubHighlightsLoadingScreenPreview() =
    _root_ide_package_.com.cornellappdev.score.components.ScorePreview {
        SubHighlightsLoadingScreen("Past 3 Days")
    }