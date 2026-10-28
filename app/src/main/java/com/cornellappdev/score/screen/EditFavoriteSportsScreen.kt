package com.cornellappdev.score.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.R
import com.cornellappdev.score.components.NavigationHeader
import com.cornellappdev.score.model.GenderDivision
import com.cornellappdev.score.model.ProfileFavorite
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.theme.*

// Use the complete icon catalog, independent of sports with games in the current season.
private val favoriteSportOptions = Sport.entries.flatMap { sport ->
    // The profile design includes both golf divisions, unlike the schedule catalog.
    val divisions = if (sport.gender == GenderDivision.ALL || sport == Sport.GOLF) {
        listOf(GenderDivision.MALE, GenderDivision.FEMALE)
    } else listOf(sport.gender)
    divisions.map { ProfileFavorite(sport, it) }
}.sortedWith(compareBy({ it.sport.displayName }, { it.gender == GenderDivision.FEMALE }))

@Composable
fun EditFavoriteSportsScreen(
    favorites: List<ProfileFavorite>,
    onToggleFavorite: (ProfileFavorite) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var gender by rememberSaveable { mutableStateOf(GenderDivision.ALL) }
    val options = (favorites + favoriteSportOptions).distinct()
        .filter { gender == GenderDivision.ALL || it.gender == gender }
        .sortedWith(compareBy<ProfileFavorite> { it !in favorites }
            .thenBy { it.sport.displayName }.thenBy { it.gender == GenderDivision.FEMALE })

    Column(modifier.fillMaxSize().background(Color.White)) {
        NavigationHeader(stringResource(R.string.edit_favorite_sports), onBack)
        Row(
            Modifier.padding(horizontal = 24.dp, vertical = 32.dp)
                .fillMaxWidth().border(1.dp, GrayLight, CircleShape)
                .padding(6.dp).selectableGroup()
        ) {
            listOf(GenderDivision.ALL, GenderDivision.MALE, GenderDivision.FEMALE).forEach { division ->
                val selected = gender == division
                Box(
                    Modifier.weight(1f).clip(CircleShape)
                        .background(if (selected) CrimsonPrimary else Color.Transparent)
                        .selectable(selected, role = Role.Tab, onClick = { gender = division })
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(division.displayName,
                        style = if (selected) Style.bodySemibold else Style.bodyMedium,
                        color = if (selected) Color.White else GrayMedium)
                }
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(options, key = { "${it.sport.name}:${it.gender.name}" }) { favorite ->
                FavoriteSportOption(favorite, favorite in favorites) { onToggleFavorite(favorite) }
            }
        }
    }
}

@Composable
private fun FavoriteSportOption(favorite: ProfileFavorite, selected: Boolean, onClick: () -> Unit) {
    val shape = RoundedCornerShape(20.dp)
    Column(
        Modifier.fillMaxWidth()
            .semantics(mergeDescendants = true) {
                contentDescription = "${favorite.gender.displayName} ${favorite.sport.displayName}"
            }
            .toggleable(selected, role = Role.Checkbox, onValueChange = { onClick() }),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(Modifier.fillMaxWidth().aspectRatio(1f), contentAlignment = Alignment.Center) {
            Box(
                Modifier.fillMaxSize()
                    .background(if (selected) Color(0xFFF6FFF3) else GrayStroke, shape)
                    .border(if (selected) 3.dp else 1.dp, if (selected) saturatedGreen else GrayStroke, shape),
                contentAlignment = Alignment.Center
            ) {
                Icon(painterResource(favorite.sport.emptyIcon), null, Modifier.size(45.dp),
                    tint = if (selected) saturatedGreen else GrayPrimary)
            }
            if (selected) {
                Box(Modifier.align(Alignment.TopStart).offset(x = (-5).dp, y = (-5).dp)
                    .size(32.dp).background(CrimsonPrimary, CircleShape), contentAlignment = Alignment.Center) {
                    Icon(painterResource(R.drawable.ic_round_minus), null, Modifier.size(24.dp), tint = Color.White)
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(favorite.sport.displayName, style = Style.bodyMedium, color = GrayPrimary,
            textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(2.dp))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            Icon(painterResource(if (favorite.gender == GenderDivision.FEMALE) R.drawable.ic_gender_women else R.drawable.ic_gender_men),
                null, Modifier.size(18.dp), tint = Color.Unspecified)
            Text(if (favorite.gender == GenderDivision.FEMALE) "W" else "M", style = Style.labelsNormal, color = GrayPrimary)
        }
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 805)
@Composable
private fun EditFavoriteSportsScreenPreview() {
    EditFavoriteSportsScreen(sampleProfileData.favorites, {}, {})
}
