package com.sproject.winlink.presentation.connection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class ConnectionViewModel @Inject constructor() : ViewModel() {

    fun discoverDevicesNearby() {
        viewModelScope.launch(Dispatchers.IO) {
        }
    }
}
