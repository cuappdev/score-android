package com.cornellappdev.score.components.highlights

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.R
import com.cornellappdev.score.theme.CrimsonPrimary
import com.cornellappdev.score.theme.White

/** UI-only state until account sign-in and saved highlights are available. */
@Composable
fun HighlightFavoriteButton(
    highlightKey: String,
    modifier: Modifier = Modifier,
    hasAccount: Boolean = false,
    initiallyFavorited: Boolean = false
) {
    var isFavorited by rememberSaveable(highlightKey) { mutableStateOf(initiallyFavorited) }
    var showSignIn by rememberSaveable(highlightKey) { mutableStateOf(false) }

    // Keep a 48dp touch target around the design's 32dp button.
    Box(modifier = modifier.size(48.dp), contentAlignment = Alignment.Center) {
        Surface(
            onClick = {
                if (hasAccount) {
                    isFavorited = !isFavorited
                } else {
                    showSignIn = true
                }
            },
            modifier = Modifier.size(32.dp).semantics { selected = isFavorited },
            shape = RoundedCornerShape(8.dp),
            color = if (isFavorited) CrimsonPrimary else White,
            shadowElevation = 3.dp
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_highlight_bookmark),
                contentDescription = if (isFavorited) "Unfavorite highlight" else "Favorite highlight",
                tint = if (isFavorited) White else CrimsonPrimary,
                modifier = Modifier.padding(8.dp)
            )
        }
    }

    if (showSignIn) {
        HighlightSignInSheet(onDismiss = { showSignIn = false })
    }
}
