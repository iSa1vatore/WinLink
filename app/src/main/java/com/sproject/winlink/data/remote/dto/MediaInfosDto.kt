package com.sproject.winlink.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MediaInfosDto(

    @SerialName("sound_info")
    val soundInfo: SoundInfoDto,

    @SerialName("screens_info")
    val screensInfo: List<ScreenInfoDto>,
)
