package com.cornellappdev.score.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.R
import com.cornellappdev.score.theme.CrimsonPrimary
import com.cornellappdev.score.theme.GrayPrimary
import com.cornellappdev.score.theme.GrayStroke
import com.cornellappdev.score.theme.GrayStroke
import com.cornellappdev.score.theme.Style

@Composable
fun OnboardingProfileSelectScreen(
    onNext: (ProfilePicture) -> Unit,
    modifier: Modifier = Modifier,
    displayName: String? = null
) {
    var selectedPicture by rememberSaveable { mutableStateOf(ProfilePicture.BLANK) }
    val name = displayName ?: stringResource(R.string.onboarding_default_name)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.edit_profile),
                    style = Style.heading1,
                    color = GrayPrimary
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.onboarding_profile_subtitle),
                    style = Style.bodyMedium,
                    color = GrayPrimary
                )

                Spacer(Modifier.height(40.dp))
                Image(
                    painter = painterResource(selectedPicture.drawable),
                    contentDescription = stringResource(
                        R.string.selected_profile_picture,
                        stringResource(selectedPicture.label)
                    ),
                    modifier = Modifier.size(100.dp)
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = name,
                    style = Style.bodyMedium,
                    color = Color.Black
                )

                Spacer(Modifier.height(60.dp))
                Text(
                    text = stringResource(R.string.onboarding_select_profile_picture),
                    style = Style.metricMedium,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
                Spacer(Modifier.height(24.dp))
                ProfilePictureOptions(
                    selectedPicture = selectedPicture,
                    onPictureSelected = { selectedPicture = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.weight(1f))
            OnboardingProfileFooter(
                onNext = { onNext(selectedPicture) }
            )
        }
    }
}

@Composable
private fun OnboardingProfileFooter(
    onNext: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 24.dp,
                end = 24.dp,
                top = 24.dp,
                bottom = 60.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = CrimsonPrimary,
                contentColor = Color.White
            )
        ) {
            Text(
                text = stringResource(R.string.next),
                style = Style.heading3,
                color = Color.White
            )
        }

        Spacer(Modifier.height(16.dp))
        RowProgressDots()
    }
}

@Composable
private fun RowProgressDots() {
    androidx.compose.foundation.layout.Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(8.dp).background(CrimsonPrimary, CircleShape))
        Box(Modifier.size(8.dp).background(GrayStroke, CircleShape))
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingProfileSelectScreenPreview() {
    OnboardingProfileSelectScreen(onNext = {})
}
