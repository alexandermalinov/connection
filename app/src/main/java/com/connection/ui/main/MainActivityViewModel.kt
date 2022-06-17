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
                R.id.all_messages_fragment
                        ,R.id.profile_fragment
                        ,R.id.people_fragment
                        ,R.id.feedFragment
                        ,R.id.searchFragment
                        ,R.id.userProfileFragment -> view.makeVisible()
                else -> view.visibility = View.GONE
            }
        }
    }

    /* --------------------------------------------------------------------------------------------
     * Private
    ---------------------------------------------------------------------------------------------*/
    private fun View.makeVisible() {
        visibility = View.VISIBLE
    }
}