package com.bytheour.presentations.screens.tabs.moreTab.adress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult.Failure
import com.bytheour.core.JtApiResult.Success
import com.bytheour.data.request.AddAddressRequest
import com.bytheour.useCases.CommonUseCases
import com.bytheour.useCases.UserUseCases
import com.bytheour.utils.LocationType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class JTSelectLocationViewModel(
    private val userUseCase: UserUseCases,
    private val commonUseCases:CommonUseCases
):ViewModel() {
    data class UIState(
        val address: String?= "",
        val addressErrorMessage: String?= "",
        val apartmentErrorMessage: String?= "",
        val directionErrorMessage: String?= "",
        val stateErrorMessage: String?= "",
        val cityErrorMessage: String?= "",
        val zipErrorMessage: String?= "",
        val typeErrorMessage: String?= "",
        val otherTypeErrorMessage: String?= "",
        val search: String? = "",
        val state :String? = null,
        val city :String? = null,
        val zipCode :String? = null,
        val latitude :String? = null,
        val longitude :String?= null,
        val message: String = "",
        val isNavigate: Boolean = false,
        val webUrl: String? = null,
        val otherType:String? = null
    )

    private val _uiState = MutableStateFlow(UIState())
    val uiStates = _uiState.asStateFlow()

    fun updateSearch(search: String){
        _uiState.update {
            it.copy(search = search)
        }
    }

    fun updateAddress(address: String) {
        _uiState.update { state ->
            state.copy(address = address, addressErrorMessage = null)
        }
    }

    fun updateState(states: String) {
        _uiState.update { state ->
            state.copy(state = states, stateErrorMessage = null)
        }
    }
    fun updateCity(city: String) {
        _uiState.update { state ->
            state.copy(city = city, cityErrorMessage = null)
        }
    }
    fun updateZipCode(zipCode: String) {
        _uiState.update { state ->
            state.copy(zipCode = zipCode , zipErrorMessage = null)
        }
    }

    fun updateLatitude(latitude: String) {
        _uiState.update { state ->
            state.copy(latitude = latitude)
        }
    }

    fun updateLongitude(longitude: String) {
        _uiState.update { state ->
            state.copy(longitude = longitude)
        }
    }

    fun validateLocationForm(selectedType: String, customName: String): Boolean {
        val ui = _uiState.value

        var isValid = true

        // Address validation
        val addressError: String? = when {
            ui.address.isNullOrBlank() -> {
                isValid = false
                "Please enter your address"
            }
            else -> null
        }

        // State validation
        val stateError: String? = when {
            ui.state.isNullOrBlank() -> {
                isValid = false
                "Please enter your State"
            }
            else -> null
        }

        // City validation
        val cityError: String? = when {
            ui.city.isNullOrBlank() -> {
                isValid = false
                "Please enter your City"
            }
            else -> null
        }

        // Zip validation
        val zipError: String? = when {
            ui.zipCode.isNullOrBlank() -> {
                isValid = false
                "Please enter your ZIP Code"
            }
            else -> null
        }

        // Type validation
        val typeError: String? = when {
            selectedType.isEmpty() -> {
                isValid = false
                "Please select a location type"
            }
            else -> null
        }

        // Other type validation (if selected type = OTHER)
        val otherTypeError: String? = when {
            selectedType == LocationType.OTHER.type && customName.isBlank() -> {
                isValid = false
                "Please enter custom type"
            }
            else -> null
        }

        // Update UI state with error messages
        _uiState.update {
            it.copy(
                addressErrorMessage = addressError,
                stateErrorMessage = stateError,
                cityErrorMessage = cityError,
                zipErrorMessage = zipError,
                typeErrorMessage = typeError,
                otherTypeErrorMessage = otherTypeError
            )
        }

        return isValid
    }

    fun cleartypeError(){
        _uiState.update {
            it.copy(
                typeErrorMessage = null
            )
        }
    }
    fun clearOtherTypeError(){
        _uiState.update {
            it.copy(
                otherTypeErrorMessage = null
            )
        }
    }

     fun addAddress(
        house: String,
        name: String,
        addressLine :String,
        state :String,
        city :String,
        zipCode :String,
        latitude :String,
        longitude :String,
        id :String,
        onSuccessful:()->Unit = {}
    ){
         viewModelScope.launch {
             val request = AddAddressRequest(
                 name = name,
                 house = house,
                 addressLine1 = addressLine,
                 state = state,
                 city = city,
                 zipCode = zipCode,
                 latitude = latitude,
                 longitude = longitude,
                 id = id,
             )
             when(val result = userUseCase.addAddressUseCase(request)){
                 is Success ->{
                     val data = result.data?.data
                     _uiState.update {
                         it.copy(
                             address = data?.address?.line1,
                             latitude = data?.lat,
                             longitude = data?.lng,
                             state = data?.address?.state,
                             city = data?.address?.city,
                             zipCode = data?.address?.zip,
                             isNavigate = true,
                             message = result.data?.meta?.message?:""
                         )
                     }
                     onSuccessful()
                 }
                 is Failure -> {
                     _uiState.update {
                         it.copy(
                             message = result.error.message,
                             isNavigate = false
                         )
                     }
                 }
             }
         }
    }

    fun updateNavigation(isNavigate: Boolean){
        _uiState.update {
            it.copy(
                isNavigate = isNavigate
            )
        }
    }
    fun verifyAccountWithStripe(){
        viewModelScope.launch {
            when(val result = commonUseCases.verifyAccountWithStripe()) {
                is Success -> {
                    val response = result.data.data
                    if (response.status=="verified") {
                        _uiState.update {
                            it.copy(
                                message = "Your account has been Verified Successfully"
                            )
                        }
                    }else if (response.url != null){
                        _uiState.update {
                            it.copy(
                                webUrl = response.url
                            )
                        }
                    }else{
                        _uiState.update {
                            it.copy(
                                message = "Account couldn't be verified. Please try again"
                            )
                        }
                    }
                }

                is Failure -> {
                    _uiState.update {
                        it.copy(
                            message = "Account couldn't be verified. Please try again"
                        )
                    }
                }
            }
        }
    }

    fun updateWebUrl(){
        _uiState.update {
            it.copy(
                webUrl = null
            )
        }
    }
}