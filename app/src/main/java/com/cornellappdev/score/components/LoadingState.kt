package com.cornellappdev.score.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.theme.GrayStroke
import com.cornellappdev.score.theme.Style.heading1

@Composable
fun LoadingState(){
    Column(
        modifier = Modifier.padding(24.dp)
    ){
        Text(
            text = "Upcoming",
            style = heading1,
            color = GrayStroke,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        LoadingStateBox(12, 200.dp)
    }
}

@Preview
@Composable
private fun LoadingStatePreview(){
    LoadingState()
}