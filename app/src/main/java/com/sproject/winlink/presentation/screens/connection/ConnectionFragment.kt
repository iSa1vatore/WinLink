package com.sproject.winlink.presentation.screens.connection

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.sproject.winlink.R
import com.sproject.winlink.databinding.FragmentConnectionBinding
import com.sproject.winlink.databinding.ItemDeviceBinding
import com.sproject.winlink.domain.model.PcInfos
import com.sproject.winlink.presentation.base.BaseFragment
import com.sproject.winlink.presentation.extensions.*
import com.sproject.winlink.presentation.screens.qr_scanner.QrScannerFragment
import com.sproject.winlink.presentation.utils.RecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ConnectionFragment : BaseFragment<FragmentConnectionBinding, ConnectionViewModel>(
    R.layout.fragment_connection
) {

    private var devicesAdapter: RecyclerViewAdapter<ItemDeviceBinding, PcInfos>? = null

    private val args: ConnectionFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val liveData = findNavController()
            .currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<String>(QrScannerFragment.EXTRA_IP_ADDRESS)

        liveData?.observe(viewLifecycleOwner) { address ->
            if (address == null) return@observe

            Log.e("address", address)

            liveData.value = null
        }

        vm.init(args.autoConnect)
    }

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

                vm.connectToPcByIp(addressField.editText?.text.toString())
            }
            refreshButton.setOnClickListener {
                vm.discoverDevicesNearby()
            }
            connectButtonByQr.setOnClickListener {
                navigate(
                    ConnectionFragmentDirections
                        .actionConnectionFragmentToQrScannerFragment()
                )
            }
        }

    override fun setupObservers() {
        vm.state.observe(viewLifecycleOwner) {
            with(binding) {
                when (it) {
                    is ConnectionState.FetchingDevicesNearby -> {
                        devicesAdapter?.items = it.devices

                        progressBarMin.visibleIf(it.devices.isNotEmpty())
                        progressBar.visibleIf(it.devices.isEmpty())

                        hideViews(connectingContainer, refreshButton, noDevicesTextView)
                        showViews(contentContainer)
                    }
                    is ConnectionState.NearbyDevicesLoaded -> {
                        devicesAdapter?.items = it.devices

                        noDevicesTextView.visibleIf(it.devices.isEmpty())

                        hideViews(progressBarMin, progressBar)
                        showViews(refreshButton)
                    }
                    is ConnectionState.ConnectingToPc -> {
                        hideViews(contentContainer, refreshButton)
                        showViews(connectingContainer)
                    }
                }
            }
        }

        lifecycleScope.launch {
            vm.events.collect { event ->
                when (event) {
                    is ConnectionEvent.ConnectedToPc -> {
                        showToast(getString(R.string.connected_to, event.pcInfos.name))
                        navigate(
                            ConnectionFragmentDirections
                                .actionConnectionFragmentToTabsFragment()
                        )
                    }
                    is ConnectionEvent.PcConnectionError -> {
                        showToast(getString(R.string.connection_error))
                    }
                }
            }
        }
    }
}
