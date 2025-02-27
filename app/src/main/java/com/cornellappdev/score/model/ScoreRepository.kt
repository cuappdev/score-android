package com.cornellappdev.score.model

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
import com.example.rocketreserver.TeamsQuery
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This is a sample repository demonstrating how to use GraphQL with Apollo Client in an Android project.
 * This repository is for testing only and should be deleted in the future.
 *
 * Note: `TeamsQuery` is an auto-generated class by Apollo GraphQL. It is generated from the `Teams.graphql`
 * file and should not be manually modified.
 */


@Singleton
class ScoreRepository @Inject constructor(
    private val apolloClient: ApolloClient
) {
    suspend fun getTeams(): ApolloResponse<TeamsQuery.Data> {
        return apolloClient.query(TeamsQuery()).execute()
    }
}
