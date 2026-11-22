package com.cornellappdev.score.components.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.R
import com.cornellappdev.score.theme.Style.bodySemibold
import com.cornellappdev.score.theme.Style.labelsMedium
import com.cornellappdev.score.theme.Style.labelsNormal

@Composable
fun HomeScreenFavoritesInfoBanner(
    toProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gradientRed = Brush.horizontalGradient(0f to Color(0xFFBC2811), 1000f to Color(0xFF62180C))
    Row(
        modifier = modifier
            .clip(shape = RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .background(gradientRed)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Showing all sports",
                style = bodySemibold,
                color = Color.White
            )
            Text(
                text = "Select your favorite sports to personalize your feed.",
                style = labelsNormal,
                color = Color.White
            )
        }
        GoToProfileButton(toProfile)
    }
}

@Composable
fun GoToProfileButton(
    toProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .clip(shape = RoundedCornerShape(100.dp))
            .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(100.dp))
            .background(shape = RoundedCornerShape(100.dp), color = Color.Transparent)
            .padding(top = 8.dp, bottom = 8.dp, start = 12.dp, end = 8.dp)
            .clickable(onClick = toProfile)
    ){
        Text(
            text = "Go to Profile",
            color = Color.White,
            style = labelsMedium
        )
        Image(
            painter = painterResource(id = R.drawable.ic_right_chevron),
            contentDescription = "Right arrow",
            modifier = Modifier.size(20.dp)
        )
    }
}

@Preview
@Composable
private fun GoToProfileButtonPreview(){
    GoToProfileButton({})
}

@Preview
@Composable
private fun HomeScreenFavoritesInfoBannerPreview(){
    HomeScreenFavoritesInfoBanner({})
}