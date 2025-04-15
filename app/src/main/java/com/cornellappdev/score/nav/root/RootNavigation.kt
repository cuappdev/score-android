package com.cornellappdev.score.nav.root

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import com.cornellappdev.score.theme.LocalInfiniteLoading
import kotlinx.serialization.Serializable

@Composable
fun RootNavigation(
    rootNavigationViewModel: RootNavigationViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    val uiState = rootNavigationViewModel.collectUiStateValue()
    val navBackStackEntry = navController.currentBackStackEntryAsState().value

    val transition = rememberInfiniteTransition()
    // Animate a value from 0 to 1 infinitely
    val animatedValue = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = InfiniteRepeatableSpec(
            animation = keyframes {
                durationMillis = 2000
                0f at 0
                1f at 1000
                0f at 2000
            }
        ),
        label = "infinite loading"
    ).value

    LaunchedEffect(uiState.navigationEvent) {
        uiState.navigationEvent?.consumeSuspend { screen ->
            navController.navigate(screen)
        }
    }


    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
        if (navBackStackEntry?.toScreen() is GameDetailsPage) {
            return@Scaffold
        }
        ScoreNavigationBar({ navController.navigate(it) }, navBackStackEntry)
    }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            CompositionLocalProvider(LocalInfiniteLoading provides animatedValue) {

                ScoreNavHost(navController)
            }
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