package com.cornellappdev.score.components.highlights

import android.graphics.BitmapFactory
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.annotation.ExperimentalCoilApi
import coil3.asImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import com.cornellappdev.score.R
import com.cornellappdev.score.components.ScorePreview
import com.cornellappdev.score.model.ArticleHighlightData
import com.cornellappdev.score.model.GenderDivision
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.model.VideoHighlightData
import com.cornellappdev.score.theme.Style.heading1

@OptIn(ExperimentalCoilApi::class)
@Preview(showBackground = true, widthDp = 393)
@Composable
internal fun HighlightFavoritingPreview() {
    val context = LocalContext.current
    val videoImage = "android.resource://${context.packageName}/${R.drawable.preview_highlight_soccer}"
    val articleImage = "android.resource://${context.packageName}/${R.drawable.preview_highlight_hockey}"
    val previewHandler = AsyncImagePreviewHandler { request ->
        BitmapFactory.decodeResource(
            context.resources,
            if (request.data == videoImage) R.drawable.preview_highlight_soccer
            else R.drawable.preview_highlight_hockey
        ).asImage()
    }
    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
        ScorePreview {
            Column(Modifier.padding(vertical = 24.dp)) {
                Text("Highlights", style = heading1, modifier = Modifier.padding(horizontal = 24.dp))
                Spacer(Modifier.height(24.dp))
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    VideoHighlightCard(
                        videoHighlight = VideoHighlightData(
                            title = "Highlights: Cornell Women’s Soccer",
                            thumbnailImageUrl = videoImage,
                            videoUrl = "https://www.youtube.com/",
                            date = "10/15",
                            sport = Sport.SOCCER,
                            gender = GenderDivision.FEMALE
                        ),
                        isWideFormat = false
                    )
                    ArticleHighlightCard(
                        articleHighlight = ArticleHighlightData(
                            title = "CORNELL NOTES | No. 14 Women’s Hockey Readies for Another Season",
                            imageUrl = articleImage,
                            articleUrl = "https://cornellsun.com/",
                            date = "10/15",
                            sport = Sport.ICE_HOCKEY
                        ),
                        isWideFormat = false
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 393)
@Composable
private fun HighlightSignInPromptPreview() {
    ScorePreview {
        HighlightSignInSheetContent(onDismiss = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun HighlightBookmarkStatesPreview() {
    Row {
        HighlightFavoriteButton(highlightKey = "signed-out")
        HighlightFavoriteButton(highlightKey = "signed-in", hasAccount = true)
        HighlightFavoriteButton(highlightKey = "saved", hasAccount = true, initiallyFavorited = true)
    }
}
