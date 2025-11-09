package com.cornellappdev.score.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.R
import com.cornellappdev.score.theme.Style.bodyNormal
import com.cornellappdev.score.theme.Style.heading2

interface DisplayableFilter {
    val displayName: String
}

enum class PriceFilter(override val displayName: String) : DisplayableFilter {
    UNTICKETED("Unticketed"),
    UNDER_20("Under $20"),
    UNDER_50("Under $50"),
    OVER_50("Over $50")
}

enum class LocationFilter(override val displayName: String) : DisplayableFilter {
    ON_CAMPUS("On Campus"),
    ONE_TO_TWO_HOURS("1-2 Hours"),
    TWO_TO_FOUR_HOURS("2-4 Hours"),
    OVER_FOUR_HOURS("Over 4 Hours")
}

enum class DateFilter(override val displayName: String) : DisplayableFilter {
    TODAY("Today"),
    WITHIN_7_DAYS("Within 7 Days"),
    WITHIN_A_MONTH("Within a Month"),
    OVER_A_MONTH("Over a Month")
}

@Composable
fun <T : DisplayableFilter> ExpandableSection(
    title: String,
    options: List<T>,
    selectedOption: T?,
    onOptionSelected: (T?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, style = heading2)
            Icon(
                painter = painterResource(
                    id = if (expanded) R.drawable.ic_round_minus else R.drawable.ic_round_plus
                ),
                contentDescription = if (expanded) "Collapse" else "Expand"
            )
        }

        // Options
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
        ) {
            AnimatedVisibility(visible = expanded) {
                Column {
                    options.forEach { option ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onOptionSelected(option) }
                                .padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
                        ) {
                            CustomRadioButton(
                                selected = (selectedOption == option),
                                onClick = { onOptionSelected(option) }
                            )
                            Text(option.displayName, style = bodyNormal)
                        }
                    }
                }
            }
        }
    }
}
