package com.agenda.app.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.agenda.app.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Single Activity that hosts the entire navigation graph.
 *
 * All UI screens are Fragments managed by the Navigation Component.
 * This Activity is responsible for:
 * - Hosting the NavHostFragment
 * - Handling deep links from notifications
 * - Managing the edge-to-edge display
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (androidx.core.content.ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }
    }
}
