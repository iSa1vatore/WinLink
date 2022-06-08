package com.sproject.winlink.presentation.screens.tabs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sproject.winlink.R
import com.sproject.winlink.databinding.FragmentTabsBinding

class TabsFragment : Fragment(R.layout.fragment_tabs) {
    private val binding: FragmentTabsBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            val navHostFragment = childFragmentManager.findFragmentById(
                tabsContainer.id
            ) as NavHostFragment

            val navController = navHostFragment.navController

            NavigationUI.setupWithNavController(
                bottomNavigationView,
                navController
            )
        }
    }
}
