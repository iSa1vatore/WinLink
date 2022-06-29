package com.sproject.winlink.presentation.screens.tabs.files

import androidx.lifecycle.viewModelScope
import com.sproject.winlink.common.util.Resource
import com.sproject.winlink.domain.model.FileItem
import com.sproject.winlink.domain.repository.PcRepository
import com.sproject.winlink.presentation.base.BaseViewModelWithStateAndAction
import com.sproject.winlink.presentation.utils.RecyclerViewAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class FilesViewModel @Inject constructor(
    private val pcRepository: PcRepository
) : BaseViewModelWithStateAndAction<FilesState, FilesEvent>(
    initialState = FilesState()
) {

    init {
        getFiles(state.value!!.path)
    }

    fun getFiles(path: String) =
        viewModelScope.launch(Dispatchers.IO) {
            pcRepository.getFiles(path).collect { it ->
                when (it) {
                    is Resource.Loading -> setState(
                        state.value!!.copy(isLoading = true),
                        true
                    )
                    is Resource.Success -> setState(
                        state.value!!.copy(
                            isLoading = false,
                            path = path,
                            files = it.data!!.map {
                                RecyclerViewAdapter.Item(it)
                            }
                        ),
                        true
                    )
                    is Resource.Error -> setState(
                        state.value!!.copy(isLoading = false, error = it.message),
                        true
                    )
                }
            }
        }

    fun downloadFile(file: FileItem) =
        viewModelScope.launch {
            val url = pcRepository.getFileDownloadUrl(file)

            sendEvent(FilesEvent.DownloadFile(name = file.name, url = url))
        }

    fun deleteFile(file: FileItem) =
        viewModelScope.launch {

            pcRepository.deleteFile(file.path).collect { it ->
                when (it) {
                    is Resource.Success -> {
                        val files = state.value!!.files

                        val newFilesList = files.filter { it.details != file }
                        setState(
                            state.value!!.copy(files = newFilesList),
                            true
                        )

                        sendEvent(FilesEvent.FileRemoved(file = file))
                    }
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                }
            }
        }
}
