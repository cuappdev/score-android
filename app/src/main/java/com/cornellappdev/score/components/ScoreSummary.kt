import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.cornellappdev.score.R
import com.cornellappdev.score.components.ScorePreview
import com.cornellappdev.score.model.ScoreEvent
import com.cornellappdev.score.theme.GrayPrimary
import com.cornellappdev.score.theme.Style.bodyMedium
import com.cornellappdev.score.theme.Style.bodyNormal
import com.cornellappdev.score.theme.Style.metricNormal
import com.cornellappdev.score.theme.Style.metricSemibold
import com.cornellappdev.score.util.scoreEvents1


@Composable
fun ScoringSummary(scoreEvents: List<ScoreEvent>, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        scoreEvents.take(3).map {
            ScoreEventItem(it)
            HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
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
        if (event.team.name == "COR") { // TODO: Check if its "COR" for all queries. It is for baseball
            Image(
                painter = painterResource(R.drawable.cornell_logo),
                contentDescription = event.team.name,
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 12.dp)
            )
        } else {
            AsyncImage(
                model = event.team.logo,
                contentDescription = event.team.name, // Turn this into a if statement if i know the link for cornell logo
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 12.dp)
            )
        }


        Row(
            modifier = Modifier.weight(2f),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = event.time,
                style = bodyNormal,
                color = GrayPrimary,
                textAlign = TextAlign.Center
            )

            Text(
                text = event.quarter,
                style = bodyNormal,
                color = GrayPrimary,
                textAlign = TextAlign.Center
            )
        }


        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = event.eventType,
                style = bodyMedium,
                color = GrayPrimary,
                textAlign = TextAlign.Center
            )
            val (homeScore, awayScore) = event.score.split(" - ").map { it.toInt() }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = homeScore.toString(),
                    style = if (event.team.name == "Cornell") metricSemibold else metricNormal, // TODO: Check name
                    color = GrayPrimary,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = " - ",
                    style = bodyNormal,
                    color = GrayPrimary,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = awayScore.toString(),
                    style = if (event.team.name != "Cornell") metricSemibold else metricNormal,
                    color = GrayPrimary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewScoringSummary() = ScorePreview {
    ScoringSummary(scoreEvents = scoreEvents1)
}
