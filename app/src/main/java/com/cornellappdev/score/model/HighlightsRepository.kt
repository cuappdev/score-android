package com.cornellappdev.score.model

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.example.score.HighlightsQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

private const val TIMEOUT_TIME_MILLIS = 5000L

@Singleton
class HighlightsRepository @Inject constructor(
    private val apolloClient: ApolloClient,
    private val appScope: CoroutineScope,
) {
    private val _highlightsFlow =
        MutableStateFlow<ApiResponse<List<HighlightData>>>(ApiResponse.Loading)
    val highlightsFlow = _highlightsFlow.asStateFlow()


    /**
     * Asynchronously fetches the list of highlights from the API. Once finished, will send down
     * `upcomingGamesFlow` to be observed.
     */
    fun fetchHighlights() = appScope.launch {
        _highlightsFlow.value = ApiResponse.Loading
        try {
            val result =
                withTimeout(TIMEOUT_TIME_MILLIS) {
                    apolloClient.query(HighlightsQuery()).execute().toResult()
                }

            if (result.isSuccess) {
                val highlights = result.getOrNull()
                val highlightsList =
                    highlights?.articles.orEmpty().mapNotNull { it?.toHighlightData() } +
                            highlights?.youtubeVideos.orEmpty().mapNotNull { it?.toHighlightData() }

                _highlightsFlow.value =
                    ApiResponse.Success(highlightsList)

            } else {
                _highlightsFlow.value = ApiResponse.Error
            }

        } catch (e: TimeoutCancellationException) {
            Log.e("HighlightsRepository", "Highlights request timed out", e)
            _highlightsFlow.value = ApiResponse.Error
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("HighlightsRepository", "Error fetching highlights", e)
            _highlightsFlow.value = ApiResponse.Error
        }
    }

}