package com.cornellappdev.score.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.R
import com.cornellappdev.score.theme.Style.heading2

@Composable
fun NavigationHeader(title: String, onBackPressed: () -> Unit) {
    Box(
        modifier = Modifier
             .shadow(elevation = 8.dp, clip = false, spotColor = Color.Black.copy(0.05f))
             .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                 .padding(start = 24.dp, top = 24.dp, bottom = 12.dp, end = 24.dp)
                 .background(Color.White)
                 .fillMaxWidth()
                 .height(27.dp)
        ) {
            IconButton(onClick = onBackPressed) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_left_arrowhead),
                    contentDescription = "Back button",
                    modifier = Modifier
                         .width(24.dp)
                         .height(24.dp),
                )
            }
            Text(
                text = title,
                modifier = Modifier.align(Alignment.Center),
                style = heading2,
                color = Color.Black
            )
        }
    }
}

@Preview
@Composable
private fun NavigationHeaderPreview() {
     NavigationHeader("Game Details", {})
}