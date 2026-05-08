package com.example.nightsky

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.nightsky.databinding.ActivityMainBinding
import com.example.nightsky.fragments.ExploreFragment
import com.example.nightsky.fragments.LogsFragment
import com.example.nightsky.fragments.MapDataFragment
import com.example.nightsky.fragments.SettingsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val TAG = "MainActivity"

    // Instâncias dos fragments que serão usados na navegação
    private val logsFragment = LogsFragment`()
    private val mapFragment = MapDataFragment`()
    private val exploreFragment = ExploreFragment()
    private val settingsFragment = SettingsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: Initializing NightSky")

        // Inicializando o View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()

        // Define o fragment inicial (Logs) ao abrir o app
        if (savedInstanceState == null) {
            loadFragment(logsFragment)
        }
    }

    private fun setupNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.nav_logs -> { Log.d(TAG, "Nav -> Logs"); logsFragment }
                R.id.nav_map -> { Log.d(TAG, "Nav -> Map"); mapFragment }
                R.id.nav_explore -> { Log.d(TAG, "Nav -> Explore"); exploreFragment }
                R.id.nav_settings -> { Log.d(TAG, "Nav -> Settings"); settingsFragment }
                else -> return@setOnItemSelectedListener false
            }
            loadFragment(fragment)
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: App in foreground")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: App going background")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Cleaning up resources")
    }
}