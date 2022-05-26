package com.sproject.winlink.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ScreenInfoDto(
    val index: Int,
    val manufacturer: String,
    val brightness: Int
)
