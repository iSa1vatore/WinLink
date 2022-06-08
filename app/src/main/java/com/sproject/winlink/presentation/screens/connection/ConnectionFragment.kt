package com.sproject.winlink.presentation.screens.connection

import android.Manifest
import android.content.pm.PackageManager
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sproject.winlink.R
import com.sproject.winlink.databinding.FragmentConnectionBinding
import com.sproject.winlink.databinding.ItemDeviceBinding
import com.sproject.winlink.domain.model.PcInfos
import com.sproject.winlink.presentation.base.BaseFragment
import com.sproject.winlink.presentation.extensions.hideKeyboard
import com.sproject.winlink.presentation.utils.RecyclerViewAdapter
import com.sproject.winlink.presentation.extensions.visibleIf
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ConnectionFragment : BaseFragment<FragmentConnectionBinding, ConnectionViewModel>(
    R.layout.fragment_connection
) {
    private lateinit var devicesAdapter: RecyclerViewAdapter<ItemDeviceBinding, PcInfos>

    override fun setupViews() {
        devicesAdapter = RecyclerViewAdapter(
            bindingInflater = ItemDeviceBinding::inflate,
            onBindView = { v, item ->
                v.pcName.text = item.details.name
                v.pcUser.text = item.details.userName

                v.connectButtonByDevice.setOnClickListener {
                    vm.connectToPc(item.details)
                }
            }
        )

        with(binding) {
            devicesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            devicesRecyclerView.adapter = devicesAdapter

            connectingContainer.visibility = View.GONE
        }
    }

    override fun setupListeners() =
        with(binding) {
            connectButton.setOnClickListener {
                hideKeyboard()

                vm.connectToPcByIp(binding.addressField.editText?.text.toString())
            }
            refreshButton.setOnClickListener {
                vm.discoverDevicesNearby()
            }
            connectButtonByQr.setOnClickListener {
                navigate(R.id.action_connectionFragment_to_qrScannerFragment)
            }
        }

    override fun setupObservers() {
        vm.state.observe(viewLifecycleOwner) {
            with(binding) {
                when (it) {
                    is ConnectionState.FetchingDevicesNearby -> {
                        devicesAdapter.items = it.devices

                        contentContainer.visibility = View.VISIBLE
                        connectingContainer.visibility = View.GONE

                        refreshButton.visibility = View.GONE

                        progressBarMin.visibleIf(it.devices.isNotEmpty())
                        progressBar.visibleIf(it.devices.isEmpty())

                        noDevicesTextView.visibility = View.GONE
                    }
                    is ConnectionState.NearbyDevicesLoaded -> {
                        devicesAdapter.items = it.devices

                        refreshButton.visibility = View.VISIBLE

                        progressBarMin.visibility = View.GONE
                        progressBar.visibility = View.GONE

                        noDevicesTextView.visibleIf(it.devices.isEmpty())
                    }
                    is ConnectionState.ConnectingToPc -> {
                        contentContainer.visibility = View.GONE
                        connectingContainer.visibility = View.VISIBLE
                        refreshButton.visibility = View.GONE
                    }
                }
            }
        }

        lifecycleScope.launch {
            vm.events.collect { event ->
                when (event) {
                    is ConnectionEvent.ConnectedToPc -> {
                        showToast(getString(R.string.connected_to, event.pcInfos.name))
                        navigate(R.id.action_connectionFragment_to_tabsFragment)
                    }
                    is ConnectionEvent.PcConnectionError -> {
                        showToast(getString(R.string.connection_error))
                    }
                }
            }
        }
    }
}
