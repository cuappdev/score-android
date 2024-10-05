package com.cornellappdev.android.score.data.models

import androidx.compose.ui.graphics.painter.Painter

data class GameCardData(
    val teamLogo: Int,
    val team: String,
    val date: String,
    val location: String,
    val genderIcon: Int,
    val sportIcon: Int,
)