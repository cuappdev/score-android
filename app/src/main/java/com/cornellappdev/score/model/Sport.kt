package com.cornellappdev.score.model

import androidx.annotation.DrawableRes
import com.cornellappdev.score.R

enum class Sport(
    val displayName: String,
    @DrawableRes val emptyIcon: Int,
    @DrawableRes val filledIcon: Int
) {
    BASEBALL(
        displayName = "Baseball",
        emptyIcon = R.drawable.ic_baseball,
        filledIcon = R.drawable.ic_baseball_filled,
    ),
    BASKETBALL(
        displayName = "Basketball",
        emptyIcon = R.drawable.ic_basketball,
        filledIcon = R.drawable.ic_basketball_filled
    ),
    CROSS_COUNTRY(
        displayName = "Cross Country",
        emptyIcon = R.drawable.ic_cross_country,
        filledIcon = R.drawable.ic_cross_country_filled
    );
    // TODO: Fill in rest
//    EQUESTRIAN,
//    FENCING,
//    FIELD_HOCKEY,
//    GYMNASTICS,
//    FOOTBALL,
//    GOLF,
//    ICE_HOCKEY,
//    LACROSSE,
//    POLO,
//    ROWING,
//    ROWING_HEAVYWEIGHT,
//    ROWING_LIGHTWEIGHT,
//    SAILING,
//    SOCCER,
//    SOFTBALL,
//    SPRINT_FOOTBALL,
//    SQUASH,
//    SWIM_DIVE,
//    TENNIS,
//    TRACK_FIELD,
//    VOLLEYBALL,
//    WRESTLING

    companion object {
        fun fromDisplayName(name: String): Sport? {
            return entries.find { it.displayName.equals(name, ignoreCase = true) }
        }
    }
}

sealed class SportSelection {
    data object All: SportSelection()
    data class SportSelect(val sport: Sport): SportSelection()
}

enum class GenderDivision(
    val displayName: String
) {
    FEMALE(
        "Womens"
    ),
    MALE(
        "Mens"
    ),
    ALL(
        "All"
    )
}
