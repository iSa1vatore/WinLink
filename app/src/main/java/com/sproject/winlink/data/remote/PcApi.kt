package com.sproject.winlink.data.remote

import com.sproject.winlink.data.remote.dto.FileItemDto
import com.sproject.winlink.data.remote.dto.MediaInfosDto
import com.sproject.winlink.data.remote.dto.PcInfosDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*

class PcApi(
    private val client: HttpClient
) {

    private lateinit var baseUrl: String

    fun initSession(ip: String, port: Int = 15110) {
        baseUrl = "http://$ip:$port"
    }

    fun getBaseUrl() = baseUrl

    suspend fun getMediaState(): MediaInfosDto = call("/media/state")

    suspend fun getPcInfos(): PcInfosDto = call("/pc/info")

    suspend fun getFiles(path: String): List<FileItemDto> = call(
        method = "/files/get",
        params = mapOf(
            "path" to path
        )
    )

    suspend fun deleteFile(path: String): Int = call(
        method = "/files/delete",
        params = mapOf(
            "path" to path
        )
    )

    private suspend inline fun <reified T> call(
        method: String,
        params: Map<String, String>? = null
    ): T = client.get(baseUrl + method) {
        url {
            params?.forEach { (key, value) ->
                parameters.append(key, value)
            }
        }
    }.body()
}
