package com.cornellappdev.score.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.theme.CrimsonPrimary
import com.cornellappdev.score.theme.Style.bodyMedium
import com.cornellappdev.score.theme.White

@Composable
fun ButtonPrimary(
    text: String,
    icon: Painter?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = CrimsonPrimary,
            disabledContainerColor = CrimsonPrimary.copy(alpha = 0.38f)
        ),
        modifier = modifier,
        contentPadding = PaddingValues(12.dp)
    ) {
        if (icon != null) {
            Image(
                painter = icon,
                contentDescription = "Icon",
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp),
                colorFilter = ColorFilter.tint(White)
            )
            if (text.isNotEmpty()) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
        Text(text = text, style = bodyMedium.copy(color = White))
    }
}

@Preview(showBackground = true)
@Composable
private fun ButtonPrimaryPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ButtonPrimary(text = "Submit", icon = null)
        ButtonPrimary(text = "Submit", icon = null, enabled = false)
    }
}
