package com.cornellappdev.score.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.cornellappdev.score.R
import com.cornellappdev.score.api.GameApiRepository
import com.cornellappdev.score.model.ApiResponse
import com.cornellappdev.score.model.Game
import com.cornellappdev.score.model.GameCardData
import com.cornellappdev.score.model.GenderDivision
import com.cornellappdev.score.model.ScoreRepository
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.model.SportSelection
import com.cornellappdev.score.nav.root.RootNavigationRepository
import com.cornellappdev.score.util.sportSelectionList
import com.example.score.GamesQuery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Month

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val rootNavigationRepository: RootNavigationRepository,
    private val gameApiRepository: GameApiRepository,
    private val scoreRepository: ScoreRepository
) : BaseViewModel<HomeViewModel.HomeUiState>(
    initialUiState = HomeUiState(
        selectedGender = GenderDivision.ALL,
        sportSelect = SportSelection.All,
        selectionList = sportSelectionList,
        upcomingGameList = emptyList()
    )
) {
    data class HomeUiState(
        val selectedGender: GenderDivision,
        val sportSelect: SportSelection,
        val selectionList: List<SportSelection>,
        val upcomingGameList: List<GameCardData>,
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

    //    fun onRefresh() {
//        gameApiRepository.fetchUpcomingGames()
//    }
    fun onRefresh() {
        viewModelScope.launch {
            scoreRepository.fetchGames()
        }
    }

    private fun formatDate(strDate: String): LocalDate? {
        val monthMap = mapOf(
            "Jan" to Month.JANUARY,
            "Feb" to Month.FEBRUARY,
            "Mar" to Month.MARCH,
            "Apr" to Month.APRIL,
            "May" to Month.MAY,
            "Jun" to Month.JUNE,
            "Jul" to Month.JULY,
            "Aug" to Month.AUGUST,
            "Sep" to Month.SEPTEMBER,
            "Oct" to Month.OCTOBER,
            "Nov" to Month.NOVEMBER,
            "Dec" to Month.DECEMBER
        )

        val parts = strDate.split(" ")
        if (parts.size < 2) return null // Ensure there's at least a month and day

        val month = monthMap[parts[0]]
        val day = parts[1].toIntOrNull() ?: return null // Convert day to Int safely

        val currentYear = LocalDate.now().year // Use current year to construct a valid date
        return LocalDate.of(currentYear, month, day)
    }

    private fun formatColor(color: String) : Int{
            val alpha = (40 * 255 / 100)// Convert percent to hex (0-255)
            val colorInt = Integer.parseInt(color.removePrefix("#"), 16)
            return (alpha shl 24) or colorInt
    }


    
    init {
        asyncCollect(scoreRepository.upcomingGamesFlow) { response ->
            Log.d("HomeViewModel", "Response: $response")
            val games: List<Game> = when (response) {
                is ApiResponse.Success -> {
                    response.data
                }

                ApiResponse.Error -> emptyList()
                ApiResponse.Loading -> emptyList()
            }
            Log.d("viewModel", "size: ${games.size}")
            val gameCards = games.filter { game ->
                val currentDate = LocalDate.now()
                val tomorrowDate = LocalDate.now().plusDays(1)
                val formattedDate = formatDate(game.date)
                formattedDate == currentDate || formattedDate == tomorrowDate //i'm understanding upcoming as today and tomorrow's games
            }.map { game ->
                GameCardData(
                    teamLogo = game.teamLogo,
                    team = game.teamName,
                    teamColor = formatColor(game.teamColor),
                    date = formatDate(game.date).toString(),
                    location = game.city,
                    genderIcon = if (game.gender == "Mens") {
                        R.drawable.ic_gender_men
                    } else R.drawable.ic_gender_women,
                    sportIcon = Sport.fromDisplayName(game.sport)?.emptyIcon ?: R.drawable.ic_empty_placeholder//R.drawable.ic_baseball //TODO: icon logic
                )
            }.sortedBy { LocalDate.parse(it.date) }
            applyMutation {
                copy(
                    upcomingGameList = gameCards
                )
            }
        }
        onRefresh()
    }
}