package com.cornellappdev.score.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.R
import com.cornellappdev.score.components.ScorePreview
import com.cornellappdev.score.model.GenderDivision
import com.cornellappdev.score.model.ProfileFavorite
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.theme.GrayPrimary
import com.cornellappdev.score.theme.Style
import com.cornellappdev.score.theme.White
import com.cornellappdev.score.util.isValidSport

typealias FavoriteSport = ProfileFavorite

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
    val selectedFavorites = selectedKeys.split('|')
        .filter(String::isNotBlank)
        .mapNotNull { FavoriteSport.fromStorageKey(it) }

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
            }

            FavoriteSportSelectionScreenContents(
                favorites = selectedFavorites,
                options = visibleOptions,
                gender = selectedGender,
                onGenderSelected = { selectedGender = it },
                onToggleFavorite = { favorite ->
                    val key = favorite.storageKey
                    val selectedKeysList = selectedKeys.split('|').filter(String::isNotBlank)
                    val newKeysList = if (key in selectedKeysList) {
                        selectedKeysList - key
                    } else {
                        selectedKeysList + key
                    }
                    selectedKeys = newKeysList.joinToString("|")
                },
                modifier = Modifier.weight(1f)
            )

            OnboardingFooter(
                buttonText = when (selectedFavorites.size) {
                    0 -> stringResource(R.string.finish)
                    1 -> stringResource(R.string.finish_with_one_sport)
                    else -> stringResource(R.string.finish_with_sports, selectedFavorites.size)
                },
                onClick = {
                    onFinish(selectedFavorites.toSet())
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
            .filter { sport -> isValidSport(sport.displayName) }
            .flatMap { sport ->
                when (sport.gender) {
                    GenderDivision.ALL -> listOf(
                        ProfileFavorite(sport, GenderDivision.MALE),
                        ProfileFavorite(sport, GenderDivision.FEMALE)
                    )

                    GenderDivision.MALE -> listOf(ProfileFavorite(sport, GenderDivision.MALE))
                    GenderDivision.FEMALE -> listOf(ProfileFavorite(sport, GenderDivision.FEMALE))
                }
            }
            .sortedWith(
                compareBy<FavoriteSport> { it.sport.displayName }
                    .thenBy { if (it.gender == GenderDivision.MALE) 0 else 1 }
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
