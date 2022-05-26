package com.sproject.winlink.data.remote

import com.sproject.winlink.common.util.Resource

interface PcSocketService {

    suspend fun initSession(
        url: String
    ): Resource<Unit>

    suspend fun mouseMove(x: Int, y: Int)

    suspend fun mouseClick(button: Int)

    suspend fun soundSetValue(value: Int)

    suspend fun closeSession()
}
