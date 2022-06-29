package com.sproject.winlink.presentation.screens.tabs.files

import com.sproject.winlink.domain.model.FileItem

sealed class FilesEvent {

    data class DownloadFile(
        val name: String,
        val url: String
    ) : FilesEvent()

    data class FileRemoved(
        val file: FileItem,
    ) : FilesEvent()
}
