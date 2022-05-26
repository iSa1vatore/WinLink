package com.sproject.winlink.domain.model

data class FileItem(
    val name: String,
    val lastEdit: Long,
    val isFolder: Boolean,
    val path: String
)
