package com.bytheour.utils

import com.bytheour.data.response.GetEphemeraKeyResponse
import kotlin.concurrent.Volatile

data class SharedPaymentMethod(
    val id: String,
    val type: String,
    val brand: String? = null,
    val last4: String? = null
)

/**
 * Callbacks that the platform (Android or iOS Swift) will implement and register into Kotlin.
 * This Kotlin interface is exposed to Objective-C/Swift by Kotlin/Native — Swift can implement it.
 */
interface PaymentCallbacks {
    // Kotlin -> platform: ask platform to initialize UI/session
    fun onRequestInitialize(amountInMinorUnits: Long, currency: String)

    // Kotlin -> platform: present method selection UI
    fun onRequestPresentPaymentMethods()

    // Kotlin -> platform: present add card UI
    fun onRequestAddPaymentMethod()

    // Kotlin -> platform: start payment flow (platform is responsible for creating client_secret / confirming)
    fun onRequestProcessPayment()

    // Platform -> Kotlin: update communication state (show/hide progress)
    fun onCommunicatingStateChanged(isCommunicating: Boolean)

    // Platform -> Kotlin: user selected a payment method
    fun onPaymentMethodSelected(pm: SharedPaymentMethod?)

    // Platform -> Kotlin: payment completed successfully
    fun onPaymentSuccess(intentId: String?)

    // Platform -> Kotlin: payment failed
    fun onPaymentFailure(errorMessage: String)

    fun callPaymentIntentAPI(paymentMethod: String)

    fun callCompletePaymentAPI(stripeId: String,mode: String)

    fun savePaymentMethod(paymentMethod: String)

    fun savePaymentMethodIOS(paymentMethod: String)
}

/**
 * Registry pattern (volatile) similar to your IosOtpBridgeRegistry.
 */
object StripeBridgeRegistry {
    @Volatile
    var bridge: StripePlatformBridge? = null

    @Volatile
    var callbacks: PaymentCallbacks? = null

    fun registerBridge(b: StripePlatformBridge) {
        bridge = b
    }

    fun unregisterBridge() {
        bridge = null
    }

    fun registerCallbacks(cb: PaymentCallbacks) {
        callbacks = cb
    }

    fun unregisterCallbacks() {
        callbacks = null
    }

    // Convenience for Swift to signal back into Kotlin (no C interop annotations needed)
    fun notifyCommunicating(isOn: Boolean) = callbacks?.onCommunicatingStateChanged(isOn)
    fun notifyMethodSelected(pm: SharedPaymentMethod?) = callbacks?.onPaymentMethodSelected(pm)
    fun notifySuccess(intentId: String?) = callbacks?.onPaymentSuccess(intentId)
    fun notifyFailure(message: String) = callbacks?.onPaymentFailure(message)
    fun savePaymentMethod(paymentMethod: String) = callbacks?.savePaymentMethod(paymentMethod)
    fun savePaymentMethodIOS(paymentMethod: String) = callbacks?.savePaymentMethodIOS(paymentMethod)
    fun callPaymentIntentAPI(paymentMethod: String) = callbacks?.callPaymentIntentAPI(paymentMethod)
    fun callCompletePaymentAPI(stripeId: String,mode: String) = callbacks?.callCompletePaymentAPI(stripeId,mode)
}

/**
 * Platform bridge interface (platform implementations register into registry).
 */
interface StripePlatformBridge {
    fun initializePaymentSession(amountInMinorUnits: Long, currency: String = "GBP")
    fun selectPaymentMethod()
    fun addPaymentMethod()
    fun processPayment(paymentMethod: String)
    fun retrievePaymentIntnt(clientSecret: String)
    fun reset()
    fun initCustomerSession(
        ephemeralKey: GetEphemeraKeyResponse.Data
    )
}