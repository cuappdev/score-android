package com.cornellappdev.score.screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.score.R
import com.cornellappdev.score.components.NavigationHeader
import com.cornellappdev.score.theme.CrimsonPrimary
import com.cornellappdev.score.theme.GrayMedium
import com.cornellappdev.score.theme.GrayPrimary
import com.cornellappdev.score.theme.GrayStroke
import com.cornellappdev.score.theme.Style

@Composable
fun SettingsScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().background(Color.White)) {
        NavigationHeader(stringResource(R.string.profile_settings), onBack)
        Column(
            Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(24.dp, 32.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            SettingsSection(stringResource(R.string.settings_preferences)) {
                SettingsRow(stringResource(R.string.profile_favorite_sports), R.drawable.settings_star)
                HorizontalDivider(color = GrayStroke)
                SettingsRow(stringResource(R.string.profile_notifications), R.drawable.settings_notifications, iconSize = 28.dp)
            }
            SettingsSection(stringResource(R.string.settings_help)) {
                SettingsRow(stringResource(R.string.settings_about), R.drawable.settings_info, iconSize = 33.dp)
                HorizontalDivider(color = GrayStroke)
                SettingsRow(stringResource(R.string.settings_report), R.drawable.settings_flag)
                HorizontalDivider(color = GrayStroke)
                SettingsRow(stringResource(R.string.settings_logout), R.drawable.settings_logout, destructive = true)
                HorizontalDivider(color = GrayStroke)
                SettingsRow(stringResource(R.string.settings_delete), R.drawable.settings_delete, destructive = true)
            }
        }
    }
}

@Composable
private fun SettingsSection(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(title, style = Style.bodyMedium, color = GrayPrimary)
        Column(
            Modifier.fillMaxWidth().border(1.dp, GrayStroke, RoundedCornerShape(16.dp))
                .padding(start = 16.dp, end = 20.dp),
            content = { content() }
        )
    }
}

@Composable
private fun SettingsRow(
    title: String,
    @DrawableRes icon: Int,
    iconSize: Dp = 30.dp,
    destructive: Boolean = false
) {
    // Placeholder actions intentionally do nothing until the settings flows are implemented.
    Row(
        Modifier.fillMaxWidth().clickable(role = Role.Button, onClick = {}).padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painterResource(icon), null, Modifier.size(iconSize), tint = Color.Unspecified)
        Spacer(Modifier.width(10.dp))
        Text(title, style = Style.bodyMedium.copy(fontSize = 16.sp),
            color = if (destructive) CrimsonPrimary else GrayPrimary, modifier = Modifier.weight(1f))
        if (!destructive) {
            Icon(painterResource(R.drawable.ic_right_chevron), null, Modifier.size(24.dp), tint = GrayMedium)
        }
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 805)
@Composable
private fun SettingsScreenPreview() {
    SettingsScreen(onBack = {})
}
