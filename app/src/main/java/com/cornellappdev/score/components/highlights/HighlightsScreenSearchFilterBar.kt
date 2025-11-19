package com.cornellappdev.score.components.highlights

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.components.ScorePreview
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.theme.Style.bodyMedium
import com.cornellappdev.score.util.sportList

@Composable
fun HighlightsScreenSearchFilterBar(
    sportList: List<Sport>,
    focusRequester: FocusRequester
) {
    val focusManager = LocalFocusManager.current
    var isActive by remember { mutableStateOf(true) }
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .clip(shape = RoundedCornerShape(100.dp))
                .clickable(onClick = {/*todo clear the highlight rows and show recent searches*/ }),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HighlightsSearchBar(
                onSearchClick = {
                    isActive = true
                    focusRequester.requestFocus()
                },
                focusRequester = focusRequester,
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { state ->
                        // When the search bar loses focus, hide cancel
                        if (!state.isFocused) {
                            isActive = !isActive
                        }
                    }
            )

            if (isActive) {
                Box(
                    modifier = Modifier.clickable {
                        isActive = true;
                        focusManager.clearFocus(force = true)
                        /*todo: clear the text in the search bar*/
                    }
                ) {
                    Text("Cancel", style = bodyMedium)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        HighlightsFilterRow(sportList, {/*todo on filter selected*/ })
    }
}

@Preview
@Composable
private fun HighlightsScreenSearchFilterBarActivePreview() {
    ScorePreview {
        HighlightsScreenSearchFilterBar(sportList, FocusRequester())
    }
}

@Preview
@Composable
private fun HighlightsScreenSearchFilterBarInactivePreview() {
    ScorePreview {
        HighlightsScreenSearchFilterBar(sportList, FocusRequester())
    }
}