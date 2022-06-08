package com.sproject.winlink.presentation.screens

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sproject.winlink.R
import com.sproject.winlink.databinding.ActivityMainBinding
import com.sproject.winlink.presentation.screens.tabs.TabsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by viewBinding(
        createMethod = CreateMethod.INFLATE
    )

    private var navController: NavController? = null

    private val topLevelDestinations = setOf(getTabsDestination(), getConnectionDestination())

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            if (f is TabsFragment || f is NavHostFragment) return
            onNavControllerActivated(f.findNavController())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContentView(binding.root)

        val navController = getRootNavController()
        prepareRootNavController(false, navController)
        onNavControllerActivated(navController)

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)
    }

    private fun getRootNavController(): NavController {
        val navHost = supportFragmentManager.findFragmentById(
            R.id.fragmentContainer
        ) as NavHostFragment

        return navHost.navController
    }

    private fun prepareRootNavController(isConnected: Boolean, navController: NavController) {
        val graph = navController.navInflater.inflate(getMainNavigationGraphId())

        graph.setStartDestination(
            if (isConnected) {
                getTabsDestination()
            } else {
                getConnectionDestination()
            }
        )

        navController.graph = graph
    }

    override fun onBackPressed() {
        if (isStartDestination(navController?.currentDestination)) {
            super.onBackPressed()
        } else {
            navController?.popBackStack()
        }
    }

    override fun onSupportNavigateUp(): Boolean =
        (navController?.navigateUp() ?: false) || super.onSupportNavigateUp()

    private fun isStartDestination(destination: NavDestination?): Boolean {
        if (destination == null) return false
        val graph = destination.parent ?: return false
        val startDestinations = topLevelDestinations + graph.startDestinationId
        return startDestinations.contains(destination.id)
    }

    private fun onNavControllerActivated(navController: NavController) {
        if (this.navController == navController) return

        this.navController = navController
    }

    private fun getMainNavigationGraphId(): Int = R.navigation.main_graph
    private fun getConnectionDestination(): Int = R.id.connectionFragment
    private fun getTabsDestination(): Int = R.id.tabsFragment

    override fun onDestroy() {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
        navController = null
        super.onDestroy()
    }
}
