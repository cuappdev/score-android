package com.cornellappdev.score.nav

import GameScoreSummaryScreenDetail
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.cornellappdev.score.model.ScoreEvent
import com.cornellappdev.score.nav.root.ScoreScreens
import com.cornellappdev.score.nav.root.ScoreScreens.Home
import com.cornellappdev.score.screen.GameDetailsScreen
import com.cornellappdev.score.screen.HighlightsScreen
import com.cornellappdev.score.screen.HighlightsSearchScreen
import com.cornellappdev.score.screen.HomeScreen
import com.cornellappdev.score.screen.PastGamesScreen
import com.cornellappdev.score.util.highlightsList
import com.cornellappdev.score.util.recentSearchList
import com.cornellappdev.score.util.sportList
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

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
            GameDetailsScreen(
                onBackArrow = {
                    navController.navigateUp()
                },
                navigateToGameScoreSummary = { scoreEvents: List<ScoreEvent> ->
                    val scoreEventsJson =
                        Json.encodeToString(ListSerializer(ScoreEvent.serializer()), scoreEvents)
                    navController.navigate(ScoreScreens.GameScoreSummaryPage(scoreEventsJson))
                }
            )
        }

        composable<ScoreScreens.GameScoreSummaryPage> { backStackEntry ->
            val route = backStackEntry.toRoute<ScoreScreens.GameScoreSummaryPage>()
            val scoreEvents: List<ScoreEvent> = Json.decodeFromString(route.scoreEvents)
            GameScoreSummaryScreenDetail(scoreEvents = scoreEvents, onBackArrow = {
                navController.navigateUp()
            })
        }

        composable<ScoreScreens.HighlightsScreen> { backStackEntry ->
            CompositionLocalProvider(LocalViewModelStoreOwner provides mainScreenViewModelStoreOwner) {
                HighlightsScreen(toSearchScreen = { navController.navigate(ScoreScreens.HighlightsSearchScreen) })
            }
        }

        composable<ScoreScreens.HighlightsSearchScreen> { backStackEntry ->
            CompositionLocalProvider(LocalViewModelStoreOwner provides mainScreenViewModelStoreOwner) {
                HighlightsSearchScreen(
                    sportList = sportList,
                    recentSearchList = recentSearchList,
                    highlightsList = highlightsList,
                    query = "",
                    header = "Search all highlights",
                    {},
                    {}
                )
            }
        }
    }
}

