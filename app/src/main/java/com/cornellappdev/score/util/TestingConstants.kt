package com.cornellappdev.score.util

import androidx.compose.ui.res.painterResource
import com.cornellappdev.score.model.GameCardData
import com.cornellappdev.score.R
import com.cornellappdev.score.model.SportOption

val PENN_GAME = GameCardData(
    teamLogo = R.drawable.penn_logo,
    team = "Penn",
    date = "5/20/2024",
    location = "Philadelphia, PA",
    genderIcon = R.drawable.ic_gender_men,
    sportIcon = R.drawable.ic_baseball
)

val PRINCETON_GAME = GameCardData(
    teamLogo = R.drawable.princeton_logo,
    team = "Princeton",
    date = "5/21/2024",
    location = "Princeton, NY",
    genderIcon = R.drawable.ic_gender_men,
    sportIcon = R.drawable.ic_baseball
)
val gameList = listOf(PENN_GAME, PRINCETON_GAME)

val sportList = listOf(
    SportOption(
        "All",
        painterResource(id = R.drawable.ic_all),
        painterResource(id = R.drawable.ic_all_filled)
    ),
    SportOption(
        "Baseball",
        painterResource(id = R.drawable.ic_baseball),
        painterResource(id = R.drawable.ic_baseball_filled)
    ),
    SportOption(
        "Basketball",
        painterResource(id = R.drawable.ic_basketball),
        painterResource(id = R.drawable.ic_basketball_filled)
    ),
    SportOption(
        "Cross Country",
        painterResource(id = R.drawable.ic_cross_country),
        painterResource(id = R.drawable.ic_cross_country_filled)
    ),
    SportOption(
        "Football",
        painterResource(id = R.drawable.ic_football),
        painterResource(id = R.drawable.ic_football_filled)
    )
)
