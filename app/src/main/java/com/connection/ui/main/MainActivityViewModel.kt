package com.connection.ui.main

import android.view.View
import androidx.navigation.NavController
import com.connection.R
import com.connection.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor() : BaseViewModel() {

    /*fun setBottomNavigationVisibility(
        navController: NavController,
        view: View
    ) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment -> view.visibility = View.GONE
            }
        }
    }*/
}