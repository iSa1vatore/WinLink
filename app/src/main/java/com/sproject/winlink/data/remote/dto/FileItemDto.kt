package com.sproject.winlink.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FileItemDto(
    val name: String,

    @SerialName("files_count")
    val filesCount: Int,

    @SerialName("last_edit")
    val lastEdit: Int,

    @SerialName("is_folder")
    val isFolder: Boolean,

    val path: String
)
