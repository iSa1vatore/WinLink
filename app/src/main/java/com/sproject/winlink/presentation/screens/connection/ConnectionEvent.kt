package com.sproject.winlink.presentation.screens.connection

import com.sproject.winlink.domain.model.PcInfos

sealed class ConnectionEvent {

    data class ConnectedToPc(
        val pcInfos: PcInfos
    ) : ConnectionEvent()

    object PcConnectionError : ConnectionEvent()
}
