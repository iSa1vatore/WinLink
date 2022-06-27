package com.sproject.winlink.data.remote

import com.sproject.winlink.common.constants.MediaAction
import com.sproject.winlink.common.constants.PowerAction
import com.sproject.winlink.common.util.Resource

interface PcSocketService {

    suspend fun initSession(
        url: String
    ): Resource<Unit>

    suspend fun mouseMove(x: Int, y: Int)

    suspend fun mouseScroll(x: Int, y: Int)

    suspend fun mouseClick(button: Int)

    suspend fun powerAction(action: PowerAction)

    suspend fun mediaAction(action: MediaAction)

    suspend fun screenOff()

    suspend fun screenSetBrightness(monitor: Int, value: Int)

    suspend fun soundSetValue(value: Int)

    suspend fun closeSession()
}
