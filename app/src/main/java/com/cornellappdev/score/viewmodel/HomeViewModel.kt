package com.cornellappdev.score.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.cornellappdev.score.R
import com.cornellappdev.score.model.ApiResponse
import com.cornellappdev.score.model.Game
import com.cornellappdev.score.model.GameCardData
import com.cornellappdev.score.model.GenderDivision
import com.cornellappdev.score.model.ScoreRepository
import com.cornellappdev.score.model.Sport
import com.cornellappdev.score.model.SportSelection
import com.cornellappdev.score.nav.root.RootNavigationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val rootNavigationRepository: RootNavigationRepository,
    private val scoreRepository: ScoreRepository
) : BaseViewModel<HomeViewModel.HomeUiState>(
    initialUiState = HomeUiState(
        selectedGender = GenderDivision.ALL,
        sportSelect = SportSelection.All,
        selectionList = Sport.getSportSelectionList(),//sportSelectionList,
        upcomingGameList = emptyList()
    )
) {
    data class HomeUiState(
        val selectedGender: GenderDivision,
        val sportSelect: SportSelection,
        val selectionList: List<SportSelection>,
        val upcomingGameList: List<GameCardData>,
        // TODO Add remaining dynamic data for UI
    ) {
        val filteredGames: List<GameCardData>
            get() = upcomingGameList.filter { game ->
                (selectedGender == GenderDivision.ALL || game.gender == selectedGender.displayName)
                        && (sportSelect is SportSelection.All || (sportSelect is SportSelection.SportSelect && game.sport == sportSelect.sport.displayName))
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

    private fun updateGameList(response: ApiResponse<List<Game>>) {
        val games: List<Game> = when (response) {
            is ApiResponse.Success -> response.data
            ApiResponse.Error -> emptyList()
            ApiResponse.Loading -> emptyList()
        }

        val gameCards = games.filter { game ->
            val currentDate = LocalDate.now()
            val tomorrowDate = LocalDate.now().plusDays(1)
            val formattedDate = formatDate(game.date)
            formattedDate == currentDate || formattedDate == tomorrowDate
        }.map { game ->
            GameCardData(
                teamLogo = game.teamLogo,
                team = game.teamName,
                teamColor = formatColor(game.teamColor),
                date = formatDate(game.date),
                dateString = dateToString(formatDate(game.date)),
                isLive = (LocalDate.now() == formatDate(game.date)),
                location = game.city,
                gender = game.gender,
                genderIcon = if (game.gender == "Mens") R.drawable.ic_gender_men else R.drawable.ic_gender_women,
                sport = game.sport,
                sportIcon = Sport.fromDisplayName(game.sport)?.emptyIcon
                    ?: R.drawable.ic_empty_placeholder
            )
        }.sortedBy { it.date }

        applyMutation {
            copy(upcomingGameList = gameCards)
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            val response = scoreRepository.fetchGames()
            updateGameList(response)
        }
    }

    //Converts date from String "month day" to a LocalDate object
    private fun formatDate(strDate: String): LocalDate? {
        val monthMap = mapOf(
            "Jan" to 1,
            "Feb" to 2,
            "Mar" to 3,
            "Apr" to 4,
            "May" to 5,
            "Jun" to 6,
            "Jul" to 7,
            "Aug" to 8,
            "Sep" to 9,
            "Oct" to 10,
            "Nov" to 11,
            "Dec" to 12
        )

        val parts = strDate.split(" ")
        if (parts.size < 2) return null

        val month = monthMap[parts[0]]
        if (month == null) {
            return null
        }
        val day = parts[1].toIntOrNull() ?: return null

        val currentYear = LocalDate.now().year
        //Log.d("HomeViewModel", "formatDate: ${LocalDate.of(currentYear, month, day)}")
        return LocalDate.of(currentYear, month, day)
    }

    //Converts from format "#xxxxxx" to a valid hex, with alpha = 40. Ready to be passed into Color()
    private fun formatColor(color: String): Int {
        val alpha = (40 * 255 / 100)// Convert percent to hex (0-255)
        val colorInt = Integer.parseInt(color.removePrefix("#"), 16)
        return (alpha shl 24) or colorInt
    }

    private fun dateToString(date: LocalDate?): String {
        if (date == null) {
            return "--"
        }
        //Log.d("HomeViewModel", "formatedDate: ${date.month.value}/${date.dayOfMonth}/${date.year}")
        return "${date.month.value}/${date.dayOfMonth}/${date.year}"
    }

    init {
        asyncCollect(scoreRepository.upcomingGamesFlow) { response ->
            Log.d("HomeViewModel", "Response: $response")
            updateGameList(response)
        }
        onRefresh()
    }
}