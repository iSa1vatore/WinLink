package com.sproject.winlink.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PcInfosDto(
    val name: String,
    @SerialName("user_name")
    val userName: String,
    val address: String,
    val port: Int
)
