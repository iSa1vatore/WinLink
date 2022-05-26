package com.sproject.winlink.domain.use_case

import com.sproject.winlink.common.util.Resource
import com.sproject.winlink.domain.model.MediaInfos
import com.sproject.winlink.domain.repository.PcRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetMediaInfosUseCase @Inject constructor(
    private val pcRepository: PcRepository
) {
    suspend operator fun invoke(): Flow<Resource<MediaInfos>> {
        return pcRepository.getMediaState()
    }
}
