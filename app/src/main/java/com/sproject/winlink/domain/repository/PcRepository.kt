package com.sproject.winlink.domain.repository

import com.sproject.winlink.common.util.Resource
import com.sproject.winlink.domain.model.FileItem
import com.sproject.winlink.domain.model.MediaInfos
import com.sproject.winlink.domain.model.PcInfos
import kotlinx.coroutines.flow.Flow

interface PcRepository {

    suspend fun getMediaState(): Flow<Resource<MediaInfos>>
    suspend fun getPcInfos(): Flow<Resource<PcInfos>>
    suspend fun getFiles(path: String): Flow<Resource<List<FileItem>>>

    suspend fun saveLastConnectedPc(pc: PcInfos)
    suspend fun getLastConnectedPc(): Flow<Resource<PcInfos?>>

    fun getFileDownloadUrl(file: FileItem): String
}
