package com.sproject.winlink.data.remote.socket_actions

import kotlinx.serialization.Serializable

class Sound {

    @Serializable
    data class SetVolume(val value: Int) {
        companion object {
            const val ACTION_NAME = "sound/setValue"
        }
    }
}
