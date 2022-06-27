package com.sproject.winlink.data.remote.socket_actions

import kotlinx.serialization.Serializable

class Screen {

    class Off {
        companion object {
            const val ACTION_NAME = "screen/off"
        }
    }

    @Serializable
    data class SetBrightness(val monitor: Int, val value: Int) {
        companion object {
            const val ACTION_NAME = "mouse/setBrightness"
        }
    }
}
