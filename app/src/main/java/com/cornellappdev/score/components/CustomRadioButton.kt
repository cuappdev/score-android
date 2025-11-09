package com.cornellappdev.score.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.R

@Composable
fun CustomRadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(
            id = if (selected) R.drawable.ic_radio_selected else R.drawable.ic_radio_unselected
        ),
        contentDescription = if (selected) "Selected" else "Unselected",
        modifier = modifier
            .size(20.dp)
            .padding(end = 8.dp)
            .clickable(onClick = onClick)
    )
}