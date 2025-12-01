package com.cornellappdev.score.components.highlights

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.R
import com.cornellappdev.score.theme.GrayLight
import com.cornellappdev.score.theme.Style.bodyNormal

@Composable
fun HighlightsSearchBar(
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    var searchQuery by remember { mutableStateOf("") } //todo: to be handled by viewmodel

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(100.dp))
            .border(1.dp, GrayLight, RoundedCornerShape(100.dp))
            .clip(RoundedCornerShape(100.dp))
            .clickable { onSearchClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically) {

        Icon(
            painter = painterResource(R.drawable.search),
            contentDescription = "search icon",
            tint = Color.Unspecified
        )

        Spacer(Modifier.width(8.dp))

        var isFocused by remember { mutableStateOf(false) }
        Box(modifier = Modifier.weight(1f)) {
            if (searchQuery.isEmpty()) {
                Text(
                    text = "Search keywords",
                    style = bodyNormal.copy(color = Color.Gray)
                )
            }

            BasicTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it /*todo viewmodel load results*/},
                singleLine = true,
                textStyle = bodyNormal,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused && !isFocused) {
                            /*todo call viewmodel -> clear highlight rows, load recent search history*/
                            isFocused = true
                        } else if (!focusState.isFocused) {
                            isFocused = false
                        }
                    }
            )
        }

        if (searchQuery.isNotEmpty()) {
            AnimatedVisibility(
                visible = searchQuery.isNotEmpty(),
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = "clear field",
                    modifier = Modifier.clickable(
                        onClick = { searchQuery = "" }
                    )
                )
            }
        }
    }
}

/*HighlightsSearchBarUI is the non-functional version of the HighlightsSearchBar, it's a dummy component that's clickable in HighlightsScreen and will navigate to HighlightsSearchScreen */
@Composable
fun HighlightsSearchBarUI(
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(100.dp))
            .border(1.dp, GrayLight, RoundedCornerShape(100.dp))
            .clip(RoundedCornerShape(100.dp))
            .clickable(onClick = { onClick() })
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.search),
            contentDescription = "search icon",
            tint = Color.Unspecified
        )

        Text(
            text = "Search keywords",
            style = bodyNormal.copy(color = Color.Gray)
        )
    }
}

@Preview
@Composable
private fun HighlightsSearchBarUIPreview() {
    HighlightsSearchBarUI({})
}

@Preview
@Composable
private fun HighlightsSearchBarPreview() {
    HighlightsSearchBar({})
}