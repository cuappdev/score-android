package com.cornellappdev.score.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.cornellappdev.score.model.GenderDivision
import com.cornellappdev.score.model.VideoHighlightData
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.theme.CrimsonPrimary
import com.cornellappdev.score.theme.GrayStroke
import com.cornellappdev.score.theme.Style.bodySemibold
import com.cornellappdev.score.theme.Style.heading2
import com.cornellappdev.score.theme.Style.labelsNormal
import com.cornellappdev.score.theme.White

@Composable
fun VideoHighlightCardHeader(
    image: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(117.dp)
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
    ) {
        AsyncImage(
            model = image,
            contentDescription = "Highlight article image"
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black.copy(alpha = 0.4f))
        )
    }
}

@Composable
fun VideoHighlightCard(
    videoHighlight: VideoHighlightData,
    wide: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = if (wide) modifier.fillMaxWidth() else modifier.width(241.dp)
    ) {
        VideoHighlightCardHeader(videoHighlight.image)

        Column(
            modifier = Modifier
                .height(75.dp)
                .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                .background(color = Color.White)
                .border(
                    width = 1.dp,
                    color = GrayStroke,
                    shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                )
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    style = heading2,
                    text = videoHighlight.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(videoHighlight.sport.emptyIcon),
                        contentDescription = "Sport icon",
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp)
                    )
                    Image(
                        painter = painterResource(if (videoHighlight.gender == GenderDivision.FEMALE) R.drawable.ic_gender_women else R.drawable.ic_gender_men),
                        contentDescription = "Gender icon"
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicText(
                        text = buildAnnotatedString {
                            withLink(
                                LinkAnnotation.Url(
                                    videoHighlight.url,
                                    TextLinkStyles(
                                        style = SpanStyle(
                                            textDecoration = TextDecoration.Underline,
                                            color = CrimsonPrimary
                                        )
                                    ),
                                )
                            ) {
                                append("YouTube")
                            }
                        }
                    )
                    Image(
                        painter = painterResource(R.drawable.arrow_outward_red),
                        contentDescription = "external link arrow"
                    )
                }
                Text(
                    style = labelsNormal,
                    text = "11/09"
                )
            }
        }
    }
}

@Composable
fun ArticleHighlightCard(
    articleHighlight: ArticleHighlightData,
    wide: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.then(
            if (wide) Modifier.fillMaxWidth() else Modifier.width(241.dp)
        )
            .height(192.dp)
            .clip(shape = RoundedCornerShape(12.dp))
    ) {
        AsyncImage(
            model = articleHighlight.image,
            contentDescription = "highlight image"
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
                                    articleHighlight.url,
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
                    Image(
                        painter = painterResource(R.drawable.arrow_outward_white),
                        contentDescription = "external link arrow"
                    )
                }
                Text(
                    color = Color.White,
                    style = labelsNormal,
                    text = "11/09"
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
            "11/9"
        ),
        false
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
            "11/9"
        ),
        true
    )
}

@Preview
@Composable
private fun VideoHighlightCardPreview() {
    VideoHighlightCard(
        VideoHighlightData(
            "vs Columbia",
            "maxresdefault.jpg",
            "https://cornellsun.com/article/london-mcdavid-is-making-a-name-for-herself-at-cornell",
            "11/9",
            Sport.BASEBALL,
            GenderDivision.MALE
        ),
        false
    )
}

@Preview
@Composable
private fun WideVideoHighlightCardPreview() {
    VideoHighlightCard(
        VideoHighlightData(
            "vs Columbia",
            "maxresdefault.jpg",
            "https://cornellsun.com/article/london-mcdavid-is-making-a-name-for-herself-at-cornell",
            "11/9",
            Sport.BASEBALL,
            GenderDivision.MALE
        ),
        true
    )
}

@Preview
@Composable
private fun OverflowVideoHighlightCardPreview() {
    VideoHighlightCard(
        VideoHighlightData(
            "Late Goal Lifts No. 6 Men’s Hockey Over Brown",
            "maxresdefault.jpg",
            "https://cornellsun.com/article/london-mcdavid-is-making-a-name-for-herself-at-cornell",
            "11/9",
            Sport.BASEBALL,
            GenderDivision.MALE
        ),
        false
    )
}

@Preview
@Composable
private fun WideOverflowVideoHighlightCardPreview() {
    VideoHighlightCard(
        VideoHighlightData(
            "Late Goal Lifts No. 6 Men’s Hockey Over Brown",
            "maxresdefault.jpg",
            "https://cornellsun.com/article/london-mcdavid-is-making-a-name-for-herself-at-cornell",
            "11/9",
            Sport.BASEBALL,
            GenderDivision.MALE
        ),
        true
    )
}