package com.sproject.winlink

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sproject.winlink.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by viewBinding(
        createMethod = CreateMethod.INFLATE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding.apply {
            setContentView(root)

            val navHostFragment = supportFragmentManager
                .findFragmentById(tabsContainer.id) as NavHostFragment

            val navController = navHostFragment.navController

            NavigationUI.setupWithNavController(
                bottomNavigationView,
                navController
            )
        }
    }
}
