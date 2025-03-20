package com.cornellappdev.score.viewmodel

import android.util.Log
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class PastGamesUiState(
    val selectedGender: GenderDivision,
    val sportSelect: SportSelection,
    val selectionList: List<SportSelection>,
    val pastGameList: List<GameCardData>,
    // TODO Add remaining dynamic data for UI
) {
    val filteredGames: List<GameCardData>
        get() = pastGameList.filter { game ->
            (selectedGender == GenderDivision.ALL || game.gender == selectedGender.displayName)
                    && (sportSelect is SportSelection.All || (sportSelect is SportSelection.SportSelect && game.sport == sportSelect.sport.displayName))
        }
}

@HiltViewModel
class PastGamesViewModel @Inject constructor(
    private val rootNavigationRepository: RootNavigationRepository,
    private val scoreRepository: ScoreRepository
) : BaseViewModel<PastGamesUiState>(
    initialUiState = PastGamesUiState(
        selectedGender = GenderDivision.ALL,
        sportSelect = SportSelection.All,
        selectionList = Sport.getSportSelectionList(),
        pastGameList = emptyList()
    )
) {
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

        val pastGames = games.filter { game ->
            val gameDate = formatDate(game.date)
            gameDate != null && gameDate.isBefore(LocalDate.now()) // Only select past games
            game.cornellScore != null && game.otherScore != null
        }.map { game ->
            Log.d("VM Scores",   "sport: " + game.sport + " date: " + game.date + " cornell: " + game.cornellScore + "other: " + game.otherScore)
            GameCardData(
                teamLogo = game.teamLogo,
                team = game.teamName,
                teamColor = formatColor(game.teamColor),
                date = formatDate(game.date),
                dateString = dateToString(formatDate(game.date)),
                isLive = false, // Past games cannot be live
                location = game.city,
                gender = game.gender,
                genderIcon = if (game.gender == "Mens") R.drawable.ic_gender_men else R.drawable.ic_gender_women,
                sport = game.sport,
                sportIcon = Sport.fromDisplayName(game.sport)?.emptyIcon
                    ?: R.drawable.ic_empty_placeholder,
                cornellScore = game.cornellScore,
                otherScore = game.otherScore
            )
        }.sortedByDescending { it.date } // Sort past games by most recent

        applyMutation {
            copy(pastGameList = pastGames) // Rename to pastGameList if needed
        }
    }


//    fun onRefresh() {
//        viewModelScope.launch {
//            val response = scoreRepository.fetchGames()
//            updateGameList(response)
//        }
//    }

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
        return LocalDate.of(currentYear, month, day)
    }

    /**
     * Converts from format "#xxxxxx" to a valid hex, with alpha = 40. Ready to be passed into Color()
     */

    private fun formatColor(color: String): Int {
        val alpha = (40 * 255 / 100)// Convert percent to hex (0-255)
        val colorInt = Integer.parseInt(color.removePrefix("#"), 16)
        return (alpha shl 24) or colorInt
    }

    private fun dateToString(date: LocalDate?): String {
        if (date == null) {
            return "--"
        }
        return "${date.month.value}/${date.dayOfMonth}/${date.year}"
    }

    private fun observePastGames() = scoreRepository.upcomingGamesFlow.onEach { response ->
        updateGameList(response)
    }.launchIn(viewModelScope)

    init {
        observePastGames()
        viewModelScope.launch {
            scoreRepository.fetchGames()
        }
    }

}