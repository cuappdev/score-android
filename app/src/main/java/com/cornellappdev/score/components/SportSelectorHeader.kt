package com.cornellappdev.score.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.score.R
import com.cornellappdev.score.theme.CrimsonPrimary
import com.cornellappdev.score.theme.GrayLight
import com.cornellappdev.score.theme.GrayMedium
import com.cornellappdev.score.theme.Style.genderFilterText
import com.cornellappdev.score.theme.Style.genderText
import com.cornellappdev.score.theme.Style.sportFilterText
import com.cornellappdev.score.theme.White

@Preview
@Composable
fun PreviewSportSelectorHeader() {
    val sports = listOf(
        Triple("All", painterResource(id = R.drawable.ic_all), painterResource(id = R.drawable.ic_all_filled)),
        Triple("Baseball", painterResource(id = R.drawable.ic_baseball), painterResource(id = R.drawable.ic_baseball_filled)),
        Triple("Basketball", painterResource(id = R.drawable.ic_basketball), painterResource(id = R.drawable.ic_basketball_filled)),
        Triple("Cross Country", painterResource(id = R.drawable.ic_cross_country), painterResource(id = R.drawable.ic_cross_country_filled)),
        Triple("Football", painterResource(id = R.drawable.ic_football), painterResource(id = R.drawable.ic_football_filled))
    )

    SportSelectorHeader(sports, {}, {})
}

@Composable
fun SportSelectorHeader(sports: List<Triple<String, Painter, Painter>>,
                        onGenderSelected: (String) -> Unit,
                        onSportSelected: (String) -> Unit) {
    var selectedOption by remember { mutableStateOf("All") }
    var selectedSport by remember { mutableStateOf(sports.first().first) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(143.dp)
            .background(color = White)
            .padding(start = 24.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .padding(end = 24.dp)
                .border(
                    border = BorderStroke(1.dp, GrayLight),
                    shape = RoundedCornerShape(100)
                )
                .padding(6.dp)
        ) {
            GenderSelector("All", selectedOption) {
                selectedOption = "All"
                onGenderSelected("All")
            }
            GenderSelector("Men's", selectedOption) {
                selectedOption = "Men's"
                onGenderSelected("Men's")
            }
            GenderSelector("Women's", selectedOption) {
                selectedOption = "Women's"
                onGenderSelected("Women's")
            }
        }
        LazyRow(Modifier.fillMaxWidth()) {
            items(sports) { (sport, emptyIcon, filledIcon) ->
                SportSelector(
                    empty = emptyIcon,
                    filled = filledIcon,
                    option = sport,
                    selectedOption = selectedSport
                ) {
                    selectedSport = sport
                    onSportSelected(sport)
                }

                Spacer(modifier = Modifier.width(20.dp))
            }
            item { Spacer(modifier = Modifier.width(4.dp)) }
        }
    }
}

@Composable
fun GenderSelector(option: String, selectedOption: String, onSelect: () -> Unit) {
    Box(
        modifier = Modifier
            .clickable(onClick = {
                onSelect()
            })
            .width(111.dp)
            .background(
                color = if (selectedOption == option) CrimsonPrimary else Color.Transparent,
                shape = RoundedCornerShape(100) // Rounded background
            )
            .padding(vertical = 8.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = option,
            style = if (selectedOption == option) genderFilterText else genderText
        )
    }
}

@Composable
fun SportSelector(
    empty: Painter,
    filled: Painter,
    option: String,
    selectedOption: String,
    onSelect: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = {
                onSelect()
            })
            .sizeIn(minWidth = 56.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = if (selectedOption == option) filled else empty,
            contentDescription = "Sport Icon",
            modifier = Modifier
                .padding(1.dp)
                .width(24.dp)
                .height(24.dp)
        )
        Text(
            text = option,
            style = sportFilterText,
            color = if (selectedOption == option) CrimsonPrimary else GrayMedium
        )
    }
}