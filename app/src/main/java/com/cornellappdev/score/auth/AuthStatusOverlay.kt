package com.cornellappdev.score.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.cornellappdev.score.R
import com.cornellappdev.score.theme.CrimsonPrimary

@Composable
fun AuthStatusOverlay(
    state: SignInState,
    onDismissError: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.busy) {
        Dialog(
            onDismissRequest = {},
            properties =
                DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false,
                    usePlatformDefaultWidth = false,
                ),
        ) {
            Box(
                modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = CrimsonPrimary)
            }
        }
    }
    state.error?.let { message ->
        AlertDialog(
            onDismissRequest = onDismissError,
            title = { Text(stringResource(R.string.auth_error_title)) },
            text = { Text(stringResource(message)) },
            confirmButton = {
                TextButton(onClick = onDismissError) { Text(stringResource(android.R.string.ok)) }
            },
        )
    }
}
