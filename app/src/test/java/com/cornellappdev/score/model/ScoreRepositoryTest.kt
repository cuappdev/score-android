package com.cornellappdev.score.model

import com.apollographql.apollo.ApolloClient
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import okhttp3.mockwebserver.Dispatcher
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ScoreRepositoryTest {
    private lateinit var server: MockWebServer
    private lateinit var client: ApolloClient
    private lateinit var scope: CoroutineScope
    private lateinit var repository: ScoreRepository
    private val requests = AtomicInteger()
    @Volatile private var fail = false
    @Volatile private var empty = false
    private var historyStarted: CountDownLatch? = null
    private var releaseHistory: CountDownLatch? = null

    @Before fun setup() {
        server = MockWebServer()
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                val body = request.body.readUtf8()
                requests.incrementAndGet()
                val field = if (body.contains("InitialGames")) "gamesByDate" else "games"
                if (field == "games") {
                    historyStarted?.countDown()
                    releaseHistory?.await(2, TimeUnit.SECONDS)
                }
                val games = if (empty) "[]" else """[{"__typename":"GameType","id":"1","city":"Ithaca","date":"2026-09-23","gender":"Mens","result":null,"sport":"Baseball","time":null,"team":{"__typename":"TeamType","name":"Opponent","image":"https://example.com/logo.png","color":"#FFFFFF"}}]"""
                val response = if (fail) """{"errors":[{"message":"Unavailable"}]}"""
                    else """{"data":{"$field":$games}}"""
                return MockResponse().setHeader("Content-Type", "application/json").setBody(response)
            }
        }
        server.start()
        client = ApolloClient.Builder().serverUrl(server.url("/").toString()).build()
        scope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)
        repository = ScoreRepository(client, scope)
    }

    @After fun teardown() {
        releaseHistory?.countDown()
        scope.cancel()
        client.close()
        server.shutdown()
    }

    @Test fun failedRefreshRestoresPreviousGames() = runBlocking {
        withTimeout(10000) {
            repository.fetchGames().join()
            val previous = repository.upcomingGamesFlow.value
            assertTrue(previous is ApiResponse.Success)
            fail = true
            repository.fetchGames().join()
            assertEquals(previous, repository.upcomingGamesFlow.value)
        }
    }

    @Test fun successfulEmptyRefreshClearsPreviousGames() = runBlocking {
        withTimeout(10000) {
            repository.fetchGames().join()
            empty = true
            repository.fetchGames().join()
            assertEquals(ApiResponse.Success(emptyList<Game>()), repository.upcomingGamesFlow.value)
        }
    }

    @Test fun refreshDuringHistoryFetchWaitsAndRuns() = runBlocking {
        withTimeout(10000) {
            historyStarted = CountDownLatch(1)
            releaseHistory = CountDownLatch(1)
            val first = repository.fetchGames()
            assertTrue(historyStarted!!.await(2, TimeUnit.SECONDS))
            val refresh = repository.fetchGames()
            assertFalse(refresh.isCompleted)
            releaseHistory!!.countDown()
            first.join()
            refresh.join()
            assertEquals(4, requests.get())
            assertTrue(repository.upcomingGamesFlow.value is ApiResponse.Success)
        }
    }

    @Test fun firstLoadFailureShowsError() = runBlocking {
        withTimeout(10000) {
            fail = true
            repository.fetchGames().join()
            assertEquals(ApiResponse.Error, repository.upcomingGamesFlow.value)
        }
    }
}
