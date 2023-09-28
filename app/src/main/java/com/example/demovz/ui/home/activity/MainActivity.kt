package com.example.demovz.ui.home.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.demovz.R
import com.example.demovz.databinding.ActivityMainBinding
import com.example.demovz.ui.home.viewmodel.DeviceViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private val viewModel: DeviceViewModel by  viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        viewModel.getDevices().observe(this) {
            if (it.isEmpty())
                viewModel.setDeviceData()
        }

        val navView: BottomNavigationView? = binding?.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView?.setupWithNavController(navController)

    }
}
