package com.bytheour.presentations.screens.tabs.servicesTab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult
import com.bytheour.data.model.response.GalleryPlan
import com.bytheour.useCases.CommonPaymentUseCase
import com.bytheour.useCases.GalleryUseCases
import com.bytheour.useCases.SharedPreferenceUseCase
import com.bytheour.utils.PaymentCallbacks
import com.bytheour.utils.SharedPaymentMethod
import com.bytheour.utils.StripeBridgeRegistry
import com.bytheour.utils.StripeUiBridgeRegistry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject

class SelectPlanViewModel(
    private val galleryUseCases: GalleryUseCases,
    private val sharedPreferenceUseCase: SharedPreferenceUseCase,
    private val commonPaymentUseCase: CommonPaymentUseCase,
) : ViewModel() {

   private val nameTag = "SAI----"

    data class UiState(
        val isLoading: Boolean = false,
        val plans: List<GalleryPlan?> = emptyList(),
        val error: String? = null,
        val isErrorMsg: Boolean = false,
        val booking: String? = null,
        val cardNumber: String? = null,
        val navigateBack: Boolean = false,
        val customId: String? = null,
        val noOfPics: String? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiStates = _uiState.asStateFlow()

    init {
        fetchPlans()
    }

    private fun fetchPlans() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = galleryUseCases.getGalleryPlans()) {
                is JtApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            plans = result.data.galleryPlans
                        )
                    }
                    getEphemeralKey()
                }
                is JtApiResult.Failure -> {
                    _uiState.update { it.copy(isLoading = false, error = result.error.message) }
                }
            }
        }
    }

    // Stripe Apis
    fun getEphemeralKey() {
        viewModelScope.launch {
            when (val result = commonPaymentUseCase.getGalleryEphemeralKey()) {
                is JtApiResult.Failure -> {
                    _uiState.update {
                        it.copy(error = result.error.message, isErrorMsg = true)
                    }
                }

                is JtApiResult.Success -> {
                    val ephemeralModel = result.data.data

                    StripeUiBridgeRegistry.bridge?.showStripeBillingScreen(ephemeralModel)

//                    StripeBridgeRegistry.bridge?.initCustomerSession(ephemeralModel)
                    attachStripePlatformListeners()
                }

            }
        }
    }

    fun attachStripePlatformListeners(){
// Register a default handler to receive events back from platform.
        StripeBridgeRegistry.registerCallbacks(object : PaymentCallbacks {
            override fun onRequestInitialize(amountInMinorUnits: Long, currency: String) {
                println("$nameTag onRequestInitialize--$amountInMinorUnits-$currency")
            }
            override fun onRequestPresentPaymentMethods() {
                println("$nameTag onRequestPresentPaymentMethods--")

            }
            override fun onRequestAddPaymentMethod() {
                println("$nameTag onRequestAddPaymentMethod---")

            }
            override fun onRequestProcessPayment() {
                println("$nameTag onRequestProcessPayment--")

            }

            override fun onCommunicatingStateChanged(isCommunicating: Boolean) {
                println("$nameTag onCommunicatingStateChanged--$isCommunicating")
                //  _ui.value = _ui.value.copy(isCommunicating = isCommunicating)
            }
            override fun onPaymentMethodSelected(pm: SharedPaymentMethod?) {
                println("$nameTag onPaymentMethodSelected--$pm")
                StripeBridgeRegistry.bridge?.selectPaymentMethod()
                //  _ui.value = _ui.value.copy(selectedMethod = pm)
            }
            override fun onPaymentSuccess(intentId: String?) {
                println("$nameTag onPaymentSuccess--$intentId")
                //  intentId?.let {  completePayment(bookingId = bookingId, stripeId = it) }
                // _ui.value = _ui.value.copy(paymentSucceeded = true)
            }
            override fun onPaymentFailure(errorMessage: String) {
                println("$nameTag onPaymentFailure--$errorMessage")
                // _ui.value = _ui.value.copy(lastError = errorMessage)
            }

            override fun callPaymentIntentAPI(paymentMethod: String) {
                sharedPreferenceUseCase.putPaymentMethod(paymentMethod)
                makePayment()

            }
            override fun callCompletePaymentAPI(stripeId: String,mode: String) {
                completePayment(stripeId)
            }

            override fun savePaymentMethod(paymentMethod: String) {
                val json = Json.parseToJsonElement(paymentMethod)
                val cardJson = json.jsonObject["card"]
//                cardJson?.jsonObject?.get("expiryYear")
//                cardJson?.jsonObject?.get("expiryMonth")
                val last4 = cardJson?.jsonObject?.get("last4").toString().replace("\"","")
                val brand = cardJson?.jsonObject?.get("brand").toString().replace("\"","")
                _uiState.update {
                    it.copy(cardNumber = "$brand-$last4")
                }
                sharedPreferenceUseCase.putPaymentMethod(paymentMethod)

            }

            override fun savePaymentMethodIOS(paymentMethod: String) {
                _uiState.update {
                    it.copy(cardNumber = paymentMethod.replace(" ","-"))
                }
            }
        })
    }

    fun makePayment() {
        viewModelScope.launch {
            when (val result = commonPaymentUseCase.galleryPaymentIntent(planId = uiStates.value.customId ?: "", noOfPics = uiStates.value.noOfPics ?: "")) {
                is JtApiResult.Failure -> {
                    _uiState.update {
                        it.copy(error = result.error.message, isErrorMsg = true)
                    }
                }

                is JtApiResult.Success -> {

                    val paymentResult = result.data.data

                    println("$nameTag $paymentResult")

                    if (result.data.meta.bookingCompleted) {
                        _uiState.update {
                            it.copy(error = result.data.meta.message, isErrorMsg = false)
                        }
                    } else {
                        _uiState.update {
                            it.copy(booking = result.data.data.booking)
                        }
                        onRetrievePaymentIntent(paymentResult.id)
                    }
                }

            }
        }
    }

    fun completePayment(stripeId: String) {
        viewModelScope.launch {
            when (val result = commonPaymentUseCase.galleryCompletePayment(uiStates.value.booking ?: "", payment = stripeId)) {
                is JtApiResult.Failure -> {
                    _uiState.update {
                        it.copy(error = result.error.message, isErrorMsg = true)
                    }
                }

                is JtApiResult.Success -> {

                    _uiState.update {
                        it.copy(error = result.data.meta.message, isErrorMsg = false, navigateBack = true)
                    }

                    println("$nameTag ${result.data}")
                }

            }
        }
    }

    fun onRetrievePaymentIntent(clientSecret: String) = StripeBridgeRegistry.bridge?.retrievePaymentIntnt(clientSecret)
    fun onPayNow() = StripeBridgeRegistry.bridge?.processPayment(sharedPreferenceUseCase.getPaymentMethod())
    fun onSelectCard() = StripeBridgeRegistry.bridge?.selectPaymentMethod()

    fun onScreenExit() {
        StripeBridgeRegistry.unregisterCallbacks()
    }



    fun updatedSelectedPlan(plan: GalleryPlan?, customPictures: Int) {
        _uiState.update {
            it.copy(
                customId = if (customPictures>0 && plan==null) "C6PsW3KrjBmM" else plan?.customId,
                noOfPics = if (customPictures>0 && plan==null) customPictures.toString() else plan?.pictures.toString()
            )
        }
    }
}