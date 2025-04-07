package com.cornellappdev.score.nav.root

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cornellappdev.score.screen.HomeScreen
import com.cornellappdev.score.util.LocalInfiniteLoading
import kotlinx.serialization.Serializable

@Composable
fun RootNavigation(
    rootNavigationViewModel: RootNavigationViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    val uiState = rootNavigationViewModel.collectUiStateValue()

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

    CompositionLocalProvider(
        LocalInfiniteLoading provides animatedValue
    ) {
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
