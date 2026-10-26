package com.bytheour.presentations.screens.tabs.moreTab.adress

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult
import com.bytheour.data.request.DefaultAddressRequest
import com.bytheour.data.request.ServiceListRequest
import com.bytheour.useCases.GeneralUseCases
import com.bytheour.useCases.SharedPreferenceUseCase
import com.bytheour.useCases.UserUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class JTAddressListViewModel(
    private val userUseCase: UserUseCases,
    private val generalUseCases: GeneralUseCases,
    private val sharedPreferenceUseCase: SharedPreferenceUseCase
) : ViewModel() {

    data class PlacePrediction(
        val description: String,
        val placeId: String,
        val mainText: String,
        val secondaryText: String
    )

    data class LocationData(
        val latitude: Double,
        val longitude: Double,
        val address: String,
        val navigateToMark:Boolean?= false
    )

    data class UIStates(
        val id: String? = null,
        val locationType: String? = null,
        val landmark: String? = null,
        val defaultAddress: Boolean? = false,
        val search: String? = "",
        val lat: String? = null,
        val lag:String? = null,
        val state:String? = null,
        val country:String? = null,
        val zip:String? = null,
        val fullAddress:String? = null,
        val city:String? = null,
        val name:String? = null,

        val notServiceable: Boolean? = false,
    )
    data class AddressListUiState(
        val addressList: List<UIStates> = emptyList(),
        val message:String = "",
        val search: String? = "",
        val paginationOffset: Int = 0,
        val isLoadingMore: Boolean = false,
        val hasMoreData: Boolean = true,
        val selectedLocation: LocationData? = null
    )

    private val _uiStates = MutableStateFlow(AddressListUiState())
    val uiStates = _uiStates.asStateFlow()

    private val _places = mutableStateListOf<PlacePrediction>()
    val places: List<PlacePrediction> get() = _places

    private var currentLimit: String = "10"

    fun updateSearch(search: String) {
        _uiStates.update { it.copy(search = search) }
    }

    fun fetchPlacePredictions(query: String, apiKey: String) {
        viewModelScope.launch {
            try {
                if (query.length >= 5) {
                    val predictions = generalUseCases.getPlacePredictions(query, apiKey)
                    _places.clear()
                    _places.addAll(predictions.mapNotNull { prediction ->
                        prediction.description?.let { desc ->
                            PlacePrediction(
                                description = desc,
                                placeId = prediction.place_id ?: "",
                                mainText = prediction.structured_formatting?.main_text ?: "",
                                secondaryText = prediction.structured_formatting?.secondary_text ?: ""
                            )
                        }
                    })
                } else {
                    _places.clear()
                }
                _uiStates.update { it.copy(message = "") }
            } catch (e: Exception) {
                _uiStates.update { it.copy(message = "Failed To Fetch Place Predictions") }
            }
        }
    }

    fun fetchPlaceDetails(placeId: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val details = generalUseCases.getPlaceDetails(placeId, apiKey)
                details?.let { placeDetails ->
                    placeDetails.result?.geometry?.location?.let { location ->
                        _uiStates.update { currentState ->
                            currentState.copy(
                                selectedLocation = LocationData(
                                    latitude = location.lat,
                                    longitude = location.lng,
                                    address = placeDetails.result.formatted_address ?: "",
                                    navigateToMark = true
                                ),
                                message = ""
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiStates.update { it.copy(message = "Failed to fetch place details") }
            }
        }
    }

    fun clearPredictions() {
        _places.clear()
    }

    fun loadInitialAddressList(limit: String) {
        _uiStates.update {
            it.copy(
                addressList = emptyList(),
                paginationOffset = 0,
                hasMoreData = true
            )
        }
        loadAddressList(limit, 0, true)
    }


    fun loadMoreAddresses(limit: String) {
        val currentState = uiStates.value
        if (!currentState.isLoadingMore && currentState.hasMoreData) {
            loadAddressList(limit, currentState.paginationOffset, false)
        }
    }

    private fun loadAddressList(limit: String, offset: Int, isInitialLoad: Boolean) {
        viewModelScope.launch {
            _uiStates.update { it.copy(isLoadingMore = true) }
            val request = ServiceListRequest(
                limit = limit,
                offset = offset.toString()
            )
            when (val result = userUseCase.getAddressList(request)) {
                is JtApiResult.Success -> {
                    val addresses = result.data.data.map { addressData ->
                        UIStates(
                            locationType = addressData.name,
                            landmark = addressData.address.line1,
                            defaultAddress = addressData.isDefault,
                            notServiceable = !addressData.isEnable,
                            id = addressData.id,
                            state = addressData.address.state,
                            zip = addressData.address.zip,
                            fullAddress = addressData.address.fullAdd,
                            city = addressData.address.city,
                            lag = addressData.lng,
                            lat = addressData.lat
                        )
                    }
                    val defaultAddressItem = addresses.find { it.defaultAddress == true }
                    defaultAddressItem?.let {
                        val fullAddress = listOfNotNull(
                            it.landmark,
                            it.state,
                            it.lag,
                            it.lat
                        ).joinToString(", ")
                        sharedPreferenceUseCase.putDefaultAddress(fullAddress)
                    }

                    val limitInt = limit.toIntOrNull() ?: 0
                    val hasMoreData = addresses.size >= limitInt

                    _uiStates.update { currentState ->
                        val updatedList = if (isInitialLoad) addresses else (currentState.addressList + addresses)
                        val newOffset = if (isInitialLoad) addresses.size else currentState.paginationOffset + addresses.size

                        currentState.copy(
                            addressList = updatedList,
                            paginationOffset = newOffset,
                            isLoadingMore = false,
                            hasMoreData = hasMoreData
                        )
                    }
                }
                is JtApiResult.Failure -> {
                    _uiStates.update {
                        it.copy(message = result.error.message, isLoadingMore = false)
                    }
                }
            }
        }
    }


    fun defaultAddress(id: String) {
        viewModelScope.launch {
            val request = DefaultAddressRequest(id = id)
            when (val result = userUseCase.defaultAddress(request)) {
                is JtApiResult.Success -> {
                    _uiStates.update {
                        it.copy(
                            message = result.data.meta.message?:""
                        )
                    }
                    loadInitialAddressList(currentLimit)
                }
                is JtApiResult.Failure -> {
                    _uiStates.update {
                        it.copy(message = result.error.message)
                    }
                }
            }
        }
    }


    fun deleteAddress(id: String) {
        viewModelScope.launch {
            val request = DefaultAddressRequest(id = id)
            when (val result = userUseCase.deleteAddress(request)) {
                is JtApiResult.Success -> {
                    _uiStates.update {
                        it.copy(
                            message = result.data.meta.message?:""
                        )
                    }
                    loadInitialAddressList(currentLimit)
                }
                is JtApiResult.Failure -> {
                    _uiStates.update {
                        it.copy(message = result.error.message)
                    }
                }
            }
        }
    }

    fun clearMessage() {
        _uiStates.update {
            it.copy(message = "")
        }
    }

}