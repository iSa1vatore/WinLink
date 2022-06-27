package com.sproject.winlink.data.remote

import com.sproject.winlink.data.remote.dto.MediaInfosDto
import com.sproject.winlink.data.remote.dto.PcInfosDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class PcApi(
    private val client: HttpClient
) {

    private lateinit var baseUrl: String

    fun initSession(ip: String, port: Int = 15110) {
        baseUrl = "http://$ip:$port"
    }

    suspend fun getMediaState(): MediaInfosDto = call("/media/state")

    suspend fun getPcInfos(): PcInfosDto = call("/pc/info")

    private suspend inline fun <reified T> call(method: String): T {
        return client.get("$baseUrl$method").body()
    }
}
