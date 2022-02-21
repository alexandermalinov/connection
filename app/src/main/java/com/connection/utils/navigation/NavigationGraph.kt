package com.connection.utils.navigation

import android.os.Bundle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator

sealed class Destination

open class Internal : Destination()
open class External : Destination()

data class NavigationGraph(
    val actionId: Int,
    val args: Bundle? = null,
    val navOptions: NavOptions? = null,
    val extras: FragmentNavigator.Extras? = null
) : Internal()

object PopBackStack : Internal()