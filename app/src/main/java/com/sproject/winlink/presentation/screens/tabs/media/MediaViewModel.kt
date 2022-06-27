package com.sproject.winlink.presentation.screens.tabs.media

import androidx.lifecycle.viewModelScope
import com.sproject.winlink.common.constants.MediaAction
import com.sproject.winlink.common.constants.PowerAction
import com.sproject.winlink.common.util.Resource
import com.sproject.winlink.data.remote.PcSocketService
import com.sproject.winlink.domain.repository.PcRepository
import com.sproject.winlink.presentation.base.BaseViewModelWithState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MediaViewModel @Inject constructor(
    private val pcRepository: PcRepository,
    private val pcSocketService: PcSocketService
) : BaseViewModelWithState<MediaState>(
    initialState = MediaState.Loading
) {

    init {
        viewModelScope.launch {
            pcRepository.getMediaState().collect { result ->
                when (result) {
                    is Resource.Success -> setState(
                        MediaState.Success(info = result.data!!)
                    )
                    is Resource.Loading -> setState(MediaState.Loading)
                    is Resource.Error -> setState(
                        MediaState.Error(
                            result.message ?: "Unknown error"
                        )
                    )
                }
            }
        }
    }

    fun setBrightness(value: Int) =
        viewModelScope.launch {
            pcSocketService.screenSetBrightness(0, value)
        }

    fun powerAction(action: PowerAction) =
        viewModelScope.launch {
            pcSocketService.powerAction(action)
        }

    fun mediaAction(action: MediaAction) =
        viewModelScope.launch {
            pcSocketService.mediaAction(action)
        }

    fun screenOff() =
        viewModelScope.launch {
            pcSocketService.screenOff()
        }

    fun soundSetVolume(value: Int) =
        viewModelScope.launch {
            pcSocketService.soundSetValue(value)
        }
}
