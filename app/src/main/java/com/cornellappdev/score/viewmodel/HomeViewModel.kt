package com.cornellappdev.score.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.cornellappdev.score.R
import com.cornellappdev.score.api.GameApiRepository
import com.cornellappdev.score.model.ApiResponse
import com.cornellappdev.score.model.Game
import com.cornellappdev.score.model.GameCardData
import com.cornellappdev.score.model.GenderDivision
import com.cornellappdev.score.model.ScoreRepository
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
        viewModelScope.launch{
            scoreRepository.fetchGames()
        }
    }

    init {
        asyncCollect(scoreRepository.upcomingGamesFlow){ response ->
            Log.d("HomeViewModel", "Response: $response")
            val games: List<Game> = when(response){
                is ApiResponse.Success -> {
                    response.data
                }
                ApiResponse.Error -> emptyList()
                ApiResponse.Loading -> emptyList()
            }
            Log.d("viewModel", "size: ${games.size}")
            val gameCards = games.filter{game->
                game.date == "Mar 1 (Sat)"
            }.map { game ->
                GameCardData(
                    teamLogo = R.drawable.cornell_logo,// todo: load images from url,
                    team = game.teamName,
                    date = game.date,
                    location = game.city,
                    genderIcon = if(game.gender == "Mens"){R.drawable.ic_gender_men} else R.drawable.ic_gender_women ,
                    sportIcon = R.drawable.ic_baseball //TODO: icon logic
                )
            }
            applyMutation {
                copy(
                    upcomingGameList = gameCards
                )
            }
        }

        onRefresh()
    }

//    init {
////        asyncCollect(scoreRepository.upcomingGamesFlow) { response ->
////            val games: List<Game> = when(response){
////                is ApiResponse.Success -> {
////                    response.data.data
////                }
////
//////                is ApiResponse.Error -> emptyList<GamesQuery.Game>()
//////                ApiResponse.Loading -> emptyList<GamesQuery.Game>()
////                ApiResponse.Error -> TODO()
////                ApiResponse.Loading -> TODO()
////            }
////            applyMutation {
////                copy(
////                    upcomingGameList = response.data
////                )
////            }
////        }
//        asyncCollect(scoreRepository.upcomingGamesFlow) { response ->
//            val games: List<Game> = when(response){
//                is ApiResponse.Success -> {
//                    response.data
//                }
//                ApiResponse.Error -> emptyList()
//                ApiResponse.Loading -> emptyList()
//            }
//            val gameCards = games.mapNotNull{game ->
//                GameCardData(
//
//                    teamLogo = R.drawable.cornell_logo,// todo: load images from url,
//                    team = game.teamName,
//                    date = game.date,
//                    location = game.city,
//                    genderIcon = if(game.gender == "Mens"){R.drawable.ic_gender_men} else R.drawable.ic_gender_women ,
//                    sportIcon = R.drawable.ic_baseball //TODO: icon logic
//                )
//            }
//            applyMutation {
//                copy(
//                    upcomingGameList = gameCards
//                )
//            }
//        }
//
//        onRefresh()
//    }
}
