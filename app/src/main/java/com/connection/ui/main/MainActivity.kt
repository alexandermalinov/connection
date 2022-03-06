package com.connection.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.connection.R
import com.connection.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var navigationController: NavController
    private lateinit var dataBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDataBinding()
        initNavController()
        //dataBinding.bottomNavigationMenu.itemIconTintList = null
        initBottomNavigation()
    }

    override fun onSupportNavigateUp(): Boolean = navigationController.navigateUp()

    private fun initDataBinding() {
        dataBinding = DataBindingUtil.setContentView(
            this@MainActivity,
            R.layout.activity_main
        )
    }

    private fun initNavController() {
        navigationController = Navigation.findNavController(
            this,
            R.id.nav_host_fragment
        )
    }

    private fun initBottomNavigation() {
        dataBinding
            .bottomNavigationMenu
            .apply {
                setupWithNavController(navigationController)
                setOnItemSelectedListener {
                    viewModel.handleBottomNavigation(it)
                }
            }
        viewModel
            .setBottomNavigationVisibility(
                navigationController,
                dataBinding.bottomNavigationMenu
            )
    }
}