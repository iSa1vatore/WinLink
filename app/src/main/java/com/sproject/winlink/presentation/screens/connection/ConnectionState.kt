package com.sproject.winlink.presentation.screens.connection

import com.sproject.winlink.domain.model.PcInfos
import com.sproject.winlink.presentation.utils.RecyclerViewAdapter

sealed class ConnectionState {
    data class NearbyDevicesLoaded(
        val devices: List<RecyclerViewAdapter.Item<PcInfos>> = emptyList()
    ) : ConnectionState()

    data class FetchingDevicesNearby(
        val devices: List<RecyclerViewAdapter.Item<PcInfos>> = emptyList()
    ) : ConnectionState()

    data class ConnectingToPc(
        val pcInfos: PcInfos
    ) : ConnectionState()

}