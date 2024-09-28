package com.cornellappdev.score.components
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment
import com.example.score.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.score.theme.Style
import com.cornellappdev.score.theme.Style.dateText
import com.cornellappdev.score.theme.Style.teamName
import com.cornellappdev.score.theme.Style.universityText

@Composable
fun SportCard(
    modifier: Modifier = Modifier,  // Add modifier argument
    teamLogo: Painter,
    team: String,
    date: String,
    location: String,
    sportName: String,
    sportIcon: Painter
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = teamLogo,
                        contentDescription = "Team Logo",
                        modifier = Modifier
                            .height(20.dp)
                            .padding(start= 4.dp, end = 4.dp)
                    )
                    Text(
                        text = team,
                        style = teamName
                    )
                }
                Text(
                    text = date,
                    style = dateText,
                )
            }

            // Second Row: Location, Sport Name, Sport Icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_location), // Location icon
                        contentDescription = "Location Icon",
                        modifier = Modifier
                            .padding(1.dp)
                            .width(24.dp)
                            .height(24.dp)
                    )
                    Text(
                        text = location,
                        style = universityText,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = sportName,
                        style = Style.sportName,
                        modifier= Modifier.padding(end = 4.dp)
                    )
                    Image(
                        painter = sportIcon,
                        contentDescription = "Sport Icon",
                        modifier = Modifier
                            .padding(1.dp)
                            .width(24.dp)
                            .height(24.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun SportCardPreview() {
    Column {
        SportCard(
            modifier = Modifier.padding(16.dp),
            teamLogo = painterResource(id = R.drawable.penn_logo),
            team = "Penn",
            date = "5/20/2024",
            location = "U. Pennsylvania",
            sportName = "Baseball",
            sportIcon = painterResource(id = R.drawable.ic_baseball)
        )
    }
}