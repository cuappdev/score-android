package com.cornellappdev.score.components.highlights

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.cornellappdev.score.model.ArticleHighlightData
import com.cornellappdev.score.util.sampleArticleHighlight
import com.cornellappdev.score.theme.Style.heading2
import com.cornellappdev.score.theme.Style.labelsNormal
import com.cornellappdev.score.theme.White

@Composable
fun ArticleHighlightCard(
    articleHighlight: ArticleHighlightData,
    isWideFormat: Boolean,
    modifier: Modifier = Modifier,
    linkTextStyle: TextStyle = TextStyle.Default,
    imageOverlayAlpha: Float = 0.4f
) {
    Box(
        modifier = modifier
            .then(if (isWideFormat) Modifier.fillMaxWidth() else Modifier.width(241.dp))
            .height(192.dp)
            .clip(shape = RoundedCornerShape(12.dp))
    ) {
        //todo: empty state if image doesn't load
        AsyncImage(
            model = articleHighlight.imageUrl,
            contentDescription = "highlight image",
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black.copy(alpha = imageOverlayAlpha))
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                style = heading2,
                color = Color.White,
                text = articleHighlight.title,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isWideFormat) {
                        Text("Read at ", color = White)
                    }
                    ExternalLink(
                        articleHighlight.articleUrl,
                        urlLabel = "Cornell Daily Sun",
                        linkColor = White,
                        textStyle = linkTextStyle
                    )
                }
                Text(
                    text = articleHighlight.dateString,
                    style = labelsNormal,
                    color = Color.White,
                )
            }
        }
    }
}

@Preview
@Composable
private fun ArticleHighlightCardPreview() {
    ArticleHighlightCard(
        sampleArticleHighlight,
        false
    )
}

@Preview
@Composable
private fun WideArticleHighlightCardPreview() {
    ArticleHighlightCard(
        sampleArticleHighlight,
        true
    )
}