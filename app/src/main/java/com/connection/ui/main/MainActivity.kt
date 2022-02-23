package com.connection.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
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
        // TODO When bottom navigation is added uncomment
        //dataBinding.bottomNavigationMenu.itemIconTintList = null
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
        /*dataBinding
            .bottomNavigationMenu
            .setupWithNavController(navigationController)

        viewModel
            .setBottomNavigationVisibility(
                navigationController,
                dataBinding.bottomNavigationMenu
            )*/
    }
}