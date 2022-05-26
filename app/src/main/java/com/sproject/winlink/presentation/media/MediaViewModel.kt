package com.sproject.winlink.presentation.media

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sproject.winlink.common.util.Resource
import com.sproject.winlink.domain.model.SoundInfo
import com.sproject.winlink.domain.use_case.GetMediaInfosUseCase
import com.sproject.winlink.domain.use_case.SoundSetValueUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MediaViewModel @Inject constructor(
    private val getMediaInfosUseCase: GetMediaInfosUseCase,
    private val soundSetValueUseCase: SoundSetValueUseCase
) : ViewModel() {

    private val _state = MutableLiveData(MediaState())
    val state = _state

    init {
        viewModelScope.launch {
            getMediaInfosUseCase().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.value = MediaState(info = result.data)
                    }
                    is Resource.Loading -> {
                        _state.value = MediaState(isLoading = true)
                    }
                    is Resource.Error -> {
                        _state.value = MediaState(
                            error = result.message ?: "Unknown error"
                        )
                    }
                }
            }
        }
    }

    fun soundSetVolume(value: Int) {
        val currentInfo = _state.value?.info ?: return

        _state.value = MediaState(
            info = currentInfo.copy(soundInfo = SoundInfo(value))
        )

        viewModelScope.launch {
            soundSetValueUseCase(value)
        }
    }
}
