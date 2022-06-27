package com.sproject.winlink.presentation.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

abstract class BaseViewModelWithState<S>(
    initialState: S
) : BaseViewModel() {

    private val _state = MutableLiveData(initialState)
    val state = _state

    fun setState(data: S, async: Boolean = false) {

        if (async) {
            _state.postValue(data)
        } else {
            _state.value = data
        }
    }
}

abstract class BaseViewModelWithStateAndAction<S, E>(
    initialState: S
) : BaseViewModelWithState<S>(
    initialState
) {

    private val _eventChannel = Channel<E>()
    val events = _eventChannel.receiveAsFlow()

    suspend fun sendEvent(event: E) {
        _eventChannel.send(event)
    }
}

abstract class BaseViewModel : ViewModel()
