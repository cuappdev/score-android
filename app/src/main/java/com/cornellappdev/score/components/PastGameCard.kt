package com.cornellappdev.score.components

import android.graphics.drawable.Icon
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.cornellappdev.score.R
import com.cornellappdev.score.model.GameCardData
import com.cornellappdev.score.theme.AmbientColor
import com.cornellappdev.score.theme.GrayLight
import com.cornellappdev.score.theme.GrayMedium
import com.cornellappdev.score.theme.GrayPrimary
import com.cornellappdev.score.theme.GrayStroke
import com.cornellappdev.score.theme.SpotColor
import com.cornellappdev.score.theme.Style.bodyNormal
import com.cornellappdev.score.theme.Style.heading2
import com.cornellappdev.score.theme.Style.labelsNormal
import com.cornellappdev.score.theme.Style.metricMedium
import com.cornellappdev.score.theme.saturatedGreen

@Composable
fun PastGameCard(
    teamLogo: String,
    team: String,
    date: String,
    location: String,
    genderIcon: Painter,
    sportIcon: Painter,
    modifier: Modifier = Modifier,
    cornellScore: Number,
    otherScore: Number,
    onClick: (Boolean) -> Unit = {}
) {
    val cornellWins = cornellScore.toFloat() > otherScore.toFloat()
    val isHome = location == "Ithaca, NY"
    val firstWins = (cornellWins && isHome) || (!cornellWins && !isHome)

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .shadow(elevation = 6.dp, spotColor = SpotColor, ambientColor = AmbientColor)
            .then(
                Modifier
                    .border(width = 1.dp, color = GrayStroke, RoundedCornerShape(16.dp))
            )
            .clickable { onClick(true) }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier
                .width(224.dp)
                .drawBehind {
                    drawLine(
                        color = GrayLight,
                        start = Offset(size.width, 0f),
                        end = Offset(size.width, size.height),
                        strokeWidth = 1.dp.toPx()
                    )
                }) {
                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()) {
                    Row(modifier = Modifier.widthIn(0.dp, 170.dp), verticalAlignment = Alignment.CenterVertically) {
                        if (isHome){
                            Image(
                                painter = painterResource(R.drawable.cornell_logo),
                                contentDescription = "Cornell Logo",
                                modifier = Modifier.height(27.dp).padding(horizontal = 2.dp, vertical = 4.dp)
                            )
                        } else {
                            AsyncImage(
                                model = teamLogo,
                                modifier = Modifier
                                    .height(20.dp)
                                    .padding(horizontal = 4.dp),
                                contentDescription = ""
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if(isHome) "Cornell" else team,
                            style = heading2,
                            color = if(firstWins) GrayPrimary else GrayLight
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = if(isHome) cornellScore.toString() else otherScore.toString(),
                            style = metricMedium,
                            color = if(firstWins) GrayPrimary else GrayLight)
                        Spacer(modifier = Modifier.width(12.dp))
                        if(firstWins) {
                            Icon(painter = painterResource(id = R.drawable.ic_arrow_back),
                                contentDescription = "Indicates winning team",
                                modifier = Modifier
                                    .width(11.dp)
                                    .height(14.dp),)
                        } else {
                             Box(modifier = Modifier.width(11.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.widthIn(0.dp, 170.dp), verticalAlignment = Alignment.CenterVertically) {
                        if (!isHome){
                            Image(
                                painter = painterResource(R.drawable.cornell_logo),
                                contentDescription = "Cornell Logo",
                                modifier = Modifier.height(27.dp).padding(horizontal = 4.dp, vertical = 4.dp)
                            )
                        } else {
                            AsyncImage(
                                model = teamLogo,
                                modifier = Modifier
                                    .height(20.dp)
                                    .padding(horizontal = 4.dp),
                                contentDescription = ""
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if(!isHome) "Cornell" else team,
                            style = heading2,
                            color = if(firstWins) GrayLight else GrayPrimary
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = if(!isHome) cornellScore.toString() else otherScore.toString(),
                            style = metricMedium,
                            color = if(firstWins) GrayLight else GrayPrimary)
                        Spacer(modifier = Modifier.width(12.dp))
                        if(!firstWins) {
                            Icon(painter = painterResource(id = R.drawable.ic_arrow_back),
                                contentDescription = "Indicates winning team",
                                modifier = Modifier
                                    .width(11.dp)
                                    .height(14.dp),)
                        } else {
                            Box(modifier = Modifier.width(11.dp))
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.width(24.dp))
            Column(modifier = Modifier.height(64.dp),
                verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.End) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = sportIcon,
                        contentDescription = "Sport Icon",
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp),
                        tint = Color.Unspecified
                    )
                    Icon(
                        painter = genderIcon,
                        contentDescription = "Gender Icon",
                        modifier = Modifier
                            .padding(2.5.dp)
                            .width(19.dp)
                            .height(19.dp),
                        tint = Color.Unspecified
                    )
                }
                Text(
                    text = date,
                    style = labelsNormal,
                    color = GrayMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PastGameCardPreview() {
    Column {
        PastGameCard(
            teamLogo = "https://cornellbigred.com/images/logos/penn_200x200.png?width=80&height=80&mode=max", //painterResource(id = R.drawable.penn_logo),
            team = "University of Pennsylvania",
            date = "5/20/2024",
            location = "U. Pennsylvania",
            genderIcon = painterResource(id = R.drawable.ic_gender_men),
            sportIcon = painterResource(id = R.drawable.ic_baseball),
            modifier = Modifier.padding(16.dp),
            cornellScore = 3,
            otherScore = 0
        )
    }
}