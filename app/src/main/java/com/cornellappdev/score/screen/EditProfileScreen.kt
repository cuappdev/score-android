package com.cornellappdev.score.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.score.R
import com.cornellappdev.score.theme.BorderDark
import com.cornellappdev.score.theme.CrimsonPrimary
import com.cornellappdev.score.theme.GrayLight
import com.cornellappdev.score.theme.GrayMedium
import com.cornellappdev.score.theme.GrayPrimary
import com.cornellappdev.score.theme.Style
import com.cornellappdev.score.theme.Wash
import com.cornellappdev.score.theme.White

@Composable
fun EditProfileScreen(onBackClick: () -> Unit) {
    var name by remember { mutableStateOf("Audrey Wu") }
    var username by remember { mutableStateOf("audreywuu") }
    var currentAvatar by remember {
        mutableStateOf(AvatarOption(R.drawable.pingpong_profile))
    }
    var showAvatarPicker by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        EditProfileContent(
            name = name,
            onNameChange = { name = it },
            username = username,
            onUsernameChange = { username = it },
            currentAvatar = currentAvatar,
            onBackClick = onBackClick,
            onEditPhotoClick = { showAvatarPicker = true },
            onSaveClick = { /* Handle save */ }
        )

        if (showAvatarPicker) {
            AvatarPickerBottomSheet(
                onDismiss = { showAvatarPicker = false },
                onAvatarSelected = {
                    currentAvatar = it
                    showAvatarPicker = false
                }
            )
        }
    }
}

@Composable
fun EditProfileContent(
    name: String,
    onNameChange: (String) -> Unit,
    username: String,
    onUsernameChange: (String) -> Unit,
    currentAvatar: AvatarOption,
    onBackClick: () -> Unit,
    onEditPhotoClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 4.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = "Edit profile",
                style = Style.heading2.copy(
                    fontSize = 18.sp,
                    color = GrayPrimary
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        HorizontalDivider(color = Wash)
        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .border(width = 0.81.dp, color = BorderDark, shape = CircleShape)
                    .clip(CircleShape)
                    .background(White),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = currentAvatar.imageRes),
                    contentDescription = "Profile photo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Edit photo",
                style = Style.heading6.copy(color = CrimsonPrimary),
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onEditPhotoClick() }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Name",
                    style = Style.heading6
                )

                BasicTextField(
                    value = name,
                    onValueChange = onNameChange,
                    textStyle = Style.heading5.copy(
                        color = GrayPrimary,
                        textAlign = TextAlign.End
                    ),
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Username",
                    style = Style.heading6
                )

                BasicTextField(
                    value = username,
                    onValueChange = onUsernameChange,
                    textStyle = Style.heading5.copy(
                        color = GrayPrimary,
                        textAlign = TextAlign.End
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onSaveClick,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 60.dp)
                .width(138.dp)
                .height(46.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CrimsonPrimary,
                disabledContainerColor = GrayLight
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "Save",
                style = Style.title.copy(
                    color = White,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvatarPickerBottomSheet(
    onDismiss: () -> Unit,
    onAvatarSelected: (AvatarOption) -> Unit
) {
    var selectedAvatar by remember { mutableStateOf<AvatarOption?>(null) }

    val avatars = listOf(
        AvatarOption(R.drawable.upload),
        AvatarOption(R.drawable.pingpong_profile),
        AvatarOption(R.drawable.soccer_profile),
        AvatarOption(R.drawable.tennis_profile),
        AvatarOption(R.drawable.badminton_profile),
        AvatarOption(R.drawable.billiards_profile),
        AvatarOption(R.drawable.basketball_profile),
        AvatarOption(R.drawable.bowling_profile),
        AvatarOption(R.drawable.volleyball_profile),
        AvatarOption(R.drawable.golf_profile),
        AvatarOption(R.drawable.weightlifting_profile),
        AvatarOption(R.drawable.baseball_profile)
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = White,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .border(1.dp, GrayLight, CircleShape)
                    .clip(CircleShape)
                    .clickable { onDismiss() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    modifier = Modifier.size(16.dp),
                    tint = GrayMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AvatarIconGrid(
                avatars = avatars,
                selectedAvatar = selectedAvatar,
                onAvatarSelected = { selectedAvatar = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    selectedAvatar?.let(onAvatarSelected)
                },
                enabled = selectedAvatar != null,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
                    .width(138.dp)
                    .height(46.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CrimsonPrimary,
                    disabledContainerColor = GrayLight
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Save",
                    style = Style.title.copy(
                        color = White,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }


            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun AvatarIconGrid(
    avatars: List<AvatarOption>,
    selectedAvatar: AvatarOption?,
    onAvatarSelected: (AvatarOption) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(avatars) { avatar ->
            val isSelected = selectedAvatar == avatar

            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .clickable { onAvatarSelected(avatar) },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = avatar.imageRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = White.copy(alpha = 0.45f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            modifier = Modifier.size(40.dp),
                            tint = GrayPrimary
                        )
                    }
                }
            }
        }
    }
}
// TODO: support uploaded profile photos later
data class AvatarOption(
    val imageRes: Int
)
@Preview(showBackground = true)
@Composable
private fun EditProfileScreenPreview() {
    MaterialTheme {
        EditProfileScreen(onBackClick = {})
    }
}