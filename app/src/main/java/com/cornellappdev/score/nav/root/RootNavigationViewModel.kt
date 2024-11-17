package com.cornellappdev.score.nav.root

import com.cornellappdev.score.viewmodel.BaseViewModel
import com.cornellappdev.score.util.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RootNavigationViewModel @Inject constructor(
    rootNavigationRepository: RootNavigationRepository,
) : BaseViewModel<RootNavigationViewModel.RootNavigationUiState>(
    initialUiState = RootNavigationUiState()
) {

    data class RootNavigationUiState(
        val navigationEvent: UIEvent<ScoreRootScreens>? = null,
    )

    init {
        asyncCollect(rootNavigationRepository.navigationFlow) {
            applyMutation {
                copy(navigationEvent = it)
            }
        }
    }
}
