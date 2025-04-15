package com.cornellappdev.score.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cornellappdev.score.nav.root.ScoreScreens
import com.cornellappdev.score.nav.root.ScoreScreens.Home
import com.cornellappdev.score.screen.GameDetailsScreen
import com.cornellappdev.score.screen.HomeScreen
import com.cornellappdev.score.screen.PastGamesScreen

@Composable
fun ScoreNavHost(navController: NavHostController) {
    // This ViewModelStoreOwner is used to scope the past and home screen view models to the root
    //  screen instead of their individual tabs. This way the view models are not reconstructed
    //  everytime you switch tabs.
    val mainScreenViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current)

    NavHost(
        navController = navController,
        startDestination = Home
    ) {
        composable<Home> {
            CompositionLocalProvider(LocalViewModelStoreOwner provides mainScreenViewModelStoreOwner) {
                HomeScreen(navigateToGameDetails = {
                    navController.navigate(ScoreScreens.GameDetailsPage(it))
                })
            }
        }
        composable<ScoreScreens.ScoresScreen> {
            CompositionLocalProvider(LocalViewModelStoreOwner provides mainScreenViewModelStoreOwner) {
                PastGamesScreen(navigateToGameDetails = {
                    navController.navigate(ScoreScreens.GameDetailsPage(it))
                })
            }
        }
        composable<ScoreScreens.GameDetailsPage> {
            GameDetailsScreen(onBackArrow = {
                navController.navigateUp()
            })
        }
    }
}