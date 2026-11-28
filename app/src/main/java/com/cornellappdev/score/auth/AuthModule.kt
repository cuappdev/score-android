package com.cornellappdev.score.auth

import com.apollographql.apollo.ApolloClient
import com.cornellappdev.score.BuildConfig
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    @Binds @Singleton abstract fun bindStorage(storage: EncryptedSessionStorage): SessionStorage

    @Binds @Singleton abstract fun bindApi(api: ApolloAuthApi): AuthApi

    companion object {
        @Provides
        @Singleton
        @Named("auth")
        fun provideAuthClient(): ApolloClient =
            ApolloClient.Builder().serverUrl(BuildConfig.BASE_URL).build()
    }
}
