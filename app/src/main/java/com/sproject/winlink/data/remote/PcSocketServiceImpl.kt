package com.sproject.winlink.data.remote

import com.sproject.winlink.common.constants.MediaAction
import com.sproject.winlink.common.constants.PowerAction
import com.sproject.winlink.common.util.Resource
import com.sproject.winlink.data.remote.dto.MediaInfosDto
import com.sproject.winlink.data.remote.mapper.toMediaInfos
import com.sproject.winlink.data.remote.socket_actions.Mouse
import com.sproject.winlink.data.remote.socket_actions.Screen
import com.sproject.winlink.data.remote.socket_actions.SocketAction
import com.sproject.winlink.data.remote.socket_actions.Sound
import com.sproject.winlink.domain.model.MediaInfos
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.* // ktlint-disable no-wildcard-imports
import kotlinx.coroutines.isActive
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class PcSocketServiceImpl(
    private val client: HttpClient
) : PcSocketService {

    private var socket: DefaultClientWebSocketSession? = null

    override suspend fun initSession(url: String): Resource<Unit> {
        return try {
            socket = client.webSocketSession {
                url(url)
            }

            if (socket?.isActive == true) {
                Resource.Success(Unit)
            } else Resource.Error("Не удалось установить соеднение")
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Неизвестная ошибка")
        }
    }

    override suspend fun mouseMove(x: Int, y: Int) {
        sendAction(Mouse.Move.ACTION_NAME, Mouse.Move(x, y))
    }

    override suspend fun mouseScroll(x: Int, y: Int) {
        sendAction(Mouse.Scroll.ACTION_NAME, Mouse.Scroll(x, y))
    }

    override suspend fun mouseClick(button: Int) {
        sendAction(Mouse.Click.ACTION_NAME, Mouse.Click(button))
    }

    override suspend fun powerAction(action: PowerAction) {
        sendAction("power/${action.toString().lowercase()}", Unit)
    }

    override suspend fun mediaAction(action: MediaAction) {
        sendAction("media/${action.toString().lowercase()}", Unit)
    }

    override suspend fun screenOff() {
        sendAction(Screen.Off.ACTION_NAME, Unit)
    }

    override suspend fun screenSetBrightness(monitor: Int, value: Int) {
        sendAction(
            Screen.SetBrightness.ACTION_NAME,
            Screen.SetBrightness(monitor, value)
        )
    }

    override suspend fun soundSetValue(value: Int) {
        sendAction(Sound.SetVolume.ACTION_NAME, Sound.SetVolume(value))
    }

    override suspend fun closeSession() {
        socket?.close()
    }

    fun observeMediaInfos(): Flow<MediaInfos> {
        return try {
            socket?.incoming
                ?.receiveAsFlow()
                ?.filter { it is Frame.Text }
                ?.map {
                    val json = (it as? Frame.Text)?.readText() ?: ""
                    val mediaInfos = Json.decodeFromString<MediaInfosDto>(json)
                    mediaInfos.toMediaInfos()
                } ?: flow { }
        } catch (e: Exception) {
            e.printStackTrace()
            flow { }
        }
    }

    private suspend inline fun <reified T> sendAction(name: String, data: T) {
        socket?.sendSerialized(SocketAction(name, data))
    }
}
