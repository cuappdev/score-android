package com.cornellappdev.score.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.components.ScorePreview
import com.cornellappdev.score.theme.saturatedGreen

/**
 * Shared profile picture choices used by profile editing and first-time onboarding.
 */
@Composable
fun ProfilePictureOptions(
    selectedPicture: ProfilePicture,
    onPictureSelected: (ProfilePicture) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .selectableGroup(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ProfilePicture.entries.forEach { picture ->
            val selected = picture == selectedPicture
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .selectable(
                        selected = selected,
                        role = Role.RadioButton,
                        onClick = { onPictureSelected(picture) }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(picture.drawable),
                    contentDescription = stringResource(picture.label),
                    modifier = Modifier.fillMaxSize()
                )
                if (selected) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(Color.White.copy(alpha = 0.6f), CircleShape)
                            .border(3.dp, saturatedGreen, CircleShape)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfilePictureOptionsPreview() {
    ScorePreview(backgroundColor = Color.White) {
        ProfilePictureOptions(
            selectedPicture = ProfilePicture.TENNIS,
            onPictureSelected = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}
