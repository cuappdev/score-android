package com.cornellappdev.score.nav.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.cornellappdev.score.screen.GameDetailsScreen
import com.cornellappdev.score.screen.HomeScreen
import kotlinx.serialization.Serializable

@Composable
fun RootNavigation(
    rootNavigationViewModel: RootNavigationViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    val uiState = rootNavigationViewModel.collectUiStateValue()

    LaunchedEffect(uiState.navigationEvent) {
        uiState.navigationEvent?.consumeSuspend { screen ->
            navController.navigate(screen)
        }
    }

    NavHost(
        navController = navController,
        startDestination = ScoreRootScreens.Home
    ) {
        composable<ScoreRootScreens.Home> {
            HomeScreen(navigate = { id ->
                navController.navigate(ScoreRootScreens.GameDetailPage(id))
            })
        }

        composable<ScoreRootScreens.GameDetailPage> {navBackStackEntry ->
            val id = navBackStackEntry.toRoute<ScoreRootScreens.GameDetailPage>().gameId
            GameDetailsScreen(id)
        }

        composable<ScoreRootScreens.Onboarding> {

        }
    }
}


@Serializable
sealed class ScoreRootScreens {
    @Serializable
    data object Home : ScoreRootScreens()

    @Serializable
    data class GameDetailPage(val gameId: String) : ScoreRootScreens()

    @Serializable
    data object Onboarding : ScoreRootScreens()

}
