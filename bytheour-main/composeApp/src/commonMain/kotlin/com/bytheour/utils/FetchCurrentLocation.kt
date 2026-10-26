package com.bytheour.utils

import dev.jordond.compass.geocoder.MobileGeocoder
import dev.jordond.compass.geocoder.placeOrNull
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.GeolocatorResult

suspend fun fetchLocationDetails(
    geoLocator: Geolocator,
    onSuccess: (
        flatNo: String?,
        address: String?,
        landmark: String?,
        pinCode: String?,
        country: String?,
        latitude: Double?,
        longitude: Double?
    ) -> Unit,
    onFailure: (message: String?) -> Unit
) {
    try {
        if (!geoLocator.isAvailable()) {
            onFailure("Device location service unavailable")
            return
        }

        when (val result = geoLocator.current()) {
            is GeolocatorResult.Success -> {
                val coordinates = result.data.coordinates

                val place = MobileGeocoder().placeOrNull(coordinates)

                val flatNo = place?.name ?: place?.locality
                val address = "${place?.locality}, ${place?.administrativeArea}"
                val landmark = place?.subLocality ?: place?.locality
                val pinCode = place?.postalCode
                val country = place?.country





                onSuccess(flatNo, address, landmark, pinCode,country,coordinates.latitude,coordinates.longitude)
            }

            is GeolocatorResult.Error -> {
                onFailure( result.message)
            }
        }

    } catch (e: Exception) {
        onFailure(e.message)
    }
}



