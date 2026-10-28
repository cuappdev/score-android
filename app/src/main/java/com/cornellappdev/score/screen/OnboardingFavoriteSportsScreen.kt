package com.cornellappdev.score.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.R
import com.cornellappdev.score.components.ScorePreview
import com.cornellappdev.score.model.GenderDivision
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.theme.CrimsonPrimary
import com.cornellappdev.score.theme.GrayLight
import com.cornellappdev.score.theme.GrayMedium
import com.cornellappdev.score.theme.GrayPrimary
import com.cornellappdev.score.theme.GrayStroke
import com.cornellappdev.score.theme.GreenWash
import com.cornellappdev.score.theme.Style
import com.cornellappdev.score.theme.White
import com.cornellappdev.score.theme.saturatedGreen

data class FavoriteSport(
    val sport: Sport,
    val gender: GenderDivision
) {
    val storageKey: String get() = "${sport.name}:${gender.name}"

    companion object {
        fun fromStorageKey(key: String): FavoriteSport? {
            val parts = key.split(':', limit = 2)
            if (parts.size != 2) return null
            val sport = runCatching { Sport.valueOf(parts[0]) }.getOrNull() ?: return null
            val gender = runCatching { GenderDivision.valueOf(parts[1]) }.getOrNull()
                ?: return null
            if (gender == GenderDivision.ALL) return null
            return FavoriteSport(sport, gender)
        }
    }
}

@Composable
fun OnboardingFavoriteSportsScreen(
    onFinish: (Set<FavoriteSport>) -> Unit,
    modifier: Modifier = Modifier,
    initiallySelectedKeys: String = ""
) {
    var selectedGender by rememberSaveable { mutableStateOf(GenderDivision.ALL) }
    var selectedKeys by rememberSaveable {
        mutableStateOf(initiallySelectedKeys)
    }
    val selectedSports = selectedKeys.split('|').filter(String::isNotBlank).toSet()
    val allOptions = rememberFavoriteSportOptions()
    val visibleOptions = when (selectedGender) {
        GenderDivision.ALL -> allOptions
        else -> allOptions.filter { it.gender == selectedGender }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 79.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.select_favorite_sports),
                    style = Style.heading1,
                    color = GrayPrimary,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.favorite_sports_subtitle),
                    style = Style.bodyMedium,
                    color = GrayPrimary,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(32.dp))
                FavoriteSportsGenderFilter(
                    selectedGender = selectedGender,
                    onGenderSelected = { selectedGender = it }
                )
            }

            Spacer(Modifier.height(32.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(
                    start = 24.dp,
                    end = 24.dp,
                    top = 8.dp,
                    bottom = 8.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                items(visibleOptions, key = { it.storageKey }) { option ->
                    FavoriteSportCard(
                        option = option,
                        selected = option.storageKey in selectedSports,
                        onClick = {
                            selectedKeys = if (option.storageKey in selectedSports) {
                                selectedSports - option.storageKey
                            } else {
                                selectedSports + option.storageKey
                            }.joinToString("|")
                        }
                    )
                }
            }

            OnboardingFooter(
                buttonText = when (selectedSports.size) {
                    0 -> stringResource(R.string.finish)
                    1 -> stringResource(R.string.finish_with_one_sport)
                    else -> stringResource(R.string.finish_with_sports, selectedSports.size)
                },
                onClick = {
                    onFinish(selectedSports.mapNotNull(FavoriteSport::fromStorageKey).toSet())
                },
                activeStep = OnboardingState.SPORT_SELECT
            )
        }
    }
}

@Composable
private fun rememberFavoriteSportOptions(): List<FavoriteSport> {
    return remember {
        Sport.entries
            .flatMap { sport ->
                when (sport.gender) {
                    GenderDivision.ALL -> listOf(
                        FavoriteSport(sport, GenderDivision.MALE),
                        FavoriteSport(sport, GenderDivision.FEMALE)
                    )

                    GenderDivision.MALE -> listOf(FavoriteSport(sport, GenderDivision.MALE))
                    GenderDivision.FEMALE -> listOf(FavoriteSport(sport, GenderDivision.FEMALE))
                }
            }
            .sortedWith(
                compareBy<FavoriteSport> { it.sport.displayName }
                    .thenBy { if (it.gender == GenderDivision.MALE) 0 else 1 }
            )
    }
}

@Composable
private fun FavoriteSportsGenderFilter(
    selectedGender: GenderDivision,
    onGenderSelected: (GenderDivision) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(48.dp)
            .border(
                BorderStroke(1.dp, GrayLight),
                CircleShape
            )
            .padding(5.dp)
            .selectableGroup(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf(
            GenderDivision.ALL to R.string.all,
            GenderDivision.MALE to R.string.onboarding_mens,
            GenderDivision.FEMALE to R.string.onboarding_womens
        ).forEach { (gender, label) ->
            val selected = gender == selectedGender
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(if (selected) CrimsonPrimary else Color.Transparent)
                    .selectable(
                        selected = selected,
                        role = Role.RadioButton,
                        onClick = { onGenderSelected(gender) }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(label),
                    style = if (selected) Style.bodySemibold else Style.bodyMedium,
                    color = if (selected) White else GrayMedium
                )
            }
        }
    }
}

@Composable
private fun FavoriteSportCard(
    option: FavoriteSport,
    selected: Boolean,
    onClick: () -> Unit
) {
    val cardShape = RoundedCornerShape(20.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.Checkbox,
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(cardShape)
                    .background(if (selected) GreenWash else GrayStroke)
                    .border(
                        width = if (selected) 3.dp else 1.dp,
                        color = if (selected) saturatedGreen else GrayStroke,
                        shape = cardShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(option.sport.emptyIcon),
                    contentDescription = null,
                    modifier = Modifier.size(45.dp),
                    colorFilter = ColorFilter.tint(
                        if (selected) saturatedGreen else GrayPrimary
                    )
                )
            }

            if (selected) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = (-8).dp, y = (-8).dp)
                        .size(32.dp)
                        .background(CrimsonPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_round_minus),
                        contentDescription = stringResource(R.string.remove_favorite_sport),
                        modifier = Modifier.size(24.dp),
                        colorFilter = ColorFilter.tint(White)
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))
        Text(
            text = option.sport.displayName,
            style = Style.bodyMedium,
            color = GrayPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(2.dp))
        FavoriteSportGenderTag(option.gender)
    }
}

@Composable
private fun FavoriteSportGenderTag(
    gender: GenderDivision,
) {
    val label = if (gender == GenderDivision.FEMALE) "W" else "M"
    val genderIcon = if (gender == GenderDivision.FEMALE) {
        R.drawable.ic_light_gray_female_gender
    } else {
        R.drawable.ic_light_gray_male_gender
    }

    Row(
        modifier = Modifier
            .background(White, RoundedCornerShape(100))
            .padding(horizontal = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .background(GrayStroke, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(genderIcon),
                contentDescription = null,
                modifier = Modifier.size(12.dp)
            )
        }
        Text(
            text = label,
            style = Style.labelsNormal,
            color = GrayPrimary
        )
    }
}


@Preview
@Composable
private fun OnboardingFavoriteSportsScreenPreview() {
    ScorePreview {
        OnboardingFavoriteSportsScreen(onFinish = {})
    }
}

@Preview
@Composable
private fun OnboardingFavoriteSportsSelectedScreenPreview() {
    ScorePreview {
        OnboardingFavoriteSportsScreen(
            onFinish = {},
            initiallySelectedKeys = "BASEBALL:MALE|CROSS_COUNTRY:FEMALE"
        )
    }
}
