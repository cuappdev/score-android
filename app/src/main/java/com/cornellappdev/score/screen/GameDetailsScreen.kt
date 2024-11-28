import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.R
import com.cornellappdev.score.model.ScoreEvent
import com.cornellappdev.score.model.Team
//TODO: Game Header, meta info
@Composable
fun GameDetailsScreen(scoreEvents: List<ScoreEvent>, onArrowClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Scoring Summary",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location Icon",
                modifier = Modifier
                    .clickable { onArrowClick() }
                    .padding(8.dp)
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(scoreEvents.size) { event ->
                ScoreEventItem(event = scoreEvents[event])
                Divider(color = Color.LightGray, thickness = 0.5.dp)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewGameDetailsScreen() {
    // Sample Team and ScoreEvent data
    val team1 = Team(name = "Cornell", logo = R.drawable.cornell_logo)
    val team2 = Team(name = "Yale", logo = R.drawable.yale_logo)

    val scoreEvents = listOf(
        ScoreEvent(
            id = 1,
            time = "6:21",
            quarter = "1st Quarter",
            team = team1,
            eventType = "Field Goal",
            score = "10 - 7",
            description = "Zhao, Alan field goal attempt from 24 GOOD"
        ),
        ScoreEvent(
            id = 2,
            time = "8:40",
            quarter = "1st Quarter",
            team = team2,
            eventType = "Touchdown",
            score = "7 - 7",
            description = "McCaughey, Brogan right pass complete to Yates, Ry for 8 yards to the COROO, TOUCHDOWN. (Conforti, Nick kick attempt good.)"
        ),
        ScoreEvent(
            id = 3,
            time = "11:29",
            quarter = "1st Quarter",
            team = team1,
            eventType = "Touchdown",
            score = "7 - 0",
            description = "Wang, Jameson left pass complete to Lee, Brendan for 34 yards to the YALOO, TOUCHDOWN. (Zhao, Alan kick attempt good.)"
        )
    )

    GameDetailsScreen(
        scoreEvents = scoreEvents,
        onArrowClick = {
            println("Arrow clicked to view more details")
        }
    )
}