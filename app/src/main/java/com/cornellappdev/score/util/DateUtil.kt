package com.cornellappdev.score.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter;
import java.util.Locale

/**
 * Converts date of form String "month-abbr day (day-of-week)" (for example, "Apr 29 (Tue)") to a LocalDate object
 * Returns null when parsing [strDate] fails
 */
fun parseDateOrNull(strDate: String): LocalDate? {
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

fun parseDateTimeOrNull(strDate: String, strTime: String): LocalDateTime? {
    val subDate = strDate.substringBefore(" (")
    val cleanedTime = strTime
        .replace(".", "")
        .trim()
        .uppercase()

    val dateTimeString = "$subDate ${LocalDateTime.now().year} $cleanedTime"

    val formatter = DateTimeFormatter.ofPattern("MMM d yyyy h:mm a", Locale.ENGLISH)

    return try {
        LocalDateTime.parse(dateTimeString, formatter)
    } catch (e: Exception) {
        null
    }
}


fun formatDateTimeDisplay(strDate: String, strTime: String): String {
    val dateTime = parseDateTimeOrNull(strDate, strTime)
    val formatter = DateTimeFormatter.ofPattern("MMM d, h:mma", Locale.ENGLISH)

    return dateTime?.format(formatter) ?: ""
}


