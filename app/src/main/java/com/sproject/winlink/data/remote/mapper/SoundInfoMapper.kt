package com.sproject.winlink.data.remote.mapper

import com.sproject.winlink.data.remote.dto.SoundInfoDto
import com.sproject.winlink.domain.model.SoundInfo

fun SoundInfoDto.toSoundInfo(): SoundInfo {
    return SoundInfo(
        volume = volume
    )
}
