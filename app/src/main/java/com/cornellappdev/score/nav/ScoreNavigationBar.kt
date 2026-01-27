package com.cornellappdev.score.nav

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import com.cornellappdev.score.nav.root.ScoreScreens
import com.cornellappdev.score.nav.root.tabs
import com.cornellappdev.score.nav.root.toScreen
import com.cornellappdev.score.theme.CrimsonPrimary
import com.cornellappdev.score.theme.GrayPrimary
import com.cornellappdev.score.theme.Style.bodyMedium

@Composable
fun ScoreNavigationBar(
    navigateToScreen: (ScoreScreens) -> Unit,
    navBackStackEntry: NavBackStackEntry?,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier,
        containerColor = Color.Transparent
    ) {
        tabs.map { item ->
            val isSelected = item.screen == navBackStackEntry?.toScreen()

            NavigationBarItem(
                selected = isSelected,
                onClick = { navigateToScreen(item.screen) },
                colors = NavigationBarItemDefaults.colors().copy(
                    selectedIndicatorColor = Color.Transparent
                ),
                icon = {
                    Icon(
                        painter = painterResource(id = if (isSelected) item.selectedIcon else item.unselectedIcon),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = bodyMedium,
                        color = if (isSelected) {
                            CrimsonPrimary
                        } else {
                            GrayPrimary
                        }
                    )
                }
            )
        }
    }
}

@Preview
@Composable
private fun ScoreNavigationBarPreview() {
    ScoreNavigationBar({}, null)
}