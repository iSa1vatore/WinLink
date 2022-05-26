package com.sproject.winlink.data.remote.socket_actions

import kotlinx.serialization.Serializable

class Mouse {

    @Serializable
    data class Move(val x: Int, val y: Int) {
        companion object {
            const val ACTION_NAME = "mouse/move"
        }
    }

    @Serializable
    data class Click(val button: Int) {
        companion object {
            const val ACTION_NAME = "mouse/click"
        }
    }
}
