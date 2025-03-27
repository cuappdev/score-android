package com.cornellappdev.score.nav.root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.cornellappdev.score.R
import com.cornellappdev.score.nav.ScoreNavHost
import com.cornellappdev.score.nav.ScoreNavigationBar
import com.cornellappdev.score.nav.root.ScoreScreens.GameDetailsPage
import com.cornellappdev.score.nav.root.ScoreScreens.Home
import com.cornellappdev.score.nav.root.ScoreScreens.ScoresScreen
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
        ScoreNavigationBar({ navController.navigate(it) }, navBackStackEntry)
    }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            ScoreNavHost(navController)
        }
    }
}


@Serializable
sealed class ScoreScreens {
    @Serializable
    data object Home : ScoreScreens()

    @Serializable
    data class GameDetailsPage(val gameId: String) : ScoreScreens()

    @Serializable
    data object ScoresScreen : ScoreScreens()
}

fun NavBackStackEntry.toScreen(): ScoreScreens? =
    when (destination.route?.substringAfterLast(".")?.substringBefore("/")) {
        "Home" -> toRoute<Home>()
        "GameDetailsPage" -> toRoute<GameDetailsPage>()
        "ScoresScreen" -> toRoute<ScoresScreen>()
        else -> throw IllegalArgumentException("Invalid screen")
    }

data class NavItem(
    val screen: ScoreScreens,
    val label: String,
    val unselectedIcon: Int,
    val selectedIcon: Int
)

val tabs = listOf(
    NavItem(
        label = "Schedule",
        unselectedIcon = R.drawable.ic_schedule,
        selectedIcon = R.drawable.ic_schedule_filled,
        screen = ScoreScreens.Home,
    ),
    NavItem(
        label = "Scores",
        unselectedIcon = R.drawable.ic_scores,
        selectedIcon = R.drawable.ic_scores_filled,
        screen = ScoreScreens.ScoresScreen,
    ),
)