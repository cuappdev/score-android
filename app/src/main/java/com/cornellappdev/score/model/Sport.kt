package com.cornellappdev.score.model

import androidx.annotation.DrawableRes
import com.cornellappdev.score.util.isValidSport
import com.cornellappdev.score.R

enum class Sport(
    val displayName: String,
    val gender: GenderDivision,
    @DrawableRes val emptyIcon: Int,
    @DrawableRes val filledIcon: Int
) {
    BASEBALL(
        displayName = "Baseball",
        gender = GenderDivision.MALE,
        emptyIcon = R.drawable.ic_baseball,
        filledIcon = R.drawable.ic_baseball_filled
    ),
    BASKETBALL(
        displayName = "Basketball",
        gender = GenderDivision.ALL,
        emptyIcon = R.drawable.ic_basketball,
        filledIcon = R.drawable.ic_basketball_filled
    ),
    CROSS_COUNTRY(
        displayName = "Cross Country",
        gender = GenderDivision.ALL,
        emptyIcon = R.drawable.ic_cross_country,
        filledIcon = R.drawable.ic_cross_country_filled
    ),
    EQUESTRIAN(
        displayName = "Equestrian",
        gender = GenderDivision.FEMALE,
        emptyIcon = R.drawable.ic_equestrian,
        filledIcon = R.drawable.ic_equestrian_filled
    ),
    FENCING(
        displayName = "Fencing",
        gender = GenderDivision.ALL,
        emptyIcon = R.drawable.ic_fencing,
        filledIcon = R.drawable.ic_fencing_filled
    ),
    FIELD_HOCKEY(
        displayName = "Field Hockey",
        gender = GenderDivision.FEMALE,
        emptyIcon = R.drawable.ic_field_hockey,
        filledIcon = R.drawable.ic_field_hockey_filled
    ),
    GYMNASTICS(
        displayName = "Gymnastics",
        gender = GenderDivision.FEMALE,
        emptyIcon = R.drawable.ic_gymnastics,
        filledIcon = R.drawable.ic_gymnastics_filled
    ),
    FOOTBALL(
        displayName = "Football",
        gender = GenderDivision.MALE,
        emptyIcon = R.drawable.ic_football,
        filledIcon = R.drawable.ic_football_filled
    ),
    GOLF(
        displayName = "Golf",
        gender = GenderDivision.MALE,
        emptyIcon = R.drawable.ic_golf,
        filledIcon = R.drawable.ic_golf_filled
    ),
    ICE_HOCKEY(
        displayName = "Ice Hockey",
        gender = GenderDivision.ALL,
        emptyIcon = R.drawable.ic_ice_hockey,
        filledIcon = R.drawable.ic_ice_hockey_filled
    ),

    //TODO: awaiting polo icon from design
//    POLO(
//        displayName = "Polo",
//        gender = GenderDivision.ALL,
//        emptyIcon = R.drawable.ic_polo,
//        filledIcon = R.drawable.ic_polo_filled
//    ),
    ROWING(
        displayName = "Rowing",
        gender = GenderDivision.FEMALE,
        emptyIcon = R.drawable.ic_rowing_lightweight,
        filledIcon = R.drawable.ic_rowing_lightweight_filled
    ),
    LACROSSE(
        displayName = "Lacrosse",
        gender = GenderDivision.ALL,
        emptyIcon = R.drawable.ic_lacrosse,
        filledIcon = R.drawable.ic_lacrosse_filled
    ),
    ROWING_HEAVYWEIGHT(
        displayName = "Heavyweight Rowing",
        gender = GenderDivision.MALE,
        emptyIcon = R.drawable.ic_rowing_heavyweight,
        filledIcon = R.drawable.ic_rowing_heavyweight_filled
    ),
    ROWING_LIGHTWEIGHT(
        displayName = "Lightweight Rowing",
        gender = GenderDivision.MALE,
        emptyIcon = R.drawable.ic_rowing_lightweight,
        filledIcon = R.drawable.ic_rowing_lightweight_filled
    ),
    SAILING(
        displayName = "Sailing",
        gender = GenderDivision.ALL,
        emptyIcon = R.drawable.ic_sailing,
        filledIcon = R.drawable.ic_sailing_filled
    ),
    SOCCER(
        displayName = "Soccer",
        gender = GenderDivision.ALL,
        emptyIcon = R.drawable.ic_soccer,
        filledIcon = R.drawable.ic_soccer_filled
    ),
    SOFTBALL(
        displayName = "Softball",
        gender = GenderDivision.FEMALE,
        emptyIcon = R.drawable.ic_softball,
        filledIcon = R.drawable.ic_softball_filled
    ),
    SPRINT_FOOTBALL(
        displayName = "Sprint Football",
        gender = GenderDivision.MALE,
        emptyIcon = R.drawable.ic_sprint_football,
        filledIcon = R.drawable.ic_sprint_football_filled
    ),
    SQUASH(
        displayName = "Squash",
        gender = GenderDivision.ALL,
        emptyIcon = R.drawable.ic_squash,
        filledIcon = R.drawable.ic_squash_filled
    ),
    SWIM_DIVE(
        displayName = "Swim Dive",
        gender = GenderDivision.ALL,
        emptyIcon = R.drawable.ic_swim_dive,
        filledIcon = R.drawable.ic_swim_dive_filled
    ),
    TENNIS(
        displayName = "Tennis",
        gender = GenderDivision.ALL,
        emptyIcon = R.drawable.ic_tennis,
        filledIcon = R.drawable.ic_tennis_filled
    ),
    TRACK_FIELD(
        displayName = "Track and Field",
        gender = GenderDivision.ALL,
        emptyIcon = R.drawable.ic_track_field,
        filledIcon = R.drawable.ic_track_field_filled
    ),
    VOLLEYBALL(
        displayName = "Volleyball",
        gender = GenderDivision.FEMALE,
        emptyIcon = R.drawable.ic_volleyball,
        filledIcon = R.drawable.ic_volleyball_filled
    ),
    WRESTLING(
        displayName = "Wrestling",
        gender = GenderDivision.MALE,
        emptyIcon = R.drawable.ic_wrestling,
        filledIcon = R.drawable.ic_wrestling_filled
    );

    companion object {
        fun fromDisplayName(name: String): Sport? {
            return entries.find { it.displayName.equals(name, ignoreCase = true) }
        }

        fun getSportSelectionList(selectedGender: GenderDivision?): List<SportSelection> {
            val filteredSports = when (selectedGender) {
                GenderDivision.MALE -> Sport.entries.filter { it.gender == GenderDivision.MALE || it.gender == GenderDivision.ALL }
                GenderDivision.FEMALE -> Sport.entries.filter { it.gender == GenderDivision.FEMALE || it.gender == GenderDivision.ALL }
                GenderDivision.ALL,
                null -> Sport.entries
            }.filter { isValidSport(it.displayName) }

            return listOf(SportSelection.All) + filteredSports.map { SportSelection.SportSelect(it) }
        }
    }
}

sealed class SportSelection {
    data object All : SportSelection()
    data class SportSelect(val sport: Sport) : SportSelection()
}

enum class GenderDivision(
    val displayName: String
) {
    FEMALE(
        "Women's"
    ),
    MALE(
        "Men's"
    ),
    ALL(
        "All"
    )
}
