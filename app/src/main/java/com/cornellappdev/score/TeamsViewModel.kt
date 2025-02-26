package com.cornellappdev.score

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornellappdev.score.model.ScoreRepository
import com.cornellappdev.score.model.Team
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

//sealed class TeamsViewState{
//    data object Loading: TeamsViewState()
//    data class Loaded(
//        val name: String?,
//        val icon: String?
//    ): TeamsViewState()
//    data object Error: TeamsViewState()
//}
//
//@HiltViewModel
//class TeamsViewModel @Inject constructor(
//    private val scoreRepository : ScoreRepository
//) : ViewModel(){
//    private val _viewState = MutableStateFlow<TeamsViewState>(TeamsViewState.Loading)
//    val viewState = _viewState.asStateFlow()
//
//    fun getTeams() = viewModelScope.launch{
//        val teams = scoreRepository.getTeams()
//        teams.data.let{
//            _viewState.update{
//                TeamsViewState.Loaded(
//                    name = it,
//                    icon = it
//                )
//            }
//        }
//    }

sealed class TeamsViewState {
    data object Loading : TeamsViewState()
    data class Loaded(
        val teams: List<Team> // Store the list of teams
    ) : TeamsViewState()

    data object Error : TeamsViewState()
}

@HiltViewModel
class TeamsViewModel @Inject constructor(
    private val scoreRepository: ScoreRepository
) : ViewModel() {
    private val _viewState = MutableStateFlow<TeamsViewState>(TeamsViewState.Loading)
    val viewState = _viewState.asStateFlow()

    fun getTeams() = viewModelScope.launch {
        try {
            val response = scoreRepository.getTeams()
            val teamResponses = response.data?.teams ?: emptyList()


            val teams: List<Team> = teamResponses.mapNotNull { teamResponse ->
                if (teamResponse?.name != null && teamResponse.image != null) {
                    teamResponse.let {
                        Team(
                            name = teamResponse.name,
                            logo = (teamResponse.image).toInt()
                        )
                    }
                } else {
                    null //skips invalid data - problematic ?
                }
            }

            _viewState.update {
                if (teams.isNotEmpty()) {
                    TeamsViewState.Loaded(teams = teams) // Pass the list of teams
                } else {
                    TeamsViewState.Error // Handle empty response as an error
                }
            }
        } catch (e: Exception) {
            _viewState.update { TeamsViewState.Error } // Handle exceptions
        }
    }
}


//    /** Emits lists of all the [UpliftClass]es that should be shown in the today's classes section. */
//    val teamsFlow = scoreRepository.teamsApiFlow.map {apiResponse ->
//        when (apiResponse) {
//            ApiResponse.Loading -> ApiResponse.Loading
//            ApiResponse.Error -> ApiResponse.Error
//            is ApiResponse.Success -> ApiResponse.Success(apiResponse.data)
//        }.stateIn(
//            CoroutineScope(Dispatchers.Main), SharingStarted.Eagerly, ApiResponse.Loading
//        )
//    }
//    }
//    init {
//        fetchTeams()
//    }
//
//    private fun fetchTeams() {
//        viewModelScope.launch {
//            try {
//                val response = apolloClient
//                    .query(TeamsQuery())
//                    .execute()
//
//                _teams.value = response.data // ✅ Extracts data from the response
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
//}