package com.cornellappdev.score.model

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
import com.example.score.TeamsQuery
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ScoreRepository @Inject constructor(
    private val apolloClient: ApolloClient
) {
    suspend fun getTeams(): ApolloResponse<TeamsQuery.Data> {
        return apolloClient.query(TeamsQuery()).execute()
    }
}

