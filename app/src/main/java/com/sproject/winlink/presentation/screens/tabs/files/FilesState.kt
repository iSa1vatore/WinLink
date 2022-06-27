package com.sproject.winlink.presentation.screens.tabs.files

import com.sproject.winlink.domain.model.FileItem
import com.sproject.winlink.presentation.utils.RecyclerViewAdapter

data class FilesState(
    var isLoading: Boolean = true,
    var files: List<RecyclerViewAdapter.Item<FileItem>> = emptyList()
)
