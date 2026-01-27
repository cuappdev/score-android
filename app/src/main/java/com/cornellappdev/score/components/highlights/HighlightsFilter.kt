package com.cornellappdev.score.components.highlights


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults.outlinedButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.theme.GrayLight
import com.cornellappdev.score.theme.GrayPrimary
import com.cornellappdev.score.theme.Stroke
import com.cornellappdev.score.theme.Style.bodyNormal
import com.cornellappdev.score.theme.White
import com.cornellappdev.score.util.sportList

@Composable
private fun HighlightsFilterButton(
    sport: Sport,
    onFilterSelected: (Sport) -> Unit,
    isSelected: Boolean = false,
) {
    OutlinedButton(
        modifier = Modifier
            .height(32.dp),
        border = BorderStroke(width = 1.dp, color = Stroke),
        onClick = { onFilterSelected(sport) },
        shape = RoundedCornerShape(100.dp),
        colors = outlinedButtonColors(
            containerColor = if (isSelected) GrayLight else White,
            contentColor = GrayPrimary
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
    ) {
        Icon(
            painter = painterResource(sport.emptyIcon),
            contentDescription = "sport Icon",
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = sport.displayName,
            style = bodyNormal
        )
    }
}

@Composable
fun HighlightsFilterRow(
    sportList: List<Sport>,
    onFilterSelected: (Sport) -> Unit,
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item { Spacer(Modifier.width(12.dp)) }
        items(sportList) { item ->
            HighlightsFilterButton(item, onFilterSelected)
        }
        item { Spacer(Modifier.width(12.dp)) }
    }
}

@Preview
@Composable
private fun HighlightsFilterButtonPreview() {
    var isSelected by remember { mutableStateOf(false) }
    HighlightsFilterButton(Sport.BASEBALL, { isSelected = !isSelected }, isSelected = isSelected)
}

@Preview
@Composable
private fun HighlightsFilterRowPreview() {
    HighlightsFilterRow(sportList, {})
}
