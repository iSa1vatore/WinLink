package com.sproject.winlink.presentation.tabs.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sproject.winlink.R
import com.sproject.winlink.databinding.FragmentSettingsBinding
import com.sproject.winlink.presentation.findTopNavController

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val binding: FragmentSettingsBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonDisconnect.setOnClickListener {
            findTopNavController()
                .navigate(R.id.action_tabsFragment_to_connectionFragment)
        }
    }
}
