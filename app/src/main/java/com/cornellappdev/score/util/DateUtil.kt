package com.cornellappdev.score.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter;

/**
 * Converts date of form String "month-abbr day (day-of-week)" (for example, "Apr 29 (Tue)") to a LocalDate object
 * Returns null when parsing [strDate] fails
 */
fun parseDate(strDate: String): LocalDate? {
    val subDate = strDate.substringBefore(" (")
    val formatter = DateTimeFormatter.ofPattern("MMM d")

    return try {
        LocalDate.parse("$subDate ${LocalDate.now().year}", formatter) //assumes current year
    } catch (e: Exception) {
        null
    }
}

/**
 * Converts from format "#xxxxxx" to a valid hex, with alpha = 40. Ready to be passed into Color()
 */
fun formatColor(color: String): Int {
    val alpha = (40 * 255 / 100)// Convert percent to hex (0-255)
    val colorInt = Integer.parseInt(color.removePrefix("#"), 16)
    return (alpha shl 24) or colorInt
}

/**
 *  Takes in a LocalDate? object and returns it as a String of format "MM/DD/YYY"
 */
fun dateToString(date: LocalDate?): String {
    if (date == null) {
        return "--"
    }
    return "${date.month.value}/${date.dayOfMonth}/${date.year}"
}
