package com.cornellappdev.score.model

import com.cornellappdev.score.util.parseColor
import com.example.score.GameByIdQuery

fun GameByIdQuery.Game.toGameDetails(): GameDetailsGame {
    return GameDetailsGame(
        id = this.id ?: "",
        city = this.city,
        date = this.date,
        gender = this.gender,
        location = this.location,
        opponentId = this.opponentId,
        result = this.result,
        sport = this.sport,
        state = this.state,
        time = this.time,
        scoreBreakdown = this.scoreBreakdown,
        team = this.team?.toGameDetailsTeam(),
        boxScore = this.boxScore?.mapNotNull { it?.toGameDetailsBoxScore() }
    )
}
fun GameByIdQuery.Team.toGameDetailsTeam(): GameDetailsTeam {
    return GameDetailsTeam(
        id = this.id,
        color = parseColor(this.color).copy(alpha = 0.4f*255),
        image = this.image,
        name = this.name
    )
}

fun GameByIdQuery.BoxScore.toGameDetailsBoxScore(): GameDetailsBoxScore {
    return GameDetailsBoxScore(
        team = this.team,
        period = this.period,
        time = this.time,
        description = this.description,
        scorer = this.scorer,
        assist = this.assist,
        scoreBy = this.scoreBy,
        corScore = this.corScore,
        oppScore = this.oppScore
    )
}

