package com.sproject.winlink.data.remote.mapper

import com.sproject.winlink.data.remote.dto.ScreenInfoDto
import com.sproject.winlink.domain.model.ScreenInfo

fun ScreenInfoDto.toScreenInfo(): ScreenInfo {
    return ScreenInfo(
        index = index,
        manufacturer = manufacturer,
        brightness = brightness
    )
}
