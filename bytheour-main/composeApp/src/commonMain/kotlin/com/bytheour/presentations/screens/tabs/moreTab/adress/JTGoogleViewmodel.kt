package com.bytheour.presentations.screens.tabs.moreTab.adress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jordond.compass.Coordinates
import dev.jordond.compass.geocoder.MobileGeocoder
import dev.jordond.compass.geocoder.placeOrNull
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class JTGoogleViewmodel:ViewModel() {
    data class MapsUiState(
        val latitude: Double = 0.0,
        val longitude: Double = 0.0,
        val address: String? = null,
        val flatNo: String? = null,
        val landmark: String? = null,
        val pinCode: String? = null,
        val country: String? = null,
        val isLocationFetch: Boolean = false,
        val errorMessage: String? = null,
        val city: String? = null,
        val state: String? = null,
        val street: String? = null,

        )

    private val _uiState = MutableStateFlow(MapsUiState())
    val uiState: StateFlow<MapsUiState> = _uiState.asStateFlow()


    fun updateCoordinatesToString(latitude: String, longitude: String) {
        val latDouble = latitude.toDoubleOrNull() ?: 0.0
        val lngDouble = longitude.toDoubleOrNull() ?: 0.0
        updateCoordinates(latDouble, lngDouble)
    }

    fun updateCoordinates(latitude: Double, longitude: Double) {
        _uiState.update { state ->
            state.copy(
                latitude = latitude,
                longitude = longitude,
                isLocationFetch = true
            )
        }
        reverseGeocode(latitude, longitude)
    }

    private fun reverseGeocode(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                val place = MobileGeocoder().placeOrNull(Coordinates(latitude, longitude))

                _uiState.update { state ->
                    state.copy(
                        address = place?.street?.let {
                            listOfNotNull(
                                place.subLocality,   //  colony / locality
                                place.thoroughfare,    // optional if you add street in MapsUiState
                            ).joinToString(", ")
                        } ?: "",
                        landmark = place?.subLocality ?: place?.locality,
                        pinCode = place?.postalCode,
                        country = place?.country,
                        isLocationFetch = false,
                        city = place?.locality,
                        state = place?.administrativeArea,
                        street = place?.thoroughfare,  // actual road/street name
                    )
                }
            } catch (e: Exception) {
                _uiState.update { state ->
                    state.copy(
                        errorMessage = "Failed To Fetch Address: ${e.message}",
                        isLocationFetch = false
                    )
                }
            }
        }
    }

    fun updateFullAddress(flatNo: String?, address: String?, landmark: String?, pinCode: String?, country: String?,latitude: Double?,longitude: Double?,
                          city: String? = null, states: String? = null) {
        _uiState.update { state ->
            state.copy(
                flatNo = flatNo,
                address = address?.let { if (pinCode != null) "$it, $pinCode" else it },
                landmark = landmark,
                pinCode = pinCode,
                country = country,
                isLocationFetch = false, latitude = latitude?:0.0,longitude = longitude?:0.0,
                city = city,
                state = states,
            )
        }
    }

}