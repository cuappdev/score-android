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
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.cornellappdev.score.R
import com.cornellappdev.score.model.ArticleHighlightData
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.theme.Style.bodySemibold
import com.cornellappdev.score.theme.Style.heading2
import com.cornellappdev.score.theme.Style.labelsNormal
import com.cornellappdev.score.theme.White

@Composable
fun ArticleHighlightCard(
    articleHighlight: ArticleHighlightData,
    modifier: Modifier = Modifier   //for wide cards, specify Modifier.fillMaxWidth()
) {
    Box(
        modifier = modifier
            .width(241.dp)
            .height(192.dp)
            .clip(shape = RoundedCornerShape(12.dp))
    ) {
        //todo: empty state if image doesn't load
        AsyncImage(
            model = articleHighlight.imageUrl,
            contentDescription = "highlight image",
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black.copy(alpha = 0.4f))
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
                    BasicText(
                        style = bodySemibold,
                        text = buildAnnotatedString {
                            withLink(
                                LinkAnnotation.Url(
                                    articleHighlight.articleUrl,
                                    TextLinkStyles(
                                        style = SpanStyle(
                                            textDecoration = TextDecoration.Underline,
                                            color = White
                                        )
                                    ),
                                )
                            ) {
                                append("Cornell Daily Sun")
                            }
                        }
                    )
                    Icon(
                        painter = painterResource(R.drawable.arrow_outward_white),
                        contentDescription = "external link arrow",
                        tint = Color.Unspecified
                    )
                }
                Text(
                    color = Color.White,
                    style = labelsNormal,
                    text = articleHighlight.date
                )
            }
        }
    }
}

@Preview
@Composable
private fun ArticleHighlightCardPreview() {
    ArticleHighlightCard(
        ArticleHighlightData(
            "Late Goal Lifts No. 6 Men’s Hockey Over Brown",
            "maxresdefault.jpg",
            "https://cornellsun.com/article/london-mcdavid-is-making-a-name-for-herself-at-cornell",
            "11/9",
            Sport.ICE_HOCKEY
        )
    )
}

@Preview
@Composable
private fun WideArticleHighlightCardPreview() {
    ArticleHighlightCard(
        ArticleHighlightData(
            "Late Goal Lifts No. 6 Men’s Hockey Over Brown",
            "maxresdefault.jpg",
            "https://cornellsun.com/article/london-mcdavid-is-making-a-name-for-herself-at-cornell",
            "11/9",
            Sport.ICE_HOCKEY
        ),
        Modifier.fillMaxWidth()
    )
}