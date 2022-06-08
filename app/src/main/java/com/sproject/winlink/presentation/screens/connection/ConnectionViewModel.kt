package com.sproject.winlink.presentation.screens.connection

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sproject.winlink.common.util.Resource
import com.sproject.winlink.domain.model.PcInfos
import com.sproject.winlink.domain.use_case.ConnectToPcUseCase
import com.sproject.winlink.domain.use_case.GetDevicesNearbyUseCase
import com.sproject.winlink.presentation.base.BaseViewModel
import com.sproject.winlink.presentation.utils.RecyclerViewAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ConnectionViewModel @Inject constructor(
    private val getDevicesNearbyUseCase: GetDevicesNearbyUseCase,
    private val connectToPcUseCase: ConnectToPcUseCase
) : BaseViewModel() {

    private val _state = MutableLiveData<ConnectionState>(
        ConnectionState.FetchingDevicesNearby()
    )
    val state = _state

    private val eventChannel = Channel<ConnectionEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        discoverDevicesNearby()
    }

    fun discoverDevicesNearby() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.postValue(ConnectionState.FetchingDevicesNearby())

            getDevicesNearbyUseCase { it ->
                when (it) {
                    is Resource.Success -> _state.postValue(
                        ConnectionState.NearbyDevicesLoaded(devices = it.data!!.map {
                            RecyclerViewAdapter.Item(it)
                        })
                    )
                    is Resource.Loading -> _state.postValue(
                        ConnectionState.FetchingDevicesNearby(devices = it.data!!.map {
                            RecyclerViewAdapter.Item(it)
                        })
                    )
                    else -> {}
                }
            }
        }
    }

    fun connectToPcByIp(ip: String) {
        println(ip)
    }

    fun connectToPc(pc: PcInfos) {
        _state.value = ConnectionState.ConnectingToPc(pc)

        viewModelScope.launch(Dispatchers.IO) {
            when (connectToPcUseCase(pc)) {
                is Resource.Success -> eventChannel.send(ConnectionEvent.ConnectedToPc(pc))
                is Resource.Error -> {
                    discoverDevicesNearby()

                    eventChannel.send(ConnectionEvent.PcConnectionError)
                }
                else -> {}
            }
        }
    }
}