package com.cornellappdev.score.viewmodel

import com.cornellappdev.score.model.GenderDivision
import com.cornellappdev.score.model.SportSelection
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel<HomeViewModel.HomeUiState>(
    initialUiState = HomeUiState(
        selectedGender = GenderDivision.ALL,
        sportSelect = SportSelection.All
    )
) {

    data class HomeUiState(
        val selectedGender: GenderDivision,
        val sportSelect: SportSelection
    )
}
