package com.sproject.winlink.presentation.tabs.touchpad

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sproject.winlink.common.util.Resource
import com.sproject.winlink.data.remote.PcSocketService
import com.sproject.winlink.domain.use_case.MouseClickUseCase
import com.sproject.winlink.domain.use_case.MouseMoveUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class TouchpadViewModel @Inject constructor(
    private val pcSocketService: PcSocketService,
    private val mouseMoveUseCase: MouseMoveUseCase,
    private val mouseClickUseCase: MouseClickUseCase,
) : ViewModel() {

    private var prevMoveX = 0
    private var prevMoveY = 0

    fun connectToPc() {
        viewModelScope.launch(Dispatchers.IO) {
            val url = "ws://192.168.1.39:15110"

            when (pcSocketService.initSession(url)) {
                is Resource.Success -> {
                    Log.i("socket", "connection success")
                }
                is Resource.Error -> {
                    Log.e("socket", "connection error")
                }
                else -> {}
            }
        }
    }

    fun resetPrevMousePosition() {
        prevMoveX = 0
        prevMoveY = 0
    }

    private fun setPrevMousePosition(x: Int, y: Int) {
        prevMoveX = x
        prevMoveY = y
    }

    fun mouseMove(x: Int, y: Int) {
        if (prevMoveY == 0) {
            return setPrevMousePosition(x, y)
        }

        val moveOnX = x - prevMoveX
        val moveOnY = y - prevMoveY

        setPrevMousePosition(x, y)

        viewModelScope.launch(Dispatchers.IO) {
            mouseMoveUseCase(moveOnX, moveOnY)
        }
    }

    private fun disconnect() {
        viewModelScope.launch(Dispatchers.IO) {
            pcSocketService.closeSession()
        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }
}
