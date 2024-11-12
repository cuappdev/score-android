package com.cornellappdev.score.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<UiState>(initialUiState: UiState) : ViewModel() {

    private val _uiStateFlow = MutableStateFlow(initialUiState)
    val uiStateFlow: StateFlow<UiState> = _uiStateFlow.asStateFlow()

    @Composable
    fun collectUiStateValue(): UiState = uiStateFlow.collectAsState().value

    /**
     * Applies a mutation to the current [UiState] and emits the new state.
     *
     * @param mutation A function that operates on the current [UiState] and returns a new [UiState].
     *
     * Most often, you'll want to `copy` the current state, changing just one of its properties,
     * and then emit the new state.
     */
    fun applyMutation(mutation: UiState.() -> UiState) {
        _uiStateFlow.value = _uiStateFlow.value.mutation()
    }

    /**
     * Asynchronously starts collecting the given flow.
     */
    fun <T> asyncCollect(flow: StateFlow<T>, collector: (T) -> Unit): Job {
        return viewModelScope.launch {
            flow.collect { collector(it) }
        }
    }
}
