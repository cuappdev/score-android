package com.cornellappdev.score.util

import androidx.compose.ui.graphics.Color

/**
 * Converts a hexcode String into Color object
 */
fun parseColor(color: String): Color {
    val colorInt = Integer.parseInt(color.removePrefix("#"), 16)
    return Color(colorInt)
}