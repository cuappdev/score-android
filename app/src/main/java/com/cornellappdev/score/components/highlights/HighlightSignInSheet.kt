package com.cornellappdev.score.components.highlights

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cornellappdev.score.R
import com.cornellappdev.score.components.ButtonPrimary
import com.cornellappdev.score.theme.GrayLight
import com.cornellappdev.score.theme.GrayMedium
import com.cornellappdev.score.theme.GrayPrimary
import com.cornellappdev.score.theme.GrayStroke
import com.cornellappdev.score.theme.Style.bodyMedium
import com.cornellappdev.score.theme.Style.heading1
import com.cornellappdev.score.theme.Style.labelsNormal
import com.cornellappdev.score.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HighlightSignInSheet(onDismiss: () -> Unit) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        containerColor = White,
        scrimColor = Color.Transparent,
        dragHandle = null
    ) {
        HighlightSignInSheetContent(onDismiss = onDismiss)
    }
}

@Composable
internal fun HighlightSignInSheetContent(
    onDismiss: () -> Unit,
    // Account navigation will be connected once the settings/sign-in work is merged.
    onSignIn: () -> Unit = {},
    onCreateAccount: () -> Unit = {}
) {
    Box(
        Modifier
            .fillMaxWidth()
            .heightIn(min = 398.dp)
            .border(1.dp, GrayStroke, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
    ) {
        Surface(
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 15.dp).size(32.dp, 4.dp),
            shape = RoundedCornerShape(4.dp),
            color = GrayLight
        ) {}
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(Modifier.fillMaxWidth().height(24.dp), contentAlignment = Alignment.CenterEnd) {
                IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = "Close sign in prompt",
                        tint = GrayMedium
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text("Sign In to Save", style = heading1, color = GrayPrimary)
            Spacer(Modifier.height(32.dp))
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ButtonPrimary(
                    text = "Sign In",
                    icon = null,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    onClick = onSignIn
                )
                OutlinedButton(
                    onClick = onCreateAccount,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(100.dp),
                    border = BorderStroke(1.dp, GrayLight),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = GrayPrimary)
                ) {
                    Text("Create an Account", style = bodyMedium)
                }
            }
            Spacer(Modifier.height(8.dp))
            TextButton(onClick = onDismiss) {
                Text("Not now", style = labelsNormal, color = GrayMedium)
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}
