package com.bytheour.utils

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.fragment.app.Fragment
import com.stripe.android.*
import com.stripe.android.model.*
import com.stripe.android.model.PaymentMethod
import com.stripe.android.view.AddPaymentMethodActivityStarter
import com.stripe.android.view.BillingAddressFields
import java.util.*


abstract class BaseStripeFrag : Fragment(),
    PaymentSession.PaymentSessionListener {

    companion object {
        const val CURRENCY = "GBP"
        internal const val TAG = "SAI---BaseStripeFrag"
        private val DEFAULT_SHIPPING_INFO = ShippingInformation(
            Address.Builder()
                .setCity("San Francisco")
                .setCountry("US")
                .setLine1("123 Market St")
                .setLine2("#345")
                .setPostalCode("94107")
                .setState("CA")
                .build(),
            "Fake Name",
            "(555) 555-5555"
        )
    }

//    private var isMethodAdded: Boolean = false
    private var paymentSessionData: PaymentSessionData? = null

    internal val stripe: Stripe by lazy {
        Stripe(requireContext(), PUBLISHABLE_KEY, enableLogging = true)
    }

    private val paymentSession: PaymentSession by lazy {
        Log.e(TAG, "paymentSession-> called")
        PaymentSession(
            this,
            PaymentSessionConfig.Builder()
                .setPrepopulatedShippingInfo(DEFAULT_SHIPPING_INFO)
                .setShippingInformationValidator(ShippingInfoValidator())
                .setShippingMethodsFactory(ShippingMethodsFactory())
                .setBillingAddressFields(BillingAddressFields.PostalCode)
                .setShouldShowGooglePay(false)
                .setShouldPrefetchCustomer(true)
                .build()
        )
    }

    /**
     * set price
     */
    open fun getPrice(): Long {
        Log.e(TAG, "getPrice-> called")
        return -1L
    }

    /**
     * call when user select payment method
     */
    protected abstract fun paymentMethodSelect(paymentMethod: String)

    /**
     * Make call API from UI
     */
    abstract fun payWithPaymentMethod(paymentMethod: PaymentMethod)

    /**
     * handle goodle pay result
     */
    abstract fun handleGooglePayResult(data: Intent)

    /**
     * call this funtion when payment succ
     */
    abstract fun paymentSucc(stripeIntent: StripeIntent)

    /**
     * call this funtion when Add card succ
     */
    abstract fun cardAdded()

    /**
     * call this funtion when display card detail
     */
    abstract fun setCardDetail(paymentMethod: PaymentMethod?)


    /**
     * when payment fail than
     */
    abstract fun paymentFail(message: String)

    fun initializePaymentSession() {
        Log.e(TAG, "initializePaymentSession-> called")
        val selectCustomization = PaymentAuthConfig.Stripe3ds2ButtonCustomization.Builder()
//            .setBackgroundColor("#EC4847")
//            .setTextColor("#000000")
            .build()
        val uiCustomization =
            PaymentAuthConfig.Stripe3ds2UiCustomization.Builder.createWithAppTheme(requireActivity())
                .setButtonCustomization(
                    selectCustomization,
                    PaymentAuthConfig.Stripe3ds2UiCustomization.ButtonType.SELECT
                )
                .build()
        PaymentAuthConfig.init(
            PaymentAuthConfig.Builder()
                .set3ds2Config(
                    PaymentAuthConfig.Stripe3ds2Config.Builder()
                        .setUiCustomization(uiCustomization)
                        .build()
                )
                .build()
        )

        initPaymentSession()
    }

    private fun initPaymentSession() {
        Log.e(TAG, "initPaymentSession-> called")
        paymentSession.init(this)
        paymentSession.setCartTotal(200L)
    }

    override fun onCommunicatingStateChanged(isCommunicating: Boolean) {
        Log.e(TAG, "onCommunicatingStateChanged-> $isCommunicating")
//        if(isCommunicating)
//            showProgress()
//        else
//            hideProgress()
    }

    override fun onError(errorCode: Int, errorMessage: String) {
        Log.e(TAG, "Error-> $errorMessage")
    }

    override fun onPaymentSessionDataChanged(data: PaymentSessionData) {
        Log.e(TAG, "onPaymentSessionDataChanged-> $data")
        onSessionDataChanged(data)
    }

    private fun onSessionDataChanged(data: PaymentSessionData) {
        paymentSessionData = data

        Log.e(TAG, "onPaymentSessionDataChanged $data")
//        if (data.paymentMethod != null) {
//            prefs.paymentMethod = data.paymentMethod
//        }
        setCardDetail(data.paymentMethod)

        data.shippingMethod?.let { shippingMethod ->
            Log.e(TAG, "shippingMethod $shippingMethod")
        }

        paymentSession.setCartTotal(getPrice())

        when {
            data.useGooglePay -> {
            }
            else -> {
                data.paymentMethod?.let { paymentMethod ->
                    Log.e(TAG, "onSessionDataChanged-> called")
                    paymentMethodSelect(getPaymentMethodDescription(paymentMethod))
                }
            }
        }
    }

    private fun getPaymentMethodDescription(paymentMethod: PaymentMethod): String {
        Log.e(TAG, "getPaymentMethodDescription-> called")
        return when (paymentMethod.type) {
            PaymentMethod.Type.Card -> {
                paymentMethod.card?.let {
                    "${getDisplayName(it.brand.displayName)}-${it.last4}"
                }.orEmpty()
            }
            PaymentMethod.Type.Fpx -> {
                paymentMethod.fpx?.let {
                    "${getDisplayName(it.bank)} (FPX)"
                }.orEmpty()
            }
            else -> ""
        }
    }

    private fun getDisplayName(name: String?): String {
        Log.e(TAG, "getDisplayName -> called")
        return name
            .orEmpty()
            .split("_")
            .joinToString(" ") { part ->
                part.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase() else it.toString()
                }
            }
    }


    private fun displayError(errorMessage: String?) {
        errorMessage?.let { paymentFail(it)}
    }

    private class ShippingInfoValidator : PaymentSessionConfig.ShippingInformationValidator {
        override fun getErrorMessage(shippingInformation: ShippingInformation): String {
            return "A US address is required"
        }

        override fun isValid(shippingInformation: ShippingInformation): Boolean {
            return Locale.US.country == shippingInformation.address?.country
        }
    }

    private class ShippingMethodsFactory : PaymentSessionConfig.ShippingMethodsFactory {
        override fun create(shippingInformation: ShippingInformation): List<ShippingMethod> {
            val isCourierSupported = "94110" == shippingInformation.address?.postalCode
            val currency = Currency.getInstance(CURRENCY.uppercase(Locale.ROOT))
            val courierMethod = if (isCourierSupported) {
                ShippingMethod(
                    label = "1 Hour Courier",
                    identifier = "courier",
                    detail = "Arrives in the next hour",
                    amount = 1099,
                    currency = currency
                )
            } else {
                null
            }
            return listOfNotNull(
                ShippingMethod(
                    label = "UPS Ground",
                    identifier = "ups-ground",
                    detail = "Arrives in 3-5 days",
                    amount = 0,
                    currency = currency
                ),
                ShippingMethod(
                    label = "FedEx",
                    identifier = "fedex",
                    detail = "Arrives tomorrow",
                    amount = 599,
                    currency = currency
                ),
                courierMethod
            )
        }
    }

    /**
     * if user want to add payment method
     */
    fun selectPaymentMode() {
        Log.e(TAG, "selectPaymentMode-> called")
        paymentSession.presentPaymentMethodSelection()
    }

    fun addPaymentMethod() {
        Log.e(TAG, "addPaymentMethod-> called")
//        if (paymentSessionData?.paymentMethod == null) {
        val obj =
            AddPaymentMethodActivityStarter.Args.Builder().setShouldAttachToCustomer(true)
                .setPaymentMethodType(PaymentMethod.Type.Card)
                .build()
        AddPaymentMethodActivityStarter(this)
            .startForResult(obj)
//        } else {
//            cardAdded()
//        }
    }

    /**
     * Call this method when user want to pay
     */
    fun processPayment(paymentMethod: PaymentMethod?) {
        Log.e(TAG, "processPayment-> called")
        paymentSessionData?.let {
            createPaymentIntent(it,paymentMethod)
        }
    }

    /**
     * Flow when user pay button press
     */
    private fun createPaymentIntent(paymentSessionData: PaymentSessionData,paymentMethod: PaymentMethod?) {
        Log.e(TAG, "createPaymentIntent $paymentSessionData")
        paymentSessionData.paymentMethod?.let { paymentMethod ->
            payWithPaymentMethod(paymentMethod)
        } ?:paymentMethod?.let {
            payWithPaymentMethod(it)
        } ?: displayError("No payment method selected")
    }

    /**
     * When google pay and session change handle
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e(TAG, "requestCode $requestCode resultCode $resultCode data  ${data?.extras}")
        val isPaymentIntentResult = stripe.onPaymentResult(
            requestCode, data,
            object : ApiResultCallback<PaymentIntentResult> {
                override fun onSuccess(result: PaymentIntentResult) {
//                    hideProgress()
                    processStripeIntent(
                        result.intent,
                        isAfterConfirmation = true,
                        paymentMethod = null
                    )
                }

                override fun onError(e: Exception) {
//                    hideProgress()
                    displayError(e.message)
                }
            })

        if (isPaymentIntentResult) {
//            showProgress()
        } else {
            val isSetupIntentResult = stripe.onSetupResult(requestCode, data,
                object : ApiResultCallback<SetupIntentResult> {
                    override fun onSuccess(result: SetupIntentResult) {
//                        hideProgress()
                        processStripeIntent(
                            result.intent,
                            isAfterConfirmation = true,
                            paymentMethod = null
                        )
                    }

                    override fun onError(e: Exception) {
//                        hideProgress()
                        displayError(e.message)
                    }
                })
            if (!isSetupIntentResult && data != null) {
                paymentSession.handlePaymentData(requestCode, resultCode, data)
            }
        }

        if (requestCode == AddPaymentMethodActivityStarter.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            val result = AddPaymentMethodActivityStarter.Result.fromIntent(data)
//            val paymentMethod = result?.
            cardAdded()
            // use paymentMethod
        }
    }

    /**
     * check payemt from stipe SDK and API
     */
    fun processStripeIntent(
        stripeIntent: StripeIntent,
        isAfterConfirmation: Boolean = false,
        paymentMethod: PaymentMethod?
    ) {
        if (stripeIntent.requiresAction()) {
            stripe.handleNextActionForPayment(this, stripeIntent.clientSecret!!)
        } else if (stripeIntent.requiresConfirmation()) {
            paymentSession.onCompleted()
            paymentSucc(stripeIntent)
        } else if (stripeIntent.status == StripeIntent.Status.Succeeded) {
            if (stripeIntent is PaymentIntent) {
                Log.e(TAG, "paymentSession.onCompleted()")
                paymentSession.onCompleted()
                paymentSucc(stripeIntent)
            } else if (stripeIntent is SetupIntent) {
                Log.e(TAG, "stripeIntent is SetupIntent")
                //finishSetup()
            }
        } else if (stripeIntent.status == StripeIntent.Status.RequiresPaymentMethod) {
            Log.e(TAG, "RequiresPaymentMethod")
            if (isAfterConfirmation) {
                resetCheckout()
            } else {
                if (stripeIntent is PaymentIntent) {
                    Log.e(TAG, "stripe confirmPayment $stripeIntent")
                    stripe.confirmPayment(
                        this,
                        ConfirmPaymentIntentParams.createWithPaymentMethodId(
                            paymentMethodId = paymentMethod?.id.orEmpty(),
                            clientSecret = requireNotNull(stripeIntent.clientSecret)
                        )
                    )
                } else if (stripeIntent is SetupIntent) {
                    Log.e(TAG, "stripe confirmSetupIntent $stripeIntent")
                    stripe.confirmSetupIntent(
                        this,
                        ConfirmSetupIntentParams.create(
                            paymentMethodId = paymentMethod?.id.orEmpty(),
                            clientSecret = requireNotNull(stripeIntent.clientSecret)
                        )
                    )
                }
            }
        } else {
            paymentFail("Unhandled Payment Intent Status: " + stripeIntent.status.toString())
        }
    }

    /**
     * reset stripe if getting error
     */
    private fun resetCheckout() {
        Log.e(TAG, "resetCheckout-> called")
        paymentSessionData = null
        // reset payment method and shipping if authentication fails
        initPaymentSession()
    }
}