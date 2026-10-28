package com.cornellappdev.score.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.cornellappdev.score.theme.GrayPrimary
import com.cornellappdev.score.theme.Style

@Composable
fun OnboardingProfileSelectScreen(
    onNext: (ProfilePicture) -> Unit,
    modifier: Modifier = Modifier,
    displayName: String? = null
) {
    var selectedPicture by rememberSaveable { mutableStateOf(ProfilePicture.TENNIS) }
    val name = displayName ?: stringResource(R.string.onboarding_default_name)

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val designScale = minOf(maxWidth.value / 393f, maxHeight.value / 852f)
        val designDp: (Float) -> Dp = { (it * designScale).dp }

        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = designDp(79f)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.edit_profile),
                    style = Style.heading1,
                    color = GrayPrimary
                )
                Spacer(Modifier.height(designDp(4f)))
                Text(
                    text = stringResource(R.string.onboarding_profile_subtitle),
                    style = Style.bodyMedium,
                    color = GrayPrimary
                )

                Spacer(Modifier.height(designDp(47f)))
                Image(
                    painter = painterResource(selectedPicture.drawable),
                    contentDescription = stringResource(
                        R.string.selected_profile_picture,
                        stringResource(selectedPicture.label)
                    ),
                    modifier = Modifier.size(designDp(100f))
                )
                Spacer(Modifier.height(designDp(16f)))
                Text(
                    text = name,
                    style = Style.bodyMedium,
                    color = Color.Black
                )

                Spacer(Modifier.height(designDp(60f)))
                Text(
                    text = stringResource(R.string.onboarding_select_profile_picture),
                    style = Style.metricMedium,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
                Spacer(Modifier.height(designDp(24f)))
                ProfilePictureOptions(
                    selectedPicture = selectedPicture,
                    onPictureSelected = { selectedPicture = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.weight(1f))
            OnboardingFooter(
                buttonText = stringResource(R.string.next),
                onClick = { onNext(selectedPicture) },
                activeStep = OnboardingState.PROFILE_SELECT
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun OnboardingProfileSelectScreenPreview() {
    OnboardingProfileSelectScreen(onNext = {})
}
