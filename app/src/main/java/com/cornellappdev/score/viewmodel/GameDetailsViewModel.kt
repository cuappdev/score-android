package com.cornellappdev.score.viewmodel

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.cornellappdev.score.model.ApiResponse
import com.cornellappdev.score.model.DetailsCardData
import com.cornellappdev.score.model.ScoreRepository
import com.cornellappdev.score.model.map
import com.cornellappdev.score.model.toGameCardData
import com.cornellappdev.score.nav.root.ScoreScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

data class GameDetailsUiState(
    val loadedState: ApiResponse<DetailsCardData>
)

@HiltViewModel
class GameDetailsViewModel @Inject constructor(
    private val scoreRepository: ScoreRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<GameDetailsUiState>(
    initialUiState = GameDetailsUiState(
        loadedState = ApiResponse.Loading
    )
) {
    private val gameId: String = checkNotNull(savedStateHandle["gameId"])

    init {
        asyncCollect(scoreRepository.currentGamesFlow) { response ->
            applyMutation {
                copy(
                    loadedState = response.map { gameCard ->
                        gameCard.toGameCardData()
                    }
                )
            }
        }
        onRefresh()
    }

    fun onRefresh() {
        applyMutation { copy(loadedState = ApiResponse.Loading) }
        scoreRepository.getGameById(gameId)
    }

    fun addGameToCalendar(context: Context, gameCard: DetailsCardData) {
        val date = gameCard.date ?: return
        val time = gameCard.time

        val startDateTime = try {
            val formatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH)
            val localTime = LocalTime.parse(time.trim().uppercase().replace(".", ""), formatter)
            date.atTime(localTime)
        } catch (e: Exception) {
            Log.e("Calendar", "Failed to parse time: '$time'", e)
            date.atStartOfDay()
        }

        val zoneId = ZoneId.systemDefault()
        val startMillis = startDateTime.atZone(zoneId).toInstant().toEpochMilli()
        val endMillis = startDateTime.plusHours(2).atZone(zoneId).toInstant().toEpochMilli()

        val intent = Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI).apply {
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
            putExtra(CalendarContract.Events.TITLE, gameCard.title)
            putExtra(CalendarContract.Events.EVENT_LOCATION, gameCard.locationString)
            putExtra(CalendarContract.Events.DESCRIPTION, "${gameCard.sport} - ${gameCard.gender}")
        }
        context.startActivity(intent)
    }
}