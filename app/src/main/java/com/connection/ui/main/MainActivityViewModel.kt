package com.connection.ui.main

import android.view.View
import androidx.navigation.NavController
import com.connection.R
import com.connection.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor() : BaseViewModel() {

    /* --------------------------------------------------------------------------------------------
     * Exposed
    ---------------------------------------------------------------------------------------------*/
    fun setBottomNavigationVisibility(
        navController: NavController,
        view: View
    ) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.all_messages_fragment -> view.visibility = View.VISIBLE
                R.id.profile_fragment -> view.visibility = View.VISIBLE
                R.id.people_fragment -> view.visibility = View.VISIBLE
                else -> view.visibility = View.GONE
            }
        }
    }
}