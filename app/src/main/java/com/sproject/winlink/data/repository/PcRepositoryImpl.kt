package com.sproject.winlink.data.repository

import com.sproject.winlink.common.util.Resource
import com.sproject.winlink.data.remote.PcApi
import com.sproject.winlink.data.remote.mapper.toMediaInfos
import com.sproject.winlink.data.remote.mapper.toPcInfos
import com.sproject.winlink.domain.model.MediaInfos
import com.sproject.winlink.domain.model.PcInfos
import com.sproject.winlink.domain.repository.PcRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PcRepositoryImpl(
    private val api: PcApi
) : PcRepository {

    override suspend fun getMediaState(): Flow<Resource<MediaInfos>> = flow {
        emit(Resource.Loading())

        try {
            val mediaState = api.getMediaState().toMediaInfos()

            emit(Resource.Success(mediaState))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message))
        }
    }

    override suspend fun getPcInfos(): Flow<Resource<PcInfos>> = flow {
        emit(Resource.Loading())

        try {
            val mediaState = api.getPcInfos().toPcInfos()

            emit(Resource.Success(mediaState))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message))
        }
    }
}
