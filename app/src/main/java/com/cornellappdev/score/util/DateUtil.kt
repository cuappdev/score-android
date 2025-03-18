package com.cornellappdev.score.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter;

/**
 * Converts date of form String "month-abbr day (day-of-week)" (for example, "Apr 29 (Tue)") to a LocalDate object
 * Returns null when parsing [strDate] fails
 */
fun parseDate(strDate: String): LocalDate? {
    val subDate = strDate.substringBefore(" (")
    val formatter = DateTimeFormatter.ofPattern("MMM d yyyy")

    return try {
        LocalDate.parse("$subDate ${LocalDate.now().year}", formatter) //assumes current year
    } catch (e: Exception) {
        null
    }
}

/**
 * DateTimeFormatter of pattern "M/d/yyyy"
 */
val outputFormatter = DateTimeFormatter.ofPattern("M/d/yyyy")

