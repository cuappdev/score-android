package com.cornellappdev.score.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cornellappdev.score.theme.CrimsonPrimary
import com.cornellappdev.score.theme.White

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
        state = state,
        indicator = {
            Indicator(state, isRefreshing, color = CrimsonPrimary, containerColor = White)
        },
        contentAlignment = Alignment.TopCenter
    ) {
        content()
    }
}