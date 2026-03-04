package com.cornellappdev.score.screen
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.score.components.GamesCarousel
import com.cornellappdev.score.util.gameList

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navigateToEditProfile: () -> Unit = {}
) {
    val bookmarkedGames = gameList.take(4)
    val recommendedGames = gameList.takeLast(4)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Bookmarks",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${bookmarkedGames.size} Results",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Icon(
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        GamesCarousel(
            games = bookmarkedGames,
            onClick = { gameId -> /* handle navigation */ }
        )

        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Games You Might Like",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${recommendedGames.size} Results",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Icon(
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        GamesCarousel(
            games = recommendedGames,
            onClick = { gameId -> /* handle navigation */ }
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
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notifications",
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )

            Icon(
                imageVector = Icons.Outlined.Menu,
                contentDescription = "Menu",
                tint = Color.Black,
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
        Box(
            modifier = Modifier
                .size(120.dp)
                .border(
                    width = 0.81.dp,
                    color = Color(0xFF1D353E),
                    shape = CircleShape
                )
                .clip(CircleShape)
                .background(Color(0xFF7EDAFF)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🏓",
                fontSize = 72.sp,
                lineHeight = 72.sp
            )
        }

        Spacer(modifier = Modifier.width(24.dp))

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = "Audrey Wu",
                color = Color(0xFF333333),
                fontSize = 18.sp,
                fontWeight = FontWeight.W600,
                lineHeight = 18.sp,
                letterSpacing = 0.sp
            )
            Text(
                text = "@audreywuu",
                color = Color(0xFF333333),
                fontSize = 18.sp,
                fontWeight = FontWeight.W500,
                lineHeight = 18.sp,
                letterSpacing = 0.sp
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
            containerColor = Color.White,
            contentColor = Color(0xFF333333)
        ),
        border = BorderStroke(1.dp, Color(0xFFD1D1D1)),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
        modifier = modifier
            .width(97.dp)
            .height(32.dp)
    ) {
        Text(
            text = "Edit profile",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.sp
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
