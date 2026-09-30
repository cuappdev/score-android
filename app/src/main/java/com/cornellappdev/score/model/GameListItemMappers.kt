package com.cornellappdev.score.model

import com.cornellappdev.score.util.parseColor
import com.cornellappdev.score.util.parseResultScore
import com.example.score.fragment.GameListItem

fun GameListItem.isMens(): Boolean = gender == "Mens"

fun GameListItem.toGame(): Game? {
    val gameTeam = team ?: return null
    val imageUrl = gameTeam.image ?: return null
    val scores = result?.split(",")?.getOrNull(1)?.split("-")
    val fallbackScores = parseResultScore(result)
    return Game(
        id = id ?: "",
        teamLogo = imageUrl,
        time = time,
        teamName = gameTeam.name,
        teamColor = parseColor(gameTeam.color).copy(alpha = 0.4f * 255),
        gender = if (isMens()) "Men's" else "Women's",
        sport = sport,
        date = date,
        city = city,
        cornellScore = scores?.getOrNull(0)?.toNumberOrNull() ?: fallbackScores?.first,
        otherScore = scores?.getOrNull(1)?.toNumberOrNull() ?: fallbackScores?.second
    )
}

private fun String.toNumberOrNull(): Number? =
    if (contains(".")) toFloatOrNull() else toIntOrNull()
