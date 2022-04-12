package com.connection.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import com.connection.ui.people.connected.ConnectedPeopleFragment
import com.connection.ui.people.invitation.InvitationsFragment
import com.connection.ui.people.notconnected.NotConnectedPeopleFragment
import com.connection.utils.image.ActivityResultHandler
import com.connection.utils.common.Constants.FRAGMENT_CONNECTED_PEOPLE
import com.connection.utils.common.Constants.FRAGMENT_NOT_CONNECTED_PEOPLE
import com.connection.utils.common.Constants.FRAGMENT_INVITATION_PEOPLE
import java.lang.IllegalArgumentException

/* --------------------------------------------------------------------------------------------
 * Exposed
---------------------------------------------------------------------------------------------*/
fun Fragment.navigate(destination: Destination) {
    when (destination) {
        is Internal -> {
            handleInternalNavigation(destination)
        }
        is External -> {
            handleExternalNavigation(destination)
        }
    }
}

/* --------------------------------------------------------------------------------------------
 * Private
---------------------------------------------------------------------------------------------*/
private fun Fragment.handleInternalNavigation(destination: Internal) {
    when (destination) {
        is NavigationGraph -> {
            findNavController().navigate(
                destination.actionId,
                destination.args,
                destination.navOptions,
                destination.extras
            )
        }
        is PopBackStack -> {
            findNavController().popBackStack()
        }
        is NestedFragmentGraph -> {
            navigateToFragment(destination)
        }
    }
}

private fun Fragment.handleExternalNavigation(destination: External) {
    when (destination) {
        is GalleryNavigation -> {
            if (this is ActivityResultHandler)
                provideObserver(destination).launch()
        }
    }
}

private fun Fragment.navigateToFragment(destination: NestedFragmentGraph) {
    fun selectFragment(id: String) = when (id) {
        FRAGMENT_CONNECTED_PEOPLE -> ConnectedPeopleFragment()
        FRAGMENT_NOT_CONNECTED_PEOPLE -> NotConnectedPeopleFragment()
        FRAGMENT_INVITATION_PEOPLE -> InvitationsFragment()
        else -> throw IllegalArgumentException()
    }
    childFragmentManager.commit {
        setReorderingAllowed(true)
        replace(destination.containerViewId, selectFragment(destination.fragmentId))
    }
}