package com.cornellappdev.score.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import com.cornellappdev.score.R
import com.cornellappdev.score.model.GenderDivision
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.model.SportSelection
import com.cornellappdev.score.theme.CrimsonPrimary
import com.cornellappdev.score.theme.GrayLight
import com.cornellappdev.score.theme.GrayMedium
import com.cornellappdev.score.theme.Style.bodyMedium
import com.cornellappdev.score.theme.Style.bodySemibold
import com.cornellappdev.score.theme.Style.genderFilterText
import com.cornellappdev.score.theme.Style.genderText
import com.cornellappdev.score.theme.Style.labelsMedium
import com.cornellappdev.score.theme.Style.labelsNormal
import com.cornellappdev.score.theme.Style.sportFilterText
import com.cornellappdev.score.theme.White
import com.cornellappdev.score.util.sportList
import com.cornellappdev.score.util.sportSelectionList

@Composable
fun SportSelectorHeader(
    sports: List<SportSelection>,
    selectedGender: GenderDivision,
    selectedSport: SportSelection,
    onGenderSelected: (GenderDivision) -> Unit,
    onSportSelected: (SportSelection) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = White)
            .padding(start = 24.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
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
            GenderSelector(GenderDivision.ALL, selectedGender == GenderDivision.ALL) {
                onGenderSelected(GenderDivision.ALL)
            }
            GenderSelector(GenderDivision.MALE, selectedGender == GenderDivision.MALE) {
                onGenderSelected(GenderDivision.MALE)
            }
            GenderSelector(GenderDivision.FEMALE, selectedGender == GenderDivision.FEMALE) {
                onGenderSelected(GenderDivision.FEMALE)
            }
        }
        Spacer(Modifier.height(24.dp))
        LazyRow(Modifier.fillMaxWidth()) {
            items(sports) { selection ->
                when (selection) {
                    SportSelection.All -> {
                        SportSelector(
                            empty = painterResource(
                                R.drawable.ic_all
                            ),
                            filled = painterResource(
                                R.drawable.ic_all_filled
                            ),
                            option = "All",
                            enabled = selectedSport == SportSelection.All
                        ) {
                            onSportSelected(selection)
                        }
                    }

                    is SportSelection.SportSelect -> {
                        val sport = selection.sport
                        SportSelector(
                            empty = painterResource(sport.emptyIcon),
                            filled = painterResource(sport.filledIcon),
                            option = sport.displayName,
                            enabled = selectedSport == selection
                        ) {
                            onSportSelected(selection)
                        }
                    }
                }


                Spacer(modifier = Modifier.width(20.dp))
            }
            item { Spacer(modifier = Modifier.width(4.dp)) }
        }
    }
}

@Composable
fun GenderSelector(option: GenderDivision, isSelected: Boolean, onSelect: () -> Unit) {
    Box(
        modifier = Modifier
            .clickable(onClick = {
                onSelect()
            })
            .width(111.dp)
            .background(
                color = if (isSelected) CrimsonPrimary else Color.Transparent,
                shape = RoundedCornerShape(100) // Rounded background
            )
            .padding(vertical = 8.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = option.displayName,
            style = if (isSelected) bodySemibold else bodyMedium,
            color = if (isSelected) White else GrayMedium
        )
    }
}

@Composable
fun SportSelector(
    empty: Painter,
    filled: Painter,
    option: String,
    enabled: Boolean,
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
            painter = if (enabled) filled else empty,
            contentDescription = "Sport Icon",
            modifier = Modifier
                .padding(1.dp)
                .width(24.dp)
                .height(24.dp)
        )
        Text(
            text = option,
            style = if (enabled) labelsMedium else labelsNormal,
            color = if (enabled) CrimsonPrimary else GrayMedium
        )
    }
}

@Preview
@Composable
fun PreviewSportSelectorHeader() {
    var selectedOption by remember { mutableStateOf(GenderDivision.ALL) }
    var selectedSport: SportSelection by remember { mutableStateOf(SportSelection.All) }

    SportSelectorHeader(
        sports = sportSelectionList,
        selectedGender = selectedOption,
        selectedSport = selectedSport,
        onGenderSelected = { selectedOption = it },
        onSportSelected = { selectedSport = it }
    )
}
