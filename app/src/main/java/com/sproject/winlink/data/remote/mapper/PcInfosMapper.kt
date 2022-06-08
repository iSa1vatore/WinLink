package com.sproject.winlink.data.remote.mapper

import com.sproject.winlink.data.remote.dto.PcInfosDto
import com.sproject.winlink.domain.model.PcInfos

fun PcInfosDto.toPcInfos(): PcInfos {
    return PcInfos(
        name = name,
        userName = userName,
        address = address,
        port = port
    )
}