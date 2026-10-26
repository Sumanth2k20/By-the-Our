package com.bytheour.utils

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class MapNavigator {
    fun openGoogleMaps(latitude: Double, longitude: Double,pinCode:String)
}

expect fun createMapNavigator(): MapNavigator