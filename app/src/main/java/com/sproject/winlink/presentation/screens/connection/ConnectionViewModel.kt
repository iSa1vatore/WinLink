package com.sproject.winlink.presentation.screens.connection

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.sproject.winlink.common.util.NetworkUtils
import com.sproject.winlink.common.util.Resource
import com.sproject.winlink.data.remote.PcApi
import com.sproject.winlink.data.remote.PcSocketService
import com.sproject.winlink.data.remote.mapper.toPcInfos
import com.sproject.winlink.domain.model.PcInfos
import com.sproject.winlink.domain.repository.PcRepository
import com.sproject.winlink.presentation.base.BaseViewModelWithStateAndAction
import com.sproject.winlink.presentation.utils.RecyclerViewAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class ConnectionViewModel @Inject constructor(
    private val pcApi: PcApi,
    private val pcSocketService: PcSocketService,
    private val pcRepository: PcRepository,
    private val networkUtils: NetworkUtils,
) : BaseViewModelWithStateAndAction<ConnectionState, ConnectionEvent>(
    initialState = ConnectionState.FetchingDevicesNearby()
) {

    fun init(autoConnect: Boolean) =
        viewModelScope.launch {
            if (!autoConnect) {
                discoverDevicesNearby()

                return@launch
            }

            pcRepository.getLastConnectedPc().collect {
                when (it) {
                    is Resource.Success -> {
                        if (it.data != null) connectToPc(it.data)
                    }
                    is Resource.Loading -> {
                        Log.e("lol", it.toString())
                    }
                    else -> discoverDevicesNearby()
                }
            }
        }

    fun discoverDevicesNearby() =
        viewModelScope.launch(Dispatchers.IO) {
            setState(ConnectionState.FetchingDevicesNearby(), true)

            var devices = mutableListOf<PcInfos>()

            networkUtils.getClients { ipAddress ->
                pcApi.initSession(ipAddress)

                try {
                    val result = pcApi.getPcInfos()

                    devices = ArrayList(devices)
                    devices.add(result.toPcInfos())

                    setState(
                        ConnectionState.FetchingDevicesNearby(
                            devices = devices.map {
                                RecyclerViewAdapter.Item(it)
                            }
                        ),
                        true
                    )
                } catch (e: Exception) {
                }
            }

            setState(
                ConnectionState.NearbyDevicesLoaded(
                    devices = devices.map {
                        RecyclerViewAdapter.Item(it)
                    }
                ),
                true
            )
        }

    fun connectToPcByIp(ip: String) =
        viewModelScope.launch(Dispatchers.IO) {
            pcApi.initSession(ip)

            pcRepository.getPcInfos().collect {
                when (it) {
                    is Resource.Success -> connectToPc(it.data!!)
                    is Resource.Error -> sendEvent(ConnectionEvent.PcConnectionError)
                    else -> {}
                }
            }
        }

    fun connectToPc(pcInfos: PcInfos) =
        viewModelScope.launch(Dispatchers.IO) {
            setState(ConnectionState.ConnectingToPc(pcInfos), true)

            pcApi.initSession(pcInfos.address, pcInfos.port)

            val createPcWs = pcSocketService.initSession(
                "ws://${pcInfos.address}:${pcInfos.port}"
            )

            when (createPcWs) {
                is Resource.Success -> {
                    pcRepository.saveLastConnectedPc(pcInfos)

                    sendEvent(ConnectionEvent.ConnectedToPc(pcInfos))
                }
                is Resource.Error -> {
                    discoverDevicesNearby()

                    sendEvent(ConnectionEvent.PcConnectionError)
                }
                else -> {}
            }
        }
}
