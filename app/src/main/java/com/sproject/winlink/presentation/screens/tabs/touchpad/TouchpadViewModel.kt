package com.sproject.winlink.presentation.screens.tabs.touchpad

import androidx.lifecycle.viewModelScope
import com.sproject.winlink.data.remote.PcSocketService
import com.sproject.winlink.presentation.base.BaseViewModelWithState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class TouchpadViewModel @Inject constructor(
    private val pcSocketService: PcSocketService
) : BaseViewModelWithState<TouchpadState>(
    initialState = TouchpadState()
) {

    fun mouseButtonClick(button: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            pcSocketService.mouseClick(button)
        }
    }

    fun mouseMove(x: Int, y: Int) {
        if (x == 0 && y == 0) return

        viewModelScope.launch(Dispatchers.IO) {
            pcSocketService.mouseMove(x, y)
        }
    }

    fun mouseScroll(x: Int, y: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            pcSocketService.mouseScroll(x, y)
        }
    }

    fun toggleGyroscopeMode() {
        setState(TouchpadState(gyroscopeIsEnabled = !state.value!!.gyroscopeIsEnabled))
    }
}
