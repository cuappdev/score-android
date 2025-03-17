package com.cornellappdev.score.viewmodel

import com.cornellappdev.score.R
import com.cornellappdev.score.model.ApiResponse
import com.cornellappdev.score.model.GameCardData
import com.cornellappdev.score.model.GenderDivision
import com.cornellappdev.score.model.ScoreRepository
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.model.SportSelection
import com.cornellappdev.score.nav.root.RootNavigationViewModel
import com.cornellappdev.score.util.dateToString
import com.cornellappdev.score.util.formatColor
import com.cornellappdev.score.util.parseDate
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

data class HomeUiState(
    val selectedGender: GenderDivision,
    val sportSelect: SportSelection,
    val selectionList: List<SportSelection>,
    val loadedState: ApiResponse<List<GameCardData>>
) {
    //TODO: refactor filters to use flows - not best practice to expose original games list to the view
    val filteredGames: List<GameCardData>
        get() = when (loadedState) {
            is ApiResponse.Success -> loadedState.data.filter { game ->
                (selectedGender == GenderDivision.ALL || game.gender == selectedGender.displayName) &&
                        (sportSelect is SportSelection.All || (sportSelect is SportSelection.SportSelect && game.sport == sportSelect.sport.displayName))
            }

            ApiResponse.Loading -> emptyList()
            ApiResponse.Error -> emptyList()
        }
    val upcomingGames: List<GameCardData> = filteredGames.take(3)
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val scoreRepository: ScoreRepository
) : BaseViewModel<HomeUiState>(
    HomeUiState(
        selectedGender = GenderDivision.ALL,
        sportSelect = SportSelection.All,
        selectionList = Sport.getSportSelectionList(),
        loadedState = ApiResponse.Loading
    )
) {
    init {
        scoreRepository.fetchGames()
        asyncCollect(scoreRepository.upcomingGamesFlow) { response ->
            applyMutation {
                copy(
                    loadedState = when (response) {
                        is ApiResponse.Success -> {
                            val gameCards = response.data
                                .map { game ->
                                    GameCardData(
                                        teamLogo = game.teamLogo,
                                        team = game.teamName,
                                        teamColor = formatColor(game.teamColor),
                                        date = parseDate(game.date),
                                        dateString = dateToString(parseDate(game.date)),
                                        isLive = (LocalDate.now() == parseDate(game.date)),
                                        location = game.city,
                                        gender = game.gender,
                                        genderIcon = if (game.gender == "Mens") R.drawable.ic_gender_men else R.drawable.ic_gender_women,
                                        sport = game.sport,
                                        sportIcon = Sport.fromDisplayName(game.sport)?.emptyIcon
                                            ?: R.drawable.ic_empty_placeholder
                                    )
                                }
                                .sortedBy { it.date }
                            ApiResponse.Success(gameCards)
                        }

                        ApiResponse.Loading -> ApiResponse.Loading
                        ApiResponse.Error -> ApiResponse.Error
                    }
                )
            }
        }
    }

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
