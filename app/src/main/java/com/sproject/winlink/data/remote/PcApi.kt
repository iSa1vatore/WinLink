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

    fun initSession(url: String) {
        baseUrl = url
    }

    suspend fun getMediaState(): MediaInfosDto {
        return call("/media/state")
    }

    suspend fun getPcInfos(): PcInfosDto {
        return call("/pc/info")
    }

    private suspend inline fun <reified T> call(method: String): T {
        return client.get("$baseUrl$method").body()
    }
}
