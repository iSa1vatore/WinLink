package com.sproject.winlink.presentation.screens.tabs.media

import com.sproject.winlink.domain.model.MediaInfos

sealed class MediaState {
    object Loading : MediaState()
    data class Success(val info: MediaInfos) : MediaState()
    data class Error(val message: String) : MediaState()
}
