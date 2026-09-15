package com.cornellappdev.score.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.R
import com.cornellappdev.score.theme.CrimsonPrimary
import com.cornellappdev.score.theme.Style.bodyNormal
import com.cornellappdev.score.theme.Style.bodySemibold
import com.cornellappdev.score.theme.White
import com.cornellappdev.score.util.sampleIvyLeaderboardData

data class LeaderboardData(
    val rank: Int,
    val icon: Int,
    val school: String,
    val points: Double
)

@Composable
fun Leaderboard(
    leaderboardData: List<LeaderboardData>
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(topEnd = 8.dp, topStart = 8.dp))
                .background(color = CrimsonPrimary)
                .height(33.dp)
        ) {
            Text("Leaderboard", color = White, style = bodyNormal)
        }
        for (team in leaderboardData) {
            LeaderboardRow(
                rank = team.rank,
                icon = painterResource(team.icon),
                school = team.school,
                points = team.points
            )
        }
    }
}

@Composable
fun LeaderboardRow(
    rank: Int,
    icon: Painter,
    school: String,
    points: Double
) {
    val pointsFormatted = String.format("%.1f", points)
    val isCornell = (school == "Cornell")
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .border(width = Dp.Hairline, color = CrimsonPrimary)
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "$rank", style = if (isCornell) {bodySemibold} else {bodyNormal}, modifier = Modifier
                .padding(10.dp)
                .width(34.dp)
        )
        Spacer(modifier = Modifier.width(11.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.width(120.dp)
        ) {
            Image(
                painter = icon,
                contentDescription = "School icon",
                modifier = Modifier.size(30.dp)
            )
            Text(text = school, style = if (isCornell) {bodySemibold} else {bodyNormal}, modifier = Modifier.padding(10.dp))
        }
        Spacer(modifier = Modifier.width(11.dp))
        Text(
            text = "$pointsFormatted pts",
            style = if (isCornell) {bodySemibold} else {bodyNormal},
            modifier = Modifier
                .padding(10.dp)
                .width(88.dp)

        )
    }
}

@Preview
@Composable
private fun LeaderboardRowPreview() {
    LeaderboardRow(
        rank = 1,
        icon = painterResource(R.drawable.cornell_logo),
        school = "Cornell",
        points = 614.5
    )
}

@Preview
@Composable
private fun LeaderboardPreview() {
    Box(
        modifier = Modifier.background(White).padding(horizontal = 24.dp).fillMaxWidth()
    ){
        Leaderboard(sampleIvyLeaderboardData)
    }
}
