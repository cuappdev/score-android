package com.cornellappdev.score.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.components.ScorePreview
import com.cornellappdev.score.theme.CrimsonPrimary
import com.cornellappdev.score.theme.GrayStroke
import com.cornellappdev.score.theme.Style
import com.cornellappdev.score.theme.White

enum class OnboardingState{
    PROFILE_SELECT, SPORT_SELECT
}

@Composable
fun OnboardingFooter(
    buttonText: String,
    onClick: () -> Unit,
    activeStep: OnboardingState
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(
                start = 24.dp,
                end = 24.dp,
                top = 24.dp,
                bottom = 60.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = CrimsonPrimary,
                contentColor = White
            )
        ) {
            Text(buttonText, style = Style.heading3, color = White)
        }

        Spacer(Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(2) { index ->
                val isActive = when (activeStep) {
                    OnboardingState.PROFILE_SELECT -> index == 0
                    OnboardingState.SPORT_SELECT -> index == 1
                }
                Box(
                    Modifier
                        .size(8.dp)
                        .background(if (isActive) CrimsonPrimary else GrayStroke, CircleShape)
                )
            }
        }
    }
}

@Preview
@Composable
private fun OnboardingFooterPreview() {
    ScorePreview {
        OnboardingFooter(
            buttonText = "Next",
            onClick = {},
            activeStep = OnboardingState.PROFILE_SELECT
        )
    }
}
