package com.sproject.winlink.data.remote.socket_actions

import kotlinx.serialization.Serializable

@Serializable
data class SocketAction<T>(val method: String, val data: T)
