package com.cornellappdev.score.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.theme.GrayPrimary

@Composable
fun ScoreRadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    RadioButton(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        colors = RadioButtonDefaults.colors(
            selectedColor = GrayPrimary,
            unselectedColor = GrayPrimary
        )
    )
}

@Preview
@Composable
private fun ScoreRadioButtonPreview() = ScorePreview {
    Column(modifier = Modifier.padding(16.dp)) {
        ScoreRadioButton(
            selected = false,
            onClick = {}
        )
        ScoreRadioButton(
            selected = true,
            onClick = {}
        )
    }
}
