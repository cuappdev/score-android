package com.cornellappdev.score.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object Style {
    val title = TextStyle(
        fontFamily = poppinsFamily,
        fontSize = 24.sp,
        fontWeight = FontWeight(600),
        color = Color(0xFF333333)
    )

    val vsText = TextStyle(
        fontSize = 24.sp,
        fontFamily = poppinsFamily,
        fontWeight = FontWeight(700),
        fontStyle = FontStyle.Italic,
        color = Color.White
    )

    val universityText = TextStyle(
        fontSize = 12.sp,
        fontFamily = poppinsFamily,
        fontWeight = FontWeight(400),
        color = Color(0xFF777777)
    )

    val teamName = TextStyle(
        fontSize = 18.sp,
        fontFamily = poppinsFamily,
        fontWeight = FontWeight(500),
        color = Color(0xFF333333)
    )

    val scoreText = TextStyle(
        fontSize = 18.sp,
        fontFamily = poppinsFamily,
        fontWeight = FontWeight(400),
        color = Color(0xFF333333)
    )

    val genderFilterText = TextStyle(
        fontSize = 16.sp,
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Bold,
        color = Color(0xFFFFFFFF)
    )

    // Gender filter (Other) text style
    val genderText = TextStyle(
        fontSize = 16.sp,
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Normal,
        color = Color(0xFF333333)
    )

    // Sport filter text style
    val sportFilterText = TextStyle(
        fontSize = 12.sp,
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Normal,
        color = Color(0xFF333333),
        textAlign = TextAlign.Center
    )

    // Date text style
    val dateText = TextStyle(
        fontSize = 12.sp,
        fontFamily = poppinsFamily,
        fontWeight = FontWeight(400),
        color = Color(0xFF777777)
    )

    val scoreHeaderText = TextStyle(
        fontSize = 40.sp,
        fontFamily = poppinsFamily,
        fontWeight = FontWeight(600),
        color = White,
        shadow = Shadow(
            color = Color.Black.copy(alpha = 0.30f),
            offset = Offset(0f, 0f),
            blurRadius = 4.0F
        )

    )

    val countdownNumberText = TextStyle(
        fontSize = 36.sp,
        fontFamily = poppinsFamily,
        fontWeight = FontWeight(600),
        color = GrayPrimary
    )

    val heading1 = TextStyle(
        fontFamily = poppinsFamily,
        fontSize = 24.sp,
        fontWeight = FontWeight(600)
    )

    val heading2 = TextStyle(
        fontFamily = poppinsFamily,
        fontSize = 18.sp,
        fontWeight = FontWeight(500)
    )

    val heading3 = TextStyle(
        fontFamily = poppinsFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight(500)
    )

    val heading4 = TextStyle(
        fontFamily = poppinsFamily,
        fontSize = 12.sp,
        fontWeight = FontWeight(500)
    )

    val heading5 = TextStyle(
        fontFamily = poppinsFamily,
        fontSize = 18.sp,
        fontWeight = FontWeight(400)
    )

    val heading6 = TextStyle(
        fontSize = 18.sp,
        fontFamily = poppinsFamily,
        fontWeight = FontWeight(600),
        color = Color(0xFF333333)
    )

    val bodyLight = TextStyle(
        fontFamily = poppinsFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight(300)
    )

    val bodyNormal = TextStyle(
        fontSize = 14.sp,
        fontFamily = poppinsFamily,
        fontWeight = FontWeight(400)
    )

    val spanBodyNormal = SpanStyle(
        fontSize = 14.sp,
        fontFamily = poppinsFamily,
        fontWeight = FontWeight(400),
    )

    val bodyMedium = TextStyle(
        fontSize = 14.sp,
        fontFamily = poppinsFamily,
        fontWeight = FontWeight(500)
    )

    val bodySemibold = TextStyle(
        fontSize = 14.sp,
        fontFamily = poppinsFamily,
        fontWeight = FontWeight(600)
    )

    val bodyBold = TextStyle(
        fontSize = 14.sp,
        fontFamily = poppinsFamily,
        fontWeight = FontWeight(700)
    )

    val labelLight = TextStyle(
        fontSize = 12.sp,
        fontFamily = poppinsFamily,
        fontWeight = FontWeight(300)
    )

    val labelNormal = TextStyle(
        fontSize = 12.sp,
        fontFamily = poppinsFamily,
        fontWeight = FontWeight(400)
    )

    val labelMedium = TextStyle(
        fontSize = 12.sp,
        fontFamily = poppinsFamily,
        fontWeight = FontWeight(500)
    )
}
