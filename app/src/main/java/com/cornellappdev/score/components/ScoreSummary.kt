import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.score.model.ScoreEvent
import com.cornellappdev.score.model.Team
import kotlin.math.log

@Composable
fun ScoringSummary(scoreEvents: List<ScoreEvent>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(scoreEvents) { event ->
            ScoreEventItem(event)
            Divider(color = Color.LightGray, thickness = 0.5.dp)
        }
    }
}

@Composable
fun ScoreEventItem(event: ScoreEvent) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Team Logo
        Image(
            painter = painterResource(event.team.logoUrl),
            contentDescription = event.team.name,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Time and Quarter
        Text(
            text = "${event.time} ${event.quarter}",
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )

        // Event Type
        Text(
            text = event.eventType,
            fontSize = 14.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(end = 16.dp)
        )

        // Score
        Text(
            text = event.score,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewScoringSummary() {
    // Sample Team data with drawable resource IDs (replace with actual resource IDs)
    val team1 = Team(name = "Cornell", R.drawable.cornell_logo)
    val team2 = Team(name = "Yale",yale_logo)

    val scoreEvents = listOf(
        ScoreEvent(
            id = 1,
            time = "6:21",
            quarter = "1st Quarter",
            team = team1,
            eventType = "Field Goal",
            score = "10 - 7"
        ),
        ScoreEvent(
            id = 2,
            time = "8:40",
            quarter = "1st Quarter",
            team = team2,
            eventType = "Touchdown",
            score = "7 - 7"
        ),
        ScoreEvent(
            id = 3,
            time = "11:29",
            quarter = "1st Quarter",
            team = team1,
            eventType = "Touchdown",
            score = "7 - 0"
        )
    )

    ScoringSummary(scoreEvents = scoreEvents)
}
