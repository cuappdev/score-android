package com.cornellappdev.score.components.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.theme.Style.bodyNormal
import com.cornellappdev.score.theme.Style.bodySemibold
import com.cornellappdev.score.theme.Style.labelsNormal

@Composable
fun HomeScreenFavoritesInfoBanner(

){
    val gradientRed = Brush.horizontalGradient(0f to Color(0xFFBC2811), 1000f to Color(0xFF62180C))
    Row(
        modifier = Modifier.fillMaxWidth().background(gradientRed),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ){
        Column(

        ){
            Text(
                text = "Showing all sports",
                style = bodySemibold
            )
            Text(
                text = "Select your favorite sports to personalize your feed.",
                style = labelsNormal
            )
        }


    }
}

@Composable
fun GoToProfileButton(){

}