import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.cornellappdev.score.components.BoxScore
import com.cornellappdev.score.model.GameData
import com.cornellappdev.score.model.ScoreEvent
import com.cornellappdev.score.model.Team
import com.cornellappdev.score.model.TeamScore

@Composable
fun GameScoreSummaryScreen(
    gameData: GameData,
    scoreEvents: List<ScoreEvent>,
    team1Score: Int,
    team2Score: Int
) {
    Row(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//        GameScoreHeader(
//            team1 = gameData.teamScores.first.team,
//            team2 = gameData.teamScores.second.team,
//            team1Score = team1Score,
//            team2Score = team2Score
//        )


        // Box Score
        BoxScore(gameData = gameData)

        Spacer(modifier = Modifier.height(16.dp))

        // Scoring Summary
        Text(
            text = "Scoring Summary",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        ScoringSummary(scoreEvents = scoreEvents)
    }
}

//@Composable
//fun GameScoreHeader(team1: Team, team2: Team, team1Score: Int, team2Score: Int) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        TeamScoreColumn(team = team1, score = team1Score)
//        Text(
//            text = "-",
//            style = MaterialTheme.typography.headlineMedium,
//            color = Color.Gray
//        )
//        TeamScoreColumn(team = team2, score = team2Score)
//    }
//}

@Composable
fun TeamScoreColumn(team: Team, score: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = rememberAsyncImagePainter(team.logoUrl),
            contentDescription = team.name,
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            text = team.name,
            fontSize = 14.sp,
            color = Color.Black
        )
        Text(
            text = score.toString(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGameScoreSummaryScreen() {
    val team1 = Team(name = "Cornell", logoUrl = "https://example.com/cornell_logo.png")
    val team2 = Team(name = "Yale", logoUrl = "https://example.com/yale_logo.png")

    val teamScore1 = TeamScore(
        team = team1,
        scoresByPeriod = listOf(13, 14, 6, 14),
        totalScore = 47
    )
    val teamScore2 = TeamScore(
        team = team2,
        scoresByPeriod = listOf(7, 7, 9, 0),
        totalScore = 23
    )

    val gameData = GameData(teamScores = Pair(teamScore1, teamScore2))

    val scoreEvents = listOf(
        ScoreEvent(id = 1, time = "6:21", quarter = "1st", team = team1, score =  "10 - 7", eventType =  "Field"),
        ScoreEvent(2,time = "8:40", quarter = "1st", team = team2, score = "7 - 7", eventType =  "Touchdown"),
        ScoreEvent(3,time = "11:29", quarter = "1st",team= team1, score = "7 - 0",  eventType =   "Touchdown")
    )

    GameScoreSummaryScreen(
        gameData = gameData,
        scoreEvents = scoreEvents,
        team1Score = teamScore1.totalScore,
        team2Score = teamScore2.totalScore
    )
}

