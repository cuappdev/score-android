package com.cornellappdev.score.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
                    color = Color(0xFF333333)
                )
                Spacer(Modifier.height(designDp(4f)))
                Text(
                    text = stringResource(R.string.onboarding_profile_subtitle),
                    style = Style.bodyMedium,
                    color = Color(0xFF333333)
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
            OnboardingProfileFooter(
                onNext = { onNext(selectedPicture) },
                designDp = designDp
            )
        }
    }
}

@Composable
private fun OnboardingProfileFooter(
    onNext: () -> Unit,
    designDp: (Float) -> Dp
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 24.dp,
                end = 24.dp,
                top = designDp(24f),
                bottom = designDp(60f)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(designDp(48f)),
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

        Spacer(Modifier.height(designDp(16f)))
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
        Box(Modifier.size(8.dp).background(Color(0xFFEAEAEA), CircleShape))
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun OnboardingProfileSelectScreenPreview() {
    OnboardingProfileSelectScreen(onNext = {})
}
