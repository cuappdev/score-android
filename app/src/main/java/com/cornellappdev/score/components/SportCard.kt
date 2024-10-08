package com.cornellappdev.score.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.theme.Style.dateText
import com.cornellappdev.score.theme.Style.teamName
import com.cornellappdev.score.theme.Style.universityText
import com.example.score.R

@Composable
fun SportCard(
    teamLogo: Painter,
    team: String,
    date: String,
    location: String,
    genderIcon: Painter,
    sportIcon: Painter,
    topCornerRound: Boolean,
    modifier: Modifier = Modifier
) {
    val cardShape = if (topCornerRound) {
        RoundedCornerShape(16.dp) // Rounded all
    } else {
        RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp) // square top corners
    }
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
            .clip(cardShape)
            .then(
                if (topCornerRound) {
                    Modifier.border(.4.dp, Color.Gray, cardShape)
                } else {
                    Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .then(
                            Modifier.border(
                                border = BorderStroke(.4.dp, Color.Gray),
                                shape = RoundedCornerShape(
                                    bottomStart = 16.dp,
                                    bottomEnd = 16.dp
                                )
                            )
                        )
                }
            )
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
                            .padding(start = 4.dp, end = 4.dp)
                    )

                    Text(
                        text = team,
                        style = teamName
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = sportIcon,
                        contentDescription = "Sport Icon",
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp)
                    )
                    Image(
                        painter = genderIcon,
                        contentDescription = "Gender Icon",
                        modifier = Modifier
                            .padding(2.5.dp)
                            .width(19.dp)
                            .height(19.dp)
                    )
                }
            }

            // Second Row: Location, date
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_location),
                        contentDescription = "Location Icon",
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp)
                    )

                    Text(
                        text = location,
                        style = universityText
                    )
                }
                Text(
                    text = date,
                    style = dateText,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SportCardPreview() {
    Column {
        SportCard(
            teamLogo = painterResource(id = R.drawable.penn_logo),
            team = "Penn",
            date = "5/20/2024",
            location = "U. Pennsylvania",
            genderIcon = painterResource(id = R.drawable.ic_gender_men),
            sportIcon = painterResource(id = R.drawable.ic_baseball),
            topCornerRound = false,
            modifier = Modifier.padding(16.dp)
        )
    }
}