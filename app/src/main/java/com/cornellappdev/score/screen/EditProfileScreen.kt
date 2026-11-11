package com.cornellappdev.score.screen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.R
import com.cornellappdev.score.components.NavigationHeader
import com.cornellappdev.score.theme.Style

enum class ProfilePicture(@DrawableRes val drawable: Int, @StringRes val label: Int) {
    BLANK(R.drawable.profile_blank, R.string.profile_blank),
    TENNIS(R.drawable.profile_tennis, R.string.profile_tennis),
    BASEBALL(R.drawable.profile_baseball, R.string.profile_baseball),
    SOCCER(R.drawable.profile_soccer, R.string.profile_soccer)
}

@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedPicture by rememberSaveable { mutableStateOf(ProfilePicture.BASEBALL) }
    EditProfileScreen(
        selectedPicture = selectedPicture,
        onPictureSelected = { selectedPicture = it },
        onBack = onBack,
        modifier = modifier
    )
}

@Composable
fun EditProfileScreen(
    selectedPicture: ProfilePicture,
    onPictureSelected: (ProfilePicture) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxSize().background(Color.White)) {
        NavigationHeader(stringResource(R.string.edit_profile), onBack)
        Column(
            modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())
                .padding(top = 60.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(selectedPicture.drawable),
                contentDescription = stringResource(R.string.selected_profile_picture, stringResource(selectedPicture.label)),
                modifier = Modifier.size(100.dp)
            )
            Spacer(Modifier.height(40.dp))
            Text(
                text = stringResource(R.string.select_profile_picture),
                style = Style.metricMedium.copy(fontFeatureSettings = "liga off, clig off"),
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Spacer(Modifier.height(24.dp))
            ProfilePictureOptions(
                selectedPicture = selectedPicture,
                onPictureSelected = onPictureSelected
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun EditProfileScreenPreview() {
    EditProfileScreen(onBack = {})
}
