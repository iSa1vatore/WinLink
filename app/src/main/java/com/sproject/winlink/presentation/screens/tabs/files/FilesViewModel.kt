package com.sproject.winlink.presentation.screens.tabs.files

import androidx.lifecycle.viewModelScope
import com.sproject.winlink.domain.model.FileItem
import com.sproject.winlink.presentation.base.BaseViewModelWithState
import com.sproject.winlink.presentation.utils.RecyclerViewAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class FilesViewModel @Inject constructor() : BaseViewModelWithState<FilesState>(
    initialState = FilesState()
) {

    init {
        getFiles("/")
    }

    fun getFiles(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            setState(
                FilesState(
                    isLoading = false,
                    files = listOf(
                        RecyclerViewAdapter.Item(
                            FileItem(
                                name = "OS (C:)", lastEdit = 1, isFolder = true, path = "C:\\"
                            )
                        ),
                        RecyclerViewAdapter.Item(
                            FileItem(
                                name = "HDD Data I (D:)",
                                lastEdit = 1,
                                isFolder = true,
                                path = "D:\\"
                            )
                        ),
                    )
                ),
                true
            )
        }
    }
}
