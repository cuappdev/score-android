package com.cornellappdev.score.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.score.R
import com.cornellappdev.score.components.ProfileGameCarousel
import com.cornellappdev.score.theme.GrayLight
import com.cornellappdev.score.theme.GrayPrimary
import com.cornellappdev.score.theme.Style
import com.cornellappdev.score.theme.White
import com.cornellappdev.score.util.gameList

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navigateToEditProfile: () -> Unit = {},
    navigateToGameDetails: (String) -> Unit = {}
) {
    val bookmarkedGames = gameList.take(4)
    val recommendedGames = gameList.takeLast(4)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
    ) {
        ProfileTopBar(
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        ProfileRow(
            modifier = Modifier.padding(horizontal = 20.dp),
            onEditClick = navigateToEditProfile
        )

        Spacer(modifier = Modifier.height(24.dp))

        ProfileGameCarousel(
            title = "Bookmarks",
            games = bookmarkedGames,
            onClick = { gameId -> navigateToGameDetails(gameId) }
        )

        ProfileGameCarousel(
            title = "Games You Might Like",
            games = recommendedGames,
            onClick = { gameId -> navigateToGameDetails(gameId) }
        )
    }
}

@Composable
fun ProfileTopBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Profile",
            style = Style.title.copy(
                fontSize = 26.sp,
                color = GrayPrimary
            )
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notifications",
                tint = GrayPrimary,
                modifier = Modifier.size(24.dp)
            )

            Icon(
                imageVector = Icons.Outlined.Menu,
                contentDescription = "Menu",
                tint = GrayPrimary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun ProfileRow(
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.pingpong_profile),
            contentDescription = "Profile image",
            modifier = Modifier.size(100.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(24.dp))

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = "Audrey Wu",
                style = Style.heading6
            )
            Text(
                text = "@audreywuu",
                style = Style.heading5.copy(
                    color = GrayPrimary
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            EditProfileButton(onClick = onEditClick)
        }
    }
}

@Composable
fun EditProfileButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(100.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = White,
            contentColor = GrayPrimary
        ),
        border = BorderStroke(1.dp, GrayLight),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
        modifier = modifier
            .width(97.dp)
            .height(32.dp)
    ) {
        Text(
            text = "Edit profile",
            style = Style.labelsMedium.copy(
                color = GrayPrimary
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    MaterialTheme {
        ProfileScreen()
    }
}