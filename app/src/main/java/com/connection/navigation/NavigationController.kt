package com.connection.navigation

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun Fragment.navigate(destination: Destination) {
    when (destination) {
        is Internal -> {
            handleInternalNavigation(destination)
        }
        is External -> {
            // TODO
        }
    }
}

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
    }
}