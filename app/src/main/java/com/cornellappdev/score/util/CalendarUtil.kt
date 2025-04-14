package com.cornellappdev.score.util

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import com.cornellappdev.score.model.DetailsCardData
import java.time.ZoneId

data class CalendarEvent(
    val title: String,
    val description: String?,
    val location: String?,
    val date: java.time.LocalDate,
    val time: String
)

fun addToCalendar(context: Context, event: CalendarEvent) {
    val startDateTime = event.date.atTime(
        event.time.split(":").getOrNull(0)?.toIntOrNull() ?: 0,
        event.time.split(":").getOrNull(1)?.toIntOrNull() ?: 0
    )

    val startMillis = startDateTime
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

    val endMillis = startMillis + (2 * 60 * 60 * 1000) // Default 2-hour duration

    val intent = Intent(Intent.ACTION_INSERT).apply {
        data = CalendarContract.Events.CONTENT_URI
        putExtra(CalendarContract.Events.TITLE, event.title)
        putExtra(CalendarContract.Events.EVENT_LOCATION, event.location)
        putExtra(CalendarContract.Events.DESCRIPTION, event.description)
        putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
        putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
    }

    context.startActivity(intent)
}

fun DetailsCardData.toCalendarEvent(): CalendarEvent? {
    val date = this.date ?: return null

    return CalendarEvent(
        title = "Cornell vs. ${this.opponent}",
        description = "${this.sport} game (${this.gender})",
        location = this.locationString,
        date = date,
        time = this.time
    )
}