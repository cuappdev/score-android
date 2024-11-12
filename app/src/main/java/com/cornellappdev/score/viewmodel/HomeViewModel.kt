package com.cornellappdev.score.viewmodel

import com.cornellappdev.score.model.GenderDivision
import com.cornellappdev.score.model.SportSelection
import com.cornellappdev.score.nav.root.RootNavigationRepository
import com.cornellappdev.score.util.sportSelectionList
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val rootNavigationRepository: RootNavigationRepository
) : BaseViewModel<HomeViewModel.HomeUiState>(
    initialUiState = HomeUiState(
        selectedGender = GenderDivision.ALL,
        sportSelect = SportSelection.All,
        selectionList = sportSelectionList
    )
) {

    data class HomeUiState(
        val selectedGender: GenderDivision,
        val sportSelect: SportSelection,
        val selectionList: List<SportSelection>,
        // TODO Add remaining dynamic data for UI
    )

    fun onGenderSelected(gender: GenderDivision) {
        applyMutation {
            copy(
                selectedGender = gender
            )
        }
    }

    fun onSportSelected(sport: SportSelection) {
        applyMutation {
            copy(
                sportSelect = sport
            )
        }
    }
}
