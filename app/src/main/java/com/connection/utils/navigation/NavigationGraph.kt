package com.connection.utils.navigation

sealed class Destination

object Internal : Destination()
object External : Destination()