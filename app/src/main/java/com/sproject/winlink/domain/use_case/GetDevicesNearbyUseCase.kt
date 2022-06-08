package com.sproject.winlink.domain.use_case


import com.sproject.winlink.common.util.NetworkUtils
import com.sproject.winlink.common.util.Resource
import com.sproject.winlink.data.remote.PcApi
import com.sproject.winlink.domain.model.PcInfos
import javax.inject.Inject

class GetDevicesNearbyUseCase @Inject constructor(
    private val networkUtils: NetworkUtils,
    private val pcApi: PcApi,
) {
    suspend operator fun invoke(emit: (res: Resource<List<PcInfos>>) -> Unit) {
        var devices = mutableListOf<PcInfos>()

        networkUtils.getClients { ipAddress ->
            pcApi.initSession("http://$ipAddress:15110")

            try {
                val result = pcApi.getPcInfos()

                devices = ArrayList(devices)
                devices.add(
                    PcInfos(
                        userName = result.userName,
                        name = result.name,
                        address = ipAddress,
                        port = 15110
                    )
                )

                emit(Resource.Loading(devices))
            } catch (e: Exception) {
            }
        }

        emit(Resource.Success(devices))
    }
}