package com.sproject.winlink.presentation.media

import com.sproject.winlink.domain.model.MediaInfos

data class MediaState(
    val isLoading: Boolean = false,
    val info: MediaInfos? = null,
    val error: String = ""
)
