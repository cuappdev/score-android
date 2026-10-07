package com.cornellappdev.score.screen

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.R
import com.cornellappdev.score.theme.CrimsonPrimary
import com.cornellappdev.score.theme.GrayLight
import com.cornellappdev.score.theme.GrayPrimary
import com.cornellappdev.score.theme.GrayStroke
import com.cornellappdev.score.theme.Style

/** Standalone profile flow with local, recreation-safe avatar selection; no backend or nav host. */
@Composable
fun EmptyProfileScreen(modifier: Modifier = Modifier) {
    var selectedPicture by rememberSaveable { mutableStateOf(ProfilePicture.BLANK) }
    var editingPicture by rememberSaveable { mutableStateOf(false) }

    BackHandler(enabled = editingPicture) { editingPicture = false }
    Column(modifier
        .fillMaxSize()
        .background(Color.White)) {
        Box(Modifier.weight(1f)) {
            if (editingPicture) {
                EditProfileScreen(
                    selectedPicture = selectedPicture,
                    onPictureSelected = { selectedPicture = it },
                    onBack = { editingPicture = false }
                )
            } else {
                EmptyProfileContent(
                    selectedPicture = selectedPicture,
                    onEditProfilePicture = { editingPicture = true }
                )
            }
        }
        ProfileTabBar()
    }
}

/** Content-only variant for embedding in a host that already supplies a bottom bar and insets. */
@Composable
fun EmptyProfileContent(
    selectedPicture: ProfilePicture,
    onEditProfilePicture: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = 8.dp, bottom = 32.dp)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                stringResource(R.string.profile_title), style = Style.heading1,
                color = Color.Black, modifier = Modifier.weight(1f)
            )
            Icon(
                painterResource(R.drawable.profile_notifications),
                stringResource(R.string.profile_notifications),
                Modifier.size(32.dp),
                tint = Color.Unspecified
            )
            Spacer(Modifier.width(8.dp))
            Icon(
                painterResource(R.drawable.profile_settings),
                stringResource(R.string.profile_settings),
                Modifier.size(32.dp),
                tint = Color.Unspecified
            )
        }
        Spacer(Modifier.height(24.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painterResource(selectedPicture.drawable),
                stringResource(
                    R.string.selected_profile_picture,
                    stringResource(selectedPicture.label)
                ),
                Modifier.size(70.dp)
            )
            Column(Modifier.weight(1f)) {
                Text(
                    stringResource(R.string.profile_placeholder_name),
                    style = Style.heading2,
                    color = GrayPrimary
                )
                Box(
                    Modifier
                        .heightIn(min = 48.dp)
                        .clickable(role = Role.Button, onClick = onEditProfilePicture),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        stringResource(R.string.edit_profile_picture),
                        style = Style.labelsNormal,
                        color = CrimsonPrimary
                    )
                }
            }
        }
        ProfileSectionDivider()
        ProfileSectionHeading(R.string.profile_favorite_sports, R.string.profile_edit, Color.Black)
        Spacer(Modifier.height(16.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                Modifier
                    .shadow(
                        4.dp, RoundedCornerShape(20.dp), ambientColor = Color(0x12000000),
                        spotColor = Color(0x12000000)
                    )
                    .background(Color.White, RoundedCornerShape(20.dp))
                    .border(1.dp, GrayStroke, RoundedCornerShape(20.dp))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painterResource(R.drawable.profile_add),
                    null,
                    Modifier.size(40.dp),
                    tint = Color.Unspecified
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                stringResource(R.string.profile_add_sports),
                style = Style.bodyMedium,
                color = Color.Black
            )
        }
        ProfileSectionDivider()
        ProfileSectionHeading(
            R.string.profile_bookmarked_highlights,
            R.string.profile_show_all,
            GrayPrimary
        )
        Spacer(Modifier.height(24.dp))
        Column(
            Modifier
                .fillMaxWidth()
                .heightIn(min = 176.dp)
                .border(1.dp, GrayStroke, RoundedCornerShape(12.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                Modifier
                    .size(44.dp)
                    .background(GrayStroke, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painterResource(R.drawable.profile_bookmark),
                    null,
                    Modifier.size(24.dp),
                    tint = Color.Unspecified
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                stringResource(R.string.profile_no_bookmarks),
                style = Style.bodyNormal,
                color = GrayPrimary
            )
        }
    }
}

@Composable
private fun ProfileSectionDivider() {
    Column {
        Spacer(Modifier.height(24.dp))
        HorizontalDivider(color = GrayStroke)
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun ProfileSectionHeading(@StringRes title: Int, @StringRes action: Int, color: Color) {
    Row(
        Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            stringResource(title),
            style = Style.heading2,
            color = color,
            modifier = Modifier.weight(1f)
        )
        Text(stringResource(action), style = Style.bodyMedium, color = GrayLight)
        Icon(
            painterResource(R.drawable.ic_right_chevron),
            null,
            Modifier.size(16.dp),
            tint = GrayLight
        )
    }
}

// Static navigation artwork: the reference tabs are intentionally non-interactive.
@Composable
private fun ProfileTabBar() {
    Row(
        Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            .background(Color.White)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileTab(R.drawable.ic_schedule, R.string.profile_schedule, Modifier.weight(1f))
        ProfileTab(R.drawable.ic_scores, R.string.profile_scores, Modifier.weight(1f))
        ProfileTab(R.drawable.ic_nav_star, R.string.profile_highlights, Modifier.weight(1f))
        ProfileTab(
            R.drawable.profile_person,
            R.string.profile_title,
            Modifier.weight(1f),
            CrimsonPrimary
        )
    }
}

@Composable
private fun ProfileTab(
    @DrawableRes icon: Int,
    @StringRes label: Int,
    modifier: Modifier = Modifier,
    color: Color = GrayPrimary
) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(painterResource(icon), null, Modifier.size(24.dp), tint = color)
        Spacer(Modifier.height(4.dp))
        Text(stringResource(label), style = Style.bodyMedium, color = color)
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 776)
@Composable
private fun EmptyProfileScreenPreview() {
    EmptyProfileScreen()
}
