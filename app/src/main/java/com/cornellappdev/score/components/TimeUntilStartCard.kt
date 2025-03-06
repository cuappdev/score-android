package com.cornellappdev.score.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.R
import com.cornellappdev.score.theme.GrayLight
import com.cornellappdev.score.theme.GrayPrimary
import com.cornellappdev.score.theme.Style.bodyNormal
import com.cornellappdev.score.theme.Style.countdownNumberText
import com.cornellappdev.score.theme.Style.heading2
import com.cornellappdev.score.theme.White

@Composable
fun TimeUntilStartCard(days: Int, hours: Int) {
    Column(modifier = Modifier.background(White).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.ic_countdown),
            contentDescription = "Countdown Icon",
            modifier = Modifier
                .width(150.dp)
                .height(150.dp),
            colorFilter = ColorFilter.tint(GrayLight)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Time until start",
            style = heading2.copy(color = GrayPrimary)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = days.toString(),
                style = countdownNumberText
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "days",
                style = bodyNormal,
                color = GrayPrimary
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = hours.toString(),
                style = countdownNumberText
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "hours",
                style = bodyNormal,
                color = GrayPrimary
            )
        }
    }
}

@Preview
@Composable
private fun TimeUntilStartCardPreview() {
    TimeUntilStartCard(2, 0)
}