import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.cornellappdev.score.util.scoreEvents2

@Composable
fun GameScoreSummaryScreenDetail(scoreEvents: List<ScoreEvent>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(scoreEvents.size) { event ->
            ScoreEventItemDetailed(event = scoreEvents[event])
            Divider(color = Color.LightGray, thickness = 0.5.dp)
        }
    }
}

@Composable
fun ScoreEventItemDetailed(event: ScoreEvent) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Image(
            painter = painterResource(event.team.logo),
            contentDescription = event.team.name,
            modifier = Modifier
                .size(40.dp)
                .padding(end = 8.dp),
        )

        Column(
            modifier = Modifier
                .weight(0.4f)
        ) {
            Text(
                text = "${event.time} - ${event.quarter.replace(" Quarter", "")}",
                style = bodyNormal,
                textAlign = TextAlign.Center
            )

            val (homeScore, awayScore) = event.score.split(" - ").map { it.toInt() }
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    text = homeScore.toString(),
                    style = if (homeScore > awayScore) bodyNormal.copy(fontWeight = FontWeight.Bold) else bodyNormal,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = " - ",
                    style = bodyNormal,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = awayScore.toString(),
                    style = if (awayScore >= homeScore) bodyNormal.copy(fontWeight = FontWeight.Bold) else bodyNormal,
                    textAlign = TextAlign.Center
                )
            }
        }

        Text(
            text = event.description ?: "",
            style = bodyNormal,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewScoringDetailsScreen() {
    GameScoreSummaryScreenDetail(scoreEvents = scoreEvents2)
}
