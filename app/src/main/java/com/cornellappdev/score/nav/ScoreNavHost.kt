package com.cornellappdev.score.nav

import GameScoreSummaryScreenDetail
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.cornellappdev.score.model.ProfileData
import com.cornellappdev.score.screen.ProfileScreen
import com.cornellappdev.score.screen.HighlightsScreen
import com.cornellappdev.score.screen.HighlightsSearchScreen
import com.cornellappdev.score.viewmodel.OnboardingSequenceViewModel
import com.cornellappdev.score.model.ScoreEvent
import com.cornellappdev.score.nav.root.ScoreScreens
import com.cornellappdev.score.nav.root.ScoreScreens.Home
import com.cornellappdev.score.screen.GameDetailsScreen
import com.cornellappdev.score.screen.HomeScreen
import com.cornellappdev.score.screen.PastGamesScreen
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

@Composable
fun ScoreNavHost(navController: NavHostController, welcomeName: String? = null) {
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
                HomeScreen(
                    welcomeName = welcomeName,
                    navigateToGameDetails = {
                        navController.navigate(ScoreScreens.GameDetailsPage(it))
                    },
                    navigateToProfile = {
                        navController.navigate(ScoreScreens.ProfileScreen) { launchSingleTop = true }
                    }
                )
            }
        }
        composable<ScoreScreens.ScoresScreen> {
            CompositionLocalProvider(LocalViewModelStoreOwner provides mainScreenViewModelStoreOwner) {
                PastGamesScreen(
                    navigateToGameDetails = {
                        navController.navigate(ScoreScreens.GameDetailsPage(it))
                    },
                    navigateToProfile = {
                        navController.navigate(ScoreScreens.ProfileScreen) { launchSingleTop = true }
                    }
                )
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
        composable<ScoreScreens.HighlightsScreen> {
            HighlightsScreen(
                toSearchScreen = { navController.navigate(ScoreScreens.HighlightsSearchScreen) }
            )
        }
        composable<ScoreScreens.HighlightsSearchScreen> {
            HighlightsSearchScreen(
                sportList = emptyList(),
                recentSearchList = emptyList(),
                highlightsList = emptyList(),
                query = "",
                header = "Search all highlights",
                onItemClick = {},
                onCloseClick = { navController.navigateUp() }
            )
        }
        composable<ScoreScreens.ProfileScreen> {
            val viewModel: OnboardingSequenceViewModel = hiltViewModel()
            ProfileScreen(
                data = ProfileData(
                    name = welcomeName?.takeIf { it.isNotBlank() } ?: "Guest",
                    favorites = viewModel.getFavoriteSports(),
                    bookmarks = emptyList()
                ),
                initialPicture = viewModel.getProfilePicture(),
                onPictureSelected = { viewModel.saveProfilePicture(it.name) },
                onFavoritesChanged = { viewModel.saveFavoriteSports(it.toSet()) },
                showBottomBar = false
            )
        }
    }
}
