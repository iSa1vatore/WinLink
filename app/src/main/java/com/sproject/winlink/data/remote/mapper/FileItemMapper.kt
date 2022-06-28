package com.sproject.winlink.data.remote.mapper

import com.sproject.winlink.data.remote.dto.FileItemDto
import com.sproject.winlink.domain.model.FileItem

fun FileItemDto.toFileItem(): FileItem {
    return FileItem(
        name = name,
        lastEdit = lastEdit,
        isFolder = isFolder,
        path = path,
        filesCount = filesCount
    )
}
