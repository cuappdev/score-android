package com.cornellappdev.score.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.annotation.StringRes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.score.R
import com.cornellappdev.score.components.highlights.ArticleHighlightCard
import com.cornellappdev.score.components.highlights.VideoHighlightCard
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.model.ProfileData
import com.cornellappdev.score.model.ProfileFavorite
import com.cornellappdev.score.model.GenderDivision
import com.cornellappdev.score.model.HighlightData
import com.cornellappdev.score.theme.AmbientColor
import com.cornellappdev.score.theme.CrimsonPrimary
import com.cornellappdev.score.theme.GrayPrimary
import com.cornellappdev.score.theme.GrayStroke
import com.cornellappdev.score.theme.SpotColor
import com.cornellappdev.score.theme.Style

/** Standalone sample backed by replaceable dummy data; avatar editing is inherited from PR #115. */
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    data: ProfileData = sampleProfileData,
    onEditSports: () -> Unit = {},
    onShowAllBookmarks: () -> Unit = {}
) {
    var selectedPicture by rememberSaveable { mutableStateOf(ProfilePicture.TENNIS) }
    var editingPicture by rememberSaveable { mutableStateOf(false) }

    var showingSettings by rememberSaveable { mutableStateOf(false) }
    var editingSports by rememberSaveable { mutableStateOf(false) }
    var favorites by rememberSaveable(data.favorites, stateSaver = listSaver<List<ProfileFavorite>, String>(
        save = { list -> list.map { "${it.sport.name}:${it.gender.name}" } },
        restore = { list -> list.map {
            val (sport, gender) = it.split(":")
            ProfileFavorite(Sport.valueOf(sport), GenderDivision.valueOf(gender))
        } }
    )) { mutableStateOf(data.favorites) }

    BackHandler(enabled = editingPicture || editingSports || showingSettings) {
        editingPicture = false
        editingSports = false
        showingSettings = false
    }
    Column(modifier.fillMaxSize().background(Color.White)) {
        Box(Modifier.weight(1f)) {
            if (showingSettings) {
                SettingsScreen(onBack = { showingSettings = false })
            } else if (editingSports) {
                EditFavoriteSportsScreen(
                    favorites = favorites,
                    onToggleFavorite = { favorite ->
                        favorites = if (favorite in favorites) favorites - favorite else favorites + favorite
                    },
                    onBack = { editingSports = false }
                )
            } else if (editingPicture) {
                EditProfileScreen(selectedPicture, { selectedPicture = it }, { editingPicture = false })
            } else {
                ProfileContent(
                    data = data.copy(favorites = favorites),
                    selectedPicture = selectedPicture,
                    onEditProfilePicture = { editingPicture = true },
                    onEditSports = {
                        editingSports = true
                        onEditSports()
                    },
                    onShowAllBookmarks = onShowAllBookmarks,
                    onSettings = { showingSettings = true }
                )
            }
        }
        if (!editingSports && !showingSettings) ProfileTabBar()
    }
}

/** Content-only entry point for a host that owns data, navigation, bottom bar, and system insets. */
@Composable
fun ProfileContent(
    selectedPicture: ProfilePicture,
    data: ProfileData,
    onEditProfilePicture: () -> Unit,
    onEditSports: () -> Unit,
    onShowAllBookmarks: () -> Unit,
    modifier: Modifier = Modifier,
    onSettings: () -> Unit = {}
) {
    Column(modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(top = 24.dp, bottom = 32.dp)) {
        Column(Modifier.padding(horizontal = 24.dp)) {
            ProfileIdentityHeader(selectedPicture, onEditProfilePicture, compact = true, displayName = data.name, onSettings = onSettings)
            ProfileSectionDivider()
            ProfileActionHeading(R.string.profile_favorite_sports, R.string.profile_edit, onEditSports)
        }
        Spacer(Modifier.height(16.dp))
        LazyRow(contentPadding = PaddingValues(horizontal = 24.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(data.favorites, key = { "${it.sport.name}:${it.gender.name}" }) { favorite ->
                FavoriteSportTile(favorite)
            }
            if (data.favorites.isEmpty()) {
                item {
                    TextButton(onClick = onEditSports) { Text(stringResource(R.string.profile_add_sports), color = CrimsonPrimary) }
                }
            }
        }
        Column(Modifier.padding(horizontal = 24.dp)) {
            ProfileSectionDivider()
            ProfileActionHeading(R.string.profile_bookmarked_highlights, R.string.profile_show_all, onShowAllBookmarks)
        }
        Spacer(Modifier.height(16.dp))
        if (data.bookmarks.isEmpty()) {
            Text(stringResource(R.string.profile_no_bookmarks), style = Style.bodyNormal,
                modifier = Modifier.padding(horizontal = 24.dp), color = GrayPrimary)
        } else {
            LazyRow(contentPadding = PaddingValues(horizontal = 24.dp, vertical = 2.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(data.bookmarks) { ProfileBookmarkCard(it) }
            }
        }
    }
}

@Composable
private fun ProfileActionHeading(@StringRes title: Int, @StringRes action: Int, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(stringResource(title), style = Style.heading2, color = GrayPrimary, modifier = Modifier.weight(1f))
        Row(Modifier.heightIn(min = 27.dp).clickable(role = Role.Button, onClick = onClick), verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(action), style = Style.bodyMedium, color = CrimsonPrimary)
            Icon(painterResource(R.drawable.ic_right_chevron), null, Modifier.size(20.dp), tint = CrimsonPrimary)
        }
    }
}

@Composable
private fun FavoriteSportTile(favorite: ProfileFavorite) {
    Column(Modifier.width(90.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.size(90.dp).border(1.dp, GrayStroke, RoundedCornerShape(20.dp)), contentAlignment = Alignment.Center) {
            Icon(painterResource(favorite.sport.emptyIcon), null, Modifier.size(40.dp), tint = GrayPrimary)
        }
        Spacer(Modifier.height(8.dp))
        Text(favorite.sport.displayName, style = Style.bodyMedium, color = GrayPrimary)
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            Box(Modifier.size(18.dp).background(GrayStroke, CircleShape).clip(CircleShape)) {
                Image(painterResource(R.drawable.profile_favorite_gender), favorite.gender.displayName,
                    Modifier.requiredSize(47.dp, 56.dp).wrapContentSize(unbounded = true).offset(x = 1.dp, y = 5.dp))
            }
            Text(if (favorite.gender == GenderDivision.FEMALE) "W" else "M", style = Style.labelsNormal, color = GrayPrimary)
        }
    }
}

@Composable
private fun ProfileBookmarkCard(bookmark: HighlightData) {
    val shape = RoundedCornerShape(12.dp)
    Box(Modifier.width(241.dp).shadow(3.dp, shape, ambientColor = AmbientColor, spotColor = SpotColor)
        .clip(shape).border(1.dp, GrayStroke, shape)) {
        when (bookmark) {
            is HighlightData.Video -> VideoHighlightCard(bookmark.data, isWideFormat = false, linkTextStyle = Style.bodyMedium,
                headerOverlay = {
                    Box(Modifier.fillMaxSize().padding(10.dp)) {
                        bookmark.data.finalScore?.let { score ->
                            Text(score, style = Style.labelsMedium, color = GrayPrimary,
                                modifier = Modifier.align(Alignment.TopEnd).background(Color.White, RoundedCornerShape(3.dp)).padding(horizontal = 4.dp, vertical = 2.dp))
                        }
                        bookmark.data.duration?.let { duration ->
                            Row(Modifier.align(Alignment.BottomStart), verticalAlignment = Alignment.CenterVertically) {
                                Image(painterResource(R.drawable.profile_highlight_play), null, Modifier.size(9.dp, 8.dp))
                                Spacer(Modifier.width(3.dp))
                                Text(duration, style = Style.labelsMedium, color = Color.White)
                            }
                        }
                        bookmark.data.photoCredit?.let { credit ->
                            Text(credit, style = Style.labelsMedium.copy(fontSize = 10.sp),
                                color = Color.White, modifier = Modifier.align(Alignment.BottomEnd))
                        }
                    }
                })
            is HighlightData.Article -> ArticleHighlightCard(bookmark.data, isWideFormat = false, linkTextStyle = Style.bodySemibold, imageOverlayAlpha = 0.55f)
        }
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 758)
@Composable
private fun ProfileScreenPreview() {
    ProfileScreen()
}
