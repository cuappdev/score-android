package com.cornellappdev.score.util

import android.util.Log
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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

/**
 * Parses a date and time string into a [LocalDateTime] object.
 *
 * Expected input format:
 * - [date]: String of form "MMM d (EEE)". For example, "Apr 5 (Sat)"
 * - [time]: String of form "h:mm a" with or without periods. For example, "3:45 PM" or "3:45 p.m."
 *
 * The current year is assumed in the final parsed result.
 * returns [LocalDateTime] object if parsing succeeds, or [null] if the format is invalid.
 */
fun parseDateTimeOrNull(date: String, time: String): LocalDateTime? {
    val subDate = date.substringBefore(" (") + date.substringAfter(")")
    val cleanedTime = time
        .replace(".", "")
        .trim()
        .uppercase()

    val dateTimeString = "$subDate $cleanedTime"
    Log.d("parseDateTimeOrNull", "parseDateTimeOrNull: ${dateTimeString}")
    val formatter = DateTimeFormatter.ofPattern("MMM d yyyy h:mm a", Locale.ENGLISH)

    return try {
        LocalDateTime.parse(dateTimeString, formatter)
    } catch (e: Exception) {
        null
    }
}


/**
 * Formats a date and time string into a user-friendly display string.
 *
 * Combines [date] and [time] inputs and attempts to parse them using [parseDateTimeOrNull].
 * If parsing succeeds, returns a string in the format "MMM d, h:mma" (For example, "Apr 5, 3:45PM").
 * If parsing fails, returns an empty string.
 */
fun formatDateTimeDisplay(date: String, time: String): String {
    val dateTime = parseDateTimeOrNull(date, time)
    val formatter = DateTimeFormatter.ofPattern("MMM d, h:mma", Locale.ENGLISH)

    return dateTime?.format(formatter) ?: ""
}

fun getTimeUntilStart(date: String, time: String): Pair<Int, Int>? {
    val gameStart = parseDateTimeOrNull(date, time) ?: return null
    val now = LocalDateTime.now()

    if (gameStart.isBefore(now)) return null

    val duration = Duration.between(now, gameStart)
    val days = duration.toDays().toInt()
    val hours = duration.minusDays(days.toLong()).toHours().toInt()
    return Pair(days, hours)
}

