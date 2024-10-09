package com.cornellappdev.score.theme
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

object Style {

    /**
     * The style for Location text.
     */
    val universityText = TextStyle(
        fontSize = 12.sp,
        fontFamily = poppinsFamily,
        fontWeight = FontWeight(400),
        color = Color(0xFF333333)
    )

    // Team name text style
    val teamName = TextStyle(
        fontSize = 20.sp,
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF333333)
    )

    // Sport name text style
    val sportName = TextStyle(
        fontSize = 12.sp,
        fontFamily = poppinsFamily,
        fontWeight = FontWeight(400),
        color = Color(0xFF333333)
    )

    // Gender filter (Chosen) text style
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
        fontSize = 14.sp,
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF333333)
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
