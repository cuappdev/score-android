package com.cornellappdev.score.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cornellappdev.score.R
import com.cornellappdev.score.auth.AuthState
import com.cornellappdev.score.auth.AuthStatusOverlay
import com.cornellappdev.score.components.EmptyStateBox
import com.cornellappdev.score.components.NavigationHeader
import com.cornellappdev.score.components.ScorePreview
import com.cornellappdev.score.components.highlights.HighlightsCardRow
import com.cornellappdev.score.components.highlights.HighlightsFilterRow
import com.cornellappdev.score.components.highlights.HighlightsSearchEntryPointRow
import com.cornellappdev.score.model.HighlightData
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.theme.Style.heading1
import com.cornellappdev.score.util.highlightsList
import com.cornellappdev.score.util.sportList
import com.cornellappdev.score.viewmodel.AccountViewModel

@Composable
fun HighlightsScreen(
    viewModel: AccountViewModel,
    onSignIn: () -> Unit,
    bookmarksOnly: Boolean = false,
    onBack: (() -> Unit)? = null,
    onSearch: () -> Unit = {},
) {
    LaunchedEffect(viewModel) { viewModel.refreshAccount() }
    val profile by viewModel.profile.collectAsStateWithLifecycle()
    val highlights by viewModel.highlights.collectAsStateWithLifecycle()
    val auth by viewModel.authState.collectAsStateWithLifecycle()
    val status by viewModel.status.collectAsStateWithLifecycle()
    val items = if (bookmarksOnly) profile.bookmarks else highlights
    if (!bookmarksOnly) {
        HighlightsScreenLayout(
            sportList =
                highlights
                    .filterIsInstance<HighlightData.Video>()
                    .mapNotNull { it.data.sport }
                    .distinct(),
            toSearchScreen = onSearch,
            allHighlightsList = highlights,
            bookmarkedIds = profile.bookmarkedHighlightIds,
            onToggleBookmark = {
                if (auth is AuthState.SignedIn) viewModel.toggleBookmark(it) else onSignIn()
            },
        )
    } else
        Column(Modifier.fillMaxSize()) {
            if (onBack != null) {
                NavigationHeader(stringResource(R.string.profile_bookmarked_highlights), onBack)
            } else {
                Text(
                    stringResource(R.string.profile_highlights),
                    modifier = Modifier.padding(24.dp),
                )
            }
            LazyColumn(
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                if (items.isEmpty()) {
                    item { Text(stringResource(R.string.profile_no_bookmarks)) }
                }
                items(items) { highlight ->
                    Column {
                        ProfileBookmarkCard(highlight)
                        val id = (highlight as? HighlightData.Video)?.data?.id
                        if (id != null) {
                            TextButton(
                                onClick = {
                                    if (auth is AuthState.SignedIn)
                                        viewModel.toggleBookmark(highlight)
                                    else onSignIn()
                                }
                            ) {
                                Text(
                                    stringResource(
                                        if (id in profile.bookmarkedHighlightIds)
                                            R.string.account_bookmark_remove
                                        else R.string.account_bookmark_add
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    AuthStatusOverlay(status, viewModel::dismissError)
}

@Composable
private fun HighlightsScreenLayout(
    toSearchScreen: () -> Unit,
    sportList: List<Sport> = emptyList(), //note - emptyLists are placeholders for nav to work, will replace will viewModel
    todayHighlightsList: List<HighlightData> = emptyList(),
    pastThreeHighlightsList: List<HighlightData> = emptyList(),
    allHighlightsList: List<HighlightData> = emptyList(),
    bookmarkedIds: Set<String> = emptySet(),
    onToggleBookmark: ((HighlightData) -> Unit)? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Column(
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Text("Highlights", style = heading1)
            Spacer(modifier = Modifier.height(12.dp))
            HighlightsSearchEntryPointRow(toSearchScreen)
        }
        Spacer(modifier = Modifier.height(16.dp))
        HighlightsFilterRow(sportList, { /*todo: handle with viewmodel*/ })
        Spacer(modifier = Modifier.height(24.dp))
        if (todayHighlightsList.isEmpty() && pastThreeHighlightsList.isEmpty() && allHighlightsList.isEmpty()) {
            EmptyStateBox(
                icon = R.drawable.ic_kid_star,
                title = "No results yet.",
            )
        }
        if (allHighlightsList.isNotEmpty()) {
            HighlightsCardRow(allHighlightsList, "All highlights", bookmarkedIds, onToggleBookmark)
        }
        if (todayHighlightsList.isNotEmpty()) {
            HighlightsCardRow(todayHighlightsList, "Today")
        }
        if (pastThreeHighlightsList.isNotEmpty()) {
            HighlightsCardRow(pastThreeHighlightsList, "Past 3 days")
        }
    }
}

data class HighlightsScreenPreviewData(
    val sportList: List<Sport>,
    val todayHighlightList: List<HighlightData>,
    val pastHighlightList: List<HighlightData>
)

class HighlightsScreenPreviewProvider : PreviewParameterProvider<HighlightsScreenPreviewData> {
    override val values: Sequence<HighlightsScreenPreviewData> = sequence {
        yield(HighlightsScreenPreviewData(sportList, highlightsList, highlightsList))
        yield(HighlightsScreenPreviewData(sportList, emptyList(), emptyList()))
        yield(HighlightsScreenPreviewData(sportList, emptyList(), highlightsList))
    }
}

@Preview(showBackground = true)
@Composable
private fun HighlightScreenPreview(
    @PreviewParameter(HighlightsScreenPreviewProvider::class) previewData: HighlightsScreenPreviewData
) {
    ScorePreview {
        HighlightsScreenLayout(
            sportList = previewData.sportList,
            todayHighlightsList = previewData.todayHighlightList,
            pastThreeHighlightsList = previewData.pastHighlightList,
            toSearchScreen = {}
        )
    }
}
