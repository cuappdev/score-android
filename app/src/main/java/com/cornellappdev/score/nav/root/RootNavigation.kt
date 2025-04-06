package com.cornellappdev.score.nav.root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.cornellappdev.score.R
import com.cornellappdev.score.nav.root.ScoreRootScreens.Home.toScreen
import com.cornellappdev.score.screen.GameDetailsScreen
import com.cornellappdev.score.screen.HomeScreen
import com.cornellappdev.score.screen.PastGamesScreen
import com.cornellappdev.score.theme.CrimsonPrimary
import com.cornellappdev.score.theme.GrayPrimary
import com.cornellappdev.score.theme.Style.bodyMedium
import com.cornellappdev.score.theme.White
import kotlinx.serialization.Serializable

@Composable
fun RootNavigation(
    rootNavigationViewModel: RootNavigationViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    val uiState = rootNavigationViewModel.collectUiStateValue()
    val navBackStackEntry = navController.currentBackStackEntryAsState().value

    LaunchedEffect(uiState.navigationEvent) {
        uiState.navigationEvent?.consumeSuspend { screen ->
            navController.navigate(screen)
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
        NavigationBar(containerColor = White) {
            tabs.map { item ->
                val isSelected = item.screen == navBackStackEntry?.toScreen()

                NavigationBarItem(
                    selected = isSelected,
                    onClick = { navController.navigate(item.screen) },
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
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = ScoreRootScreens.Home
            ) {
                composable<ScoreRootScreens.Home> {
                    HomeScreen(navigateToGameDetails = {
                        navController.navigate(ScoreRootScreens.GameDetailsPage("67e585b941adb1bafec37766"))
                    })
                }

                composable<ScoreRootScreens.GameDetailsPage> {
                    GameDetailsScreen("", onBackArrow = {
                        navController.navigateUp()
                    })

                }

                composable<ScoreRootScreens.ScoresScreen> {
                    PastGamesScreen(navigateToGameDetails = {
                        navController.navigate(ScoreRootScreens.GameDetailsPage(""))
                    })
                }
            }
        }
    }
}


@Serializable
sealed class ScoreRootScreens {
    @Serializable
    data object Home : ScoreRootScreens()

    @Serializable
    data class GameDetailsPage(val gameId: String) : ScoreRootScreens()

    @Serializable
    data object ScoresScreen : ScoreRootScreens()

    fun NavBackStackEntry.toScreen(): ScoreRootScreens? =
        when (destination.route?.substringAfterLast(".")?.substringBefore("/")) {
            "Home" -> toRoute<Home>()
            "GameDetailsPage" -> toRoute<GameDetailsPage>()
            "ScoresScreen" -> toRoute<ScoresScreen>()
            else -> throw IllegalArgumentException("Invalid screen")
        }
}

data class NavItem(
    val screen: ScoreRootScreens,
    val label: String,
    val unselectedIcon: Int,
    val selectedIcon: Int
)

val tabs = listOf(
    NavItem(
        label = "Schedule",
        unselectedIcon = R.drawable.ic_schedule,
        selectedIcon = R.drawable.ic_schedule_filled,
        screen = ScoreRootScreens.Home,
    ),
    NavItem(
        label = "Scores",
        unselectedIcon = R.drawable.ic_scores,
        selectedIcon = R.drawable.ic_scores_filled,
        screen = ScoreRootScreens.ScoresScreen,
    ),
)