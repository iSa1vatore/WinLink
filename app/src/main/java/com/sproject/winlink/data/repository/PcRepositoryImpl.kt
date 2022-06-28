package com.sproject.winlink.data.repository

import com.sproject.winlink.common.util.Resource
import com.sproject.winlink.data.local.DataStoreService
import com.sproject.winlink.data.remote.PcApi
import com.sproject.winlink.data.remote.mapper.toFileItem
import com.sproject.winlink.data.remote.mapper.toMediaInfos
import com.sproject.winlink.data.remote.mapper.toPcInfos
import com.sproject.winlink.domain.model.FileItem
import com.sproject.winlink.domain.model.MediaInfos
import com.sproject.winlink.domain.model.PcInfos
import com.sproject.winlink.domain.repository.PcRepository
import java.io.IOException
import java.net.URLEncoder
import kotlinx.coroutines.flow.*

class PcRepositoryImpl(
    private val api: PcApi,
    private val dataStoreService: DataStoreService
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
            val pcInfos = api.getPcInfos().toPcInfos()

            emit(Resource.Success(pcInfos))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message))
        }
    }

    override suspend fun getFiles(path: String): Flow<Resource<List<FileItem>>> = flow {
        emit(Resource.Loading())

        try {
            val files = api.getFiles(path)

            emit(Resource.Success(files.map { it.toFileItem() }))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message))
        }
    }

    override suspend fun saveLastConnectedPc(pc: PcInfos) =
        dataStoreService.saveLastConnectedPc(pc)

    override suspend fun getLastConnectedPc(): Flow<Resource<PcInfos?>> = flow {
        emit(Resource.Loading())

        try {
            val pcInfos = dataStoreService.getLastConnectedPc()

            if (pcInfos != null) emit(
                Resource.Success(pcInfos)
            ) else emit(
                Resource.Error(message = "no device")
            )
        } catch (e: IOException) {
            emit(Resource.Error(message = e.message))
        }
    }

    override fun getFileDownloadUrl(file: FileItem): String {
        val path = file.path.dropLast(1)

        val query: String = URLEncoder.encode(path, "utf-8")

        return "${api.getBaseUrl()}/files/download?path=$query"
    }
}
