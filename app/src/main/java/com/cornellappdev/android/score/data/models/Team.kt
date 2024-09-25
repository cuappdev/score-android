package com.cornellappdev.android.score.data.models

data class Team(
    val sport: Sport,
    val genderDivision: GenderDivision
)
enum class Sport {
    BASEBALL,
    BASKETBALL,
    CROSSCOUNTRY,
    EQUESTRIAN,
    FENCING,
    FIELDHOCKEY,
    GYMNASTICS,
    FOOTBALL,
    GOLF,
    ICEHOCKEY,
    LACROSSE,
    POLO,
    ROWING,
    ROWINGHEAVYWEIGHT,
    ROWINGLIGHTWEIGHT,
    SAILING,
    SOCCER,
    SOFTBALL,
    SPRINTFOOTBALL,
    SQUASH,
    SWIMDIVE,
    TENNIS,
    TRACKFIELD,
    VOLLEYBALL,
    WRESTLING
}

enum class GenderDivision {
    FEMALE,
    MALE,
    MIXED
}