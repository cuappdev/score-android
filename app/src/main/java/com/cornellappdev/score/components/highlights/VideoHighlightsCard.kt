package com.cornellappdev.score.components.highlights

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.cornellappdev.score.R
import com.cornellappdev.score.components.ScorePreview
import com.cornellappdev.score.model.GenderDivision
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.model.VideoHighlightData
import com.cornellappdev.score.theme.CrimsonPrimary
import com.cornellappdev.score.theme.GrayStroke
import com.cornellappdev.score.theme.Style.heading2
import com.cornellappdev.score.theme.Style.labelsNormal

@Composable
private fun VideoHighlightCardHeader(
    imageUrl: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(117.dp)
    ) {
        //todo: empty state if image doesn't load
        AsyncImage(
            model = imageUrl,
            contentDescription = "Highlight article image",
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black.copy(alpha = 0.4f))
        )
    }
}

@Composable
fun VideoHighlightCardBody(
    videoHighlight: VideoHighlightData,
    isWideFormat: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
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
                Icon(
                    painter = painterResource(videoHighlight.sport.emptyIcon),
                    contentDescription = "Sport icon",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Unspecified
                )
                Icon(
                    painter = painterResource(if (videoHighlight.gender == GenderDivision.FEMALE) R.drawable.ic_gender_women else R.drawable.ic_gender_men),
                    contentDescription = "Gender icon",
                    tint = Color.Unspecified
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
                if (isWideFormat) {
                    Text("Watch on ")
                }
                ExternalLink(videoHighlight.videoUrl, "Youtube", CrimsonPrimary)
            }
            Text(
                style = labelsNormal,
                text = videoHighlight.date
            )
        }
    }
}

@Composable
fun VideoHighlightCard(
    videoHighlight: VideoHighlightData,
    isWideFormat: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .then(if (isWideFormat) Modifier.fillMaxWidth() else Modifier.width(241.dp))
            .clip(RoundedCornerShape(12.dp))
    ) {
        VideoHighlightCardHeader(videoHighlight.thumbnailImageUrl)
        VideoHighlightCardBody(videoHighlight, isWideFormat)
    }
}

data class VideoHighlightPreviewData(
    val videoHighlight: VideoHighlightData,
    val isWideFormat: Boolean
)

class VideoHighlightsPreviewProvider : PreviewParameterProvider<VideoHighlightPreviewData> {
    override val values: Sequence<VideoHighlightPreviewData> = sequence {
        val samples = listOf(
            VideoHighlightData(
                "vs Columbia",
                "maxresdefault.jpg",
                "https://cornellsun.com/article/london-mcdavid-is-making-a-name-for-herself-at-cornell",
                "11/9",
                Sport.BASEBALL,
                GenderDivision.MALE
            ),
            VideoHighlightData(
                "Late Goal Lifts No. 6 Men’s Hockey Over Brown",
                "maxresdefault.jpg",
                "https://cornellsun.com/article/london-mcdavid-is-making-a-name-for-herself-at-cornell",
                "11/9",
                Sport.BASEBALL,
                GenderDivision.MALE
            )
        )
        for (sample in samples) {
            yield(VideoHighlightPreviewData(sample, true))
            yield(VideoHighlightPreviewData(sample, false))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VideoHighlightCardPreview(
    @PreviewParameter(VideoHighlightsPreviewProvider::class) previewData: VideoHighlightPreviewData
) {
    ScorePreview {
        VideoHighlightCard(
            videoHighlight = previewData.videoHighlight,
            isWideFormat = previewData.isWideFormat
        )
    }
}