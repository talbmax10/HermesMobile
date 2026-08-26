package com.hermesmobile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.hermesmobile.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the Navigation Component
        navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.agentFragment,
                R.id.projectsFragment,
                R.id.filesFragment,
                R.id.terminalFragment,
                R.id.skillsFragment,
                R.id.buildsFragment,
                R.id.modelsFragment,
                R.id.settingsFragment
            )
        )
        // Setup ActionBar with NavController (optional, we have Toolbar in each fragment)
        // setupActionBarWithNavController(navController, appBarConfiguration)

        // Setup BottomNavigationView with NavController
        binding.bottomNavigation.setupWithNavController(navController)
    }
}