package com.cornellappdev.score.di

import com.apollographql.apollo.ApolloClient
import com.cornellappdev.score.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApolloClient(): ApolloClient = ApolloClient.Builder()
        .serverUrl(BuildConfig.BASE_URL)
        .build()
}