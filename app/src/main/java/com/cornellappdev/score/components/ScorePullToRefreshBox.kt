package com.cornellappdev.score.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cornellappdev.score.theme.CrimsonPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScorePullToRefreshBox(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val state = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing, onRefresh, modifier,
        indicator = {
            Indicator(state, isRefreshing, color = CrimsonPrimary)
        }
    ) {
        content()
    }
}