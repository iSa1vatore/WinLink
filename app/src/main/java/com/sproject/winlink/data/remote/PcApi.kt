package com.sproject.winlink.data.remote

import com.sproject.winlink.data.remote.dto.MediaInfosDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class PcApi(
    private val client: HttpClient
) {

    suspend fun getMediaState(): MediaInfosDto {
        return client.get("http://192.168.1.39:15110/media/state").body()
    }
}
