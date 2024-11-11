package com.cornellappdev.score.model

import androidx.compose.ui.graphics.painter.Painter

data class SportOption(
    val name: String,
    val emptyIcon: Painter,
    val filledIcon: Painter
)