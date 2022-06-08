package com.sproject.winlink.domain.use_case

import com.sproject.winlink.common.util.Resource
import com.sproject.winlink.data.remote.PcApi
import com.sproject.winlink.data.remote.PcSocketService
import com.sproject.winlink.domain.model.PcInfos
import javax.inject.Inject

class ConnectToPcUseCase @Inject constructor(
    private val pcApi: PcApi,
    private val pcSocketService: PcSocketService,
) {
    suspend operator fun invoke(pc: PcInfos): Resource<Unit> {
        pcApi.initSession("http://${pc.address}:${pc.port}")

        return pcSocketService.initSession("ws://${pc.address}:${pc.port}")
    }
}