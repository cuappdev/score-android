package com.cornellappdev.score.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.R
import com.cornellappdev.score.components.ButtonPrimary
import com.cornellappdev.score.components.GameScoreHeader
import com.cornellappdev.score.components.NavigationHeader
import com.cornellappdev.score.components.TimeUntilStartCard
import com.cornellappdev.score.theme.GrayMedium
import com.cornellappdev.score.theme.GrayPrimary
import com.cornellappdev.score.theme.Style.bodyNormal
import com.cornellappdev.score.theme.Style.heading1
import com.cornellappdev.score.theme.Style.heading3
import com.cornellappdev.score.theme.White

@Composable
fun GameDetailsScreen(gameId: String = "", onBackArrow: () -> Unit = {}) {
    Column(modifier = Modifier.background(White).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        // TODO: add navigation
        NavigationHeader(title = "Game Details", onBackArrow)
        GameScoreHeader(
            leftTeamLogo = painterResource(R.drawable.cornell_logo),
            rightTeamLogo = painterResource(R.drawable.penn_logo),
            gradientColor1 = Color(0xFFE1A69F),
            gradientColor2 = Color(0xFF011F5B),
            modifier = Modifier.height(185.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = "Men's Football",
                style = heading3.copy(color = GrayPrimary)
            )
            Text(
                text = "Cornell vs. Yale",
                style = heading1.copy(color = GrayPrimary)
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = "Location Icon",
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp),
                    colorFilter = ColorFilter.tint(GrayMedium)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Ithaca (Schoellkopf)", style = bodyNormal.copy(color = GrayPrimary))
                Spacer(modifier = Modifier.width(12.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = "Location Icon",
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp),
                    colorFilter = ColorFilter.tint(GrayMedium)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "9/28/2024, 2:00PM", style = bodyNormal.copy(color = GrayPrimary))
            }

            //render the below if the game is in the future
            Spacer(modifier = Modifier.height(40.dp))
            TimeUntilStartCard(2, 0)

        }
        Spacer(modifier = Modifier.height(84.dp))
        ButtonPrimary("Add to Calendar", painterResource(R.drawable.ic_calendar))
    }
}

@Preview
@Composable
fun GameDetailsScreenPreview() {
    GameDetailsScreen()
// import androidx.compose.ui.tooling.preview.Preview
// import androidx.compose.ui.unit.dp
// import com.cornellappdev.score.R
// import com.cornellappdev.score.model.ScoreEvent
// import com.cornellappdev.score.model.Team
// //TODO: Game Header, meta info
// @Composable
// fun GameDetailsScreen(scoreEvents: List<ScoreEvent>, onArrowClick: () -> Unit) {
//     Column(
//         modifier = Modifier
//             .fillMaxWidth()
//             .padding(16.dp)
//     ) {
//         Row(
//             modifier = Modifier
//                 .fillMaxWidth()
//                 .padding(bottom = 8.dp),
//             horizontalArrangement = Arrangement.SpaceBetween,
//             verticalAlignment = Alignment.CenterVertically
//         ) {
//             Text(
//                 text = "Scoring Summary",
//                 style = MaterialTheme.typography.titleMedium,
//                 modifier = Modifier.weight(1f)
//             )

//             Icon(
//                 imageVector = Icons.Default.LocationOn,
//                 contentDescription = "Location Icon",
//                 modifier = Modifier
//                     .clickable { onArrowClick() }
//                     .padding(8.dp)
//             )
//         }

//         LazyColumn(
//             modifier = Modifier.fillMaxWidth()
//         ) {
//             items(scoreEvents.size) { event ->
//                 ScoreEventItem(event = scoreEvents[event])
//                 Divider(color = Color.LightGray, thickness = 0.5.dp)
//             }
//         }
//     }
// }


// @Preview(showBackground = true)
// @Composable
// fun PreviewGameDetailsScreen() {
//     // Sample Team and ScoreEvent data
//     val team1 = Team(name = "Cornell", logo = R.drawable.cornell_logo)
//     val team2 = Team(name = "Yale", logo = R.drawable.yale_logo)

//     val scoreEvents = listOf(
//         ScoreEvent(
//             id = 1,
//             time = "6:21",
//             quarter = "1st Quarter",
//             team = team1,
//             eventType = "Field Goal",
//             score = "10 - 7",
//             description = "Zhao, Alan field goal attempt from 24 GOOD"
//         ),
//         ScoreEvent(
//             id = 2,
//             time = "8:40",
//             quarter = "1st Quarter",
//             team = team2,
//             eventType = "Touchdown",
//             score = "7 - 7",
//             description = "McCaughey, Brogan right pass complete to Yates, Ry for 8 yards to the COROO, TOUCHDOWN. (Conforti, Nick kick attempt good.)"
//         ),
//         ScoreEvent(
//             id = 3,
//             time = "11:29",
//             quarter = "1st Quarter",
//             team = team1,
//             eventType = "Touchdown",
//             score = "7 - 0",
//             description = "Wang, Jameson left pass complete to Lee, Brendan for 34 yards to the YALOO, TOUCHDOWN. (Zhao, Alan kick attempt good.)"
//         )
//     )

//     GameDetailsScreen(
//         scoreEvents = scoreEvents,
//         onArrowClick = {
//             println("Arrow clicked to view more details")
//         }
//     )
}