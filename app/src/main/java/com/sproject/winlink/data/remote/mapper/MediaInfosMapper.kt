package com.sproject.winlink.data.remote.mapper

import com.sproject.winlink.data.remote.dto.MediaInfosDto
import com.sproject.winlink.domain.model.MediaInfos

fun MediaInfosDto.toMediaInfos(): MediaInfos {
    return MediaInfos(
        soundInfo = soundInfo.toSoundInfo(),
        screensInfo = screensInfo.map { it.toScreenInfo() }
    )
}
