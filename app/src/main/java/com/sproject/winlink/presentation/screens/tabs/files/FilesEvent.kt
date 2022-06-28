package com.sproject.winlink.presentation.screens.tabs.files

sealed class FilesEvent {

    data class DownloadFile(
        val name: String,
        val url: String
    ) : FilesEvent()
}
