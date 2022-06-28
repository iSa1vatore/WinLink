package com.sproject.winlink.data.remote.socket_actions

import kotlinx.serialization.Serializable

class Keyboard {

    @Serializable
    data class Press(val key: String) {
        companion object {
            const val ACTION_NAME = "keyboard/press"
        }
    }
}
