package com.cornellappdev.score.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.model.GenderDivision
import com.cornellappdev.score.theme.CrimsonPrimary
import com.cornellappdev.score.theme.GrayLight
import com.cornellappdev.score.theme.GrayMedium
import com.cornellappdev.score.theme.Style
import com.cornellappdev.score.theme.White

/** Accessible, single-choice filter shared by the Home screen and onboarding. */
@Composable
fun GenderDivisionFilter(
    selectedGender: GenderDivision,
    onGenderSelected: (GenderDivision) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = listOf(
        GenderDivision.ALL,
        GenderDivision.MALE,
        GenderDivision.FEMALE
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(48.dp)
            .border(BorderStroke(1.dp, GrayLight), CircleShape)
            .padding(5.dp)
            .selectableGroup(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEach { gender ->
            val selected = gender == selectedGender
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        color = if (selected) CrimsonPrimary else Color.Transparent,
                        shape = CircleShape
                    )
                    .selectable(
                        selected = selected,
                        role = Role.RadioButton,
                        onClick = { onGenderSelected(gender) }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = gender.displayName,
                    style = if (selected) Style.bodySemibold else Style.bodyMedium,
                    color = if (selected) White else GrayMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GenderDivisionFilterPreview() {
    ScorePreview {
        var selectedGender by remember { mutableStateOf(GenderDivision.ALL) }

        GenderDivisionFilter(
            selectedGender = selectedGender,
            onGenderSelected = { selectedGender = it }
        )
    }
}
