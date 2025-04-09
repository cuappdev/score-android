package com.cornellappdev.score.util

import androidx.compose.ui.graphics.Color

/**
 * Converts a hexcode String into Color object
 */
fun parseColor(color: String): Color {
    val colorInt = Integer.parseInt(color.removePrefix("#"), 16)
    return Color(colorInt)
}

/**
 * Interpolates between two colors based on a given fraction.
 */
fun interpolateColorHSV(startColor: Color, endColor: Color, fraction: Float): Color {
    // Clamp the fraction to be between 0 and 1
    val clampedFraction = fraction.coerceIn(0f, 1f)

    // Convert start and end colors to HSV
    val startHSV = FloatArray(3)
    val endHSV = FloatArray(3)
    android.graphics.Color.colorToHSV(
        android.graphics.Color.argb(
            (startColor.alpha * 255).toInt(),
            (startColor.red * 255).toInt(),
            (startColor.green * 255).toInt(),
            (startColor.blue * 255).toInt()
        ),
        startHSV
    )
    android.graphics.Color.colorToHSV(
        android.graphics.Color.argb(
            (endColor.alpha * 255).toInt(),
            (endColor.red * 255).toInt(),
            (endColor.green * 255).toInt(),
            (endColor.blue * 255).toInt()
        ),
        endHSV
    )

    // Interpolate HSV values
    val hue = startHSV[0] + (endHSV[0] - startHSV[0]) * clampedFraction
    val saturation = startHSV[1] + (endHSV[1] - startHSV[1]) * clampedFraction
    val value = startHSV[2] + (endHSV[2] - startHSV[2]) * clampedFraction

    // Convert back to RGB
    val rgb = android.graphics.Color.HSVToColor(floatArrayOf(hue, saturation, value))

    return Color(
        red = ((rgb shr 16) and 0xFF) / 255f,
        green = ((rgb shr 8) and 0xFF) / 255f,
        blue = (rgb and 0xFF) / 255f,
        alpha = ((rgb shr 24) and 0xFF) / 255f
    )
}