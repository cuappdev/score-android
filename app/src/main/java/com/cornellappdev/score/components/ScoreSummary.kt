import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.model.ScoreEvent
import com.cornellappdev.score.theme.Style.bodyNormal
import com.cornellappdev.score.theme.Style.heading5
import com.cornellappdev.score.util.scoreEvents1


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
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(event.team.logo),
            contentDescription = event.team.name,
            modifier = Modifier
                .size(40.dp)
                .padding(end = 12.dp)
        )

        Row(
            modifier = Modifier.weight(2f),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = event.time,
                style = bodyNormal,
                textAlign = TextAlign.Center
            )

            Text(
                text = event.quarter,
                style = bodyNormal,
                textAlign = TextAlign.Center
            )
        }


        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val (homeScore, awayScore) = event.score.split(" - ").map { it.toInt() }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = homeScore.toString(),
                    style = if (homeScore > awayScore) heading5.copy(fontWeight = FontWeight.Bold) else heading5,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = " - ",
                    style = heading5,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = awayScore.toString(),
                    style = if (awayScore >= homeScore) heading5.copy(fontWeight = FontWeight.Bold) else heading5,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewScoringSummary() {
    ScoringSummary(scoreEvents = scoreEvents1)
}
