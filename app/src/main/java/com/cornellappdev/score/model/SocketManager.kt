package com.cornellappdev.score.model

import android.util.Log
import com.cornellappdev.score.BuildConfig
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.json.JSONObject
import java.util.Collections
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "SocketManager"

@Singleton
class SocketManager @Inject constructor(private val appScope: CoroutineScope) {

    private val _gameUpdateFlow = MutableSharedFlow<SocketGameUpdateEnvelope>(extraBufferCapacity = 16)
    val gameUpdateFlow: SharedFlow<SocketGameUpdateEnvelope> = _gameUpdateFlow.asSharedFlow()

    private val activeSubscriptions: MutableSet<String> = Collections.synchronizedSet(mutableSetOf())

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    private val socket: Socket by lazy {
        val opts = IO.Options.builder()
            .setTransports(arrayOf("websocket"))
            .build()
        IO.socket(BuildConfig.SOCKET_URL, opts).also { s ->
            s.on(Socket.EVENT_CONNECT) {
                Log.d(TAG, "Connected")
                activeSubscriptions.forEach { id ->
                    s.emit("subscribe", JSONObject().put("gameId", id))
                }
            }
            s.on(Socket.EVENT_DISCONNECT) { args -> Log.d(TAG, "Disconnected: ${args.firstOrNull()}") }
            s.on(Socket.EVENT_CONNECT_ERROR) { args -> Log.e(TAG, "Error: ${args.firstOrNull()}") }
            s.on("game_update") { args ->
                val raw = args.firstOrNull() as? JSONObject ?: return@on
                runCatching { json.decodeFromString<SocketGameUpdateEnvelope>(raw.toString()) }
                    .onSuccess { appScope.launch { _gameUpdateFlow.emit(it) } }
                    .onFailure { Log.e(TAG, "Parse error: $it") }
            }
            s.connect()
        }
    }

    fun subscribe(gameId: String) {
        activeSubscriptions.add(gameId)
        socket.emit("subscribe", JSONObject().put("gameId", gameId))
    }

    fun unsubscribe(gameId: String) {
        activeSubscriptions.remove(gameId)
        socket.emit("unsubscribe", JSONObject().put("gameId", gameId))
    }
}
