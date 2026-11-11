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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.R
import com.cornellappdev.score.components.OnboardingFooter
import com.cornellappdev.score.components.ScorePreview
import com.cornellappdev.score.model.GenderDivision
import com.cornellappdev.score.model.ProfileFavorite
import com.cornellappdev.score.theme.GrayPrimary
import com.cornellappdev.score.theme.Style
import com.cornellappdev.score.theme.White

typealias FavoriteSport = ProfileFavorite

@Composable
fun OnboardingFavoriteSportsScreen(
    favorites: List<FavoriteSport>,
    onToggleFavorite: (FavoriteSport) -> Unit,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    var gender by rememberSaveable { mutableStateOf(GenderDivision.ALL) }
    val options = (favorites + favoriteSportOptions).distinct()
        .filter { gender == GenderDivision.ALL || it.gender == gender }
        .sortedWith(selectedFavoritesFirst(favorites))

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
                favorites = favorites,
                options = options,
                gender = gender,
                onGenderSelected = { gender = it },
                onToggleFavorite = onToggleFavorite,
                modifier = Modifier.weight(1f)
            )

            OnboardingFooter(
                buttonText = when (favorites.size) {
                    0 -> stringResource(R.string.finish)
                    1 -> stringResource(R.string.finish_with_one_sport)
                    else -> stringResource(R.string.finish_with_sports, favorites.size)
                },
                onClick = onFinish,
                activeStep = 1
            )
        }
    }
}

@Preview
@Composable
private fun OnboardingFavoriteSportsScreenPreview() {
    ScorePreview {
        OnboardingFavoriteSportsScreen(
            favorites = emptyList(),
            onToggleFavorite = {},
            onFinish = {}
        )
    }
}

@Preview
@Composable
private fun OnboardingFavoriteSportsSelectedScreenPreview() {
    ScorePreview {
        OnboardingFavoriteSportsScreen(
            favorites = emptyList(),
            onToggleFavorite = {},
            onFinish = {}
        )
    }
}
