package com.cornellappdev.score.util

import com.cornellappdev.score.R
import com.cornellappdev.score.model.GameCardData
import com.cornellappdev.score.model.Sport

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
    Sport.BASEBALL,
    Sport.BASKETBALL,
    Sport.CROSS_COUNTRY,
)
