package com.sproject.winlink.domain.model

data class FileItem(
    val name: String,
    val lastEdit: Int,
    val isFolder: Boolean,
    val path: String,
    val filesCount: Int
)
