package com.cornellappdev.score.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.theme.GrayPrimary
import com.cornellappdev.score.theme.GrayStroke
import com.cornellappdev.score.theme.Style.heading1

@Composable
fun ErrorState(){
    Column(
        modifier = Modifier.padding(24.dp)
    ){
        Text(
            text = "Upcoming",
            style = heading1,
            color = GrayStroke,
            modifier = Modifier.fillMaxWidth()
        )
    }
}