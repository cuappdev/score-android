package com.cornellappdev.score.nav.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
            HomeScreen()
        }

        composable<ScoreRootScreens.GameDetailPage> {

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
    data object GameDetailPage : ScoreRootScreens()

    @Serializable
    data object Onboarding : ScoreRootScreens()

}
