import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.model.ScoreEvent
import com.cornellappdev.score.theme.Style.bodyNormal
import com.cornellappdev.score.theme.Style.spanBodyNormal
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
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(event.team.logo),
            contentDescription = event.team.name,
            modifier = Modifier
                .size(40.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .weight(0.3f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val (homeScoreStyle, awayScoreStyle) = if (event.homeScore.toInt() >= event.awayScore.toInt()) {
                spanBodyNormal.copy(fontWeight = FontWeight.Bold) to spanBodyNormal
            } else {
                spanBodyNormal to spanBodyNormal.copy(fontWeight = FontWeight.Bold)
            }

            Text(
                textAlign = TextAlign.Center,
                text =
                buildAnnotatedString {
                    withStyle(
                        style = spanBodyNormal
                    ) {
                        append("${event.time} - ${event.quarter.replace(" Quarter", "")}\n")
                    }
                    withStyle(
                        style = homeScoreStyle
                    ) {
                        append(event.homeScore)
                    }
                    withStyle(style = spanBodyNormal) {
                        append(" - ")
                    }
                    withStyle(style = awayScoreStyle) {
                        append(event.awayScore)
                    }
                }
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
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
