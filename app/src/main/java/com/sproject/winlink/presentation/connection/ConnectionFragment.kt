package com.sproject.winlink.presentation.connection

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sproject.winlink.R
import com.sproject.winlink.databinding.FragmentConnectionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConnectionFragment : Fragment(R.layout.fragment_connection) {

    private val binding: FragmentConnectionBinding by viewBinding()
    private val vm: ConnectionViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.connectButton.setOnClickListener {
            findNavController().navigate(R.id.action_connectionFragment_to_tabsFragment)
        }

        vm.discoverDevicesNearby()

        binding.connectingContainer.visibility = View.GONE
    }
}
