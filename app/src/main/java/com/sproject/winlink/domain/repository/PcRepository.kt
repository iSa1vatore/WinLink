package com.sproject.winlink.domain.repository

import com.sproject.winlink.common.util.Resource
import com.sproject.winlink.domain.model.MediaInfos
import kotlinx.coroutines.flow.Flow

interface PcRepository {

    suspend fun getMediaState(): Flow<Resource<MediaInfos>>
}
