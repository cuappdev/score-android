package com.cornellappdev.score.components.highlights

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import com.cornellappdev.score.R

@Composable
fun ExternalLink(
    url: String,
    urlLabel: String,
    linkColor: Color,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicText(
            text = buildAnnotatedString {
                withLink(
                    LinkAnnotation.Url(
                        url,
                        TextLinkStyles(
                            style = SpanStyle(
                                textDecoration = TextDecoration.Underline,
                                color = linkColor,
                                fontWeight = FontWeight.Bold
                            )
                        ),
                    )
                ) {
                    append(urlLabel)
                }
            }
        )
        Icon(
            painter = painterResource(R.drawable.arrow_outward),
            contentDescription = "external link arrow",
            tint = linkColor
        )
    }
}