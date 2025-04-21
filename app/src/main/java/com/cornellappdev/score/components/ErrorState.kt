package com.cornellappdev.score.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.R
import com.cornellappdev.score.theme.CrimsonPrimary
import com.cornellappdev.score.theme.GrayMedium
import com.cornellappdev.score.theme.GrayPrimary
import com.cornellappdev.score.theme.Style.bodyNormal
import com.cornellappdev.score.theme.Style.heading2

@Composable
fun ErrorState(
    onRefresh: () -> Unit,
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .defaultMinSize(minHeight = 740.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .padding(bottom = 270.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(100.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_feedback),
                    contentDescription = "feedback bubble"
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = message,
                    style = heading2.copy(color = GrayPrimary)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Please try again later.",
                    style = bodyNormal.copy(color = GrayMedium)
                )
            }

            Button(
                colors = ButtonDefaults.buttonColors(containerColor = CrimsonPrimary),
                onClick = onRefresh
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_cached),
                        contentDescription = "refresh icon"
                    )
                    Text("Try again")
                }
            }
        }
    }
}


@Preview
@Composable
private fun ErrorStatePreview() = ScorePreview {
    ErrorState({}, "Oops! Failed to load.")
}