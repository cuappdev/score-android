package com.cornellappdev.score.util

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import com.cornellappdev.score.model.DetailsCardData
import java.time.ZoneId

fun addToCalendar(context: Context, details: DetailsCardData) {
    val startDateTime = details.date?.atTime(
        details.time.split(":").getOrNull(0)?.toIntOrNull() ?: 0,
        details.time.split(":").getOrNull(1)?.toIntOrNull() ?: 0
    ) ?: return

    val startMillis = startDateTime
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

    val endMillis = startMillis + (2 * 60 * 60 * 1000)

    val intent = Intent(Intent.ACTION_INSERT).apply {
        data = CalendarContract.Events.CONTENT_URI
        putExtra(CalendarContract.Events.TITLE, "Cornell vs. ${details.opponent}")
        putExtra(CalendarContract.Events.EVENT_LOCATION, details.locationString)
        putExtra(CalendarContract.Events.DESCRIPTION, "${details.sport} game (${details.gender})")
        putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
        putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
    }

    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}
