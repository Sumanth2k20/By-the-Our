package com.bytheour.utils

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bytheour.MainActivity
import com.bytheour.R
import com.bytheour.data.response.GetEphemeraKeyResponse
import com.google.gson.Gson
import com.stripe.android.ApiResultCallback
import com.stripe.android.CustomerSession
import com.stripe.android.EphemeralKeyProvider
import com.stripe.android.EphemeralKeyUpdateListener
import com.stripe.android.PaymentConfiguration
import com.stripe.android.model.PaymentIntent
import com.stripe.android.model.PaymentMethod
import com.stripe.android.model.StripeIntent
import org.json.JSONArray
import org.json.JSONObject

class StripeBillingFragment(val ephemeralKey: GetEphemeraKeyResponse.Data) : BaseStripeFrag(),StripePlatformBridge {
    companion object {
        private const val STRIPE = "stripe"
    }

//    private var clientSecret: String = ""
    private lateinit var paymentMethod: PaymentMethod
//    private var walletAmount = 0.0
//    private var payableAmount = 0.0
//    private var serviceId = ""
//    private var serviceTitle = ""
//    private var serviceCategory = ""
//    private var promocodeAmount = 0.0
    private var cardId = ""
//    private var commissionPercentage = 0.0



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vie = inflater.inflate(R.layout.fragment_stripe_billing, container, false)
        StripeBridgeRegistry.registerBridge(this)
        initCustomerSession(ephemeralKey = ephemeralKey)
        init()
        return vie
    }

     fun init() {
        getWalletBal()
         initializePaymentSession()
    }
    private fun getWalletBal() {
    }

    /**
     * after getting client secret retrive payment intent from Stripe
     */
    private fun retrievePaymentIntent(
        clientSecret: String
    ) {
        Log.e(TAG, "retrievePaymentIntent")

        stripe.retrievePaymentIntent(
            clientSecret,
            callback = object : ApiResultCallback<PaymentIntent> {
                override fun onError(e: Exception) {
                    println("$TAG, onError-> ${e.message}")
                    //liveData.value = Result.failure(e)
                }

                override fun onSuccess(result: PaymentIntent) {
                    processStripeIntent(stripeIntent = result,false,paymentMethod = paymentMethod)
                    // liveData.value = Result.success(result)
                }
            }
        )
    }

    override fun paymentMethodSelect(paymentMethod: String) {
        Log.e(TAG, "paymentMethodSelect-> called")
//        if (isStartPayment)
//            processPayment()
//        binding.tvCardDigit.text = paymentMethod
//        binding.tvPaymentMode.text = getString(R.string.txt_change_payment_mode)
    }

    override fun payWithPaymentMethod(paymentMethod: PaymentMethod) {
        this.paymentMethod = paymentMethod
        val paymentMethod = Gson().toJson(paymentMethod)
        StripeBridgeRegistry.callPaymentIntentAPI(paymentMethod)
    }

    override fun handleGooglePayResult(data: Intent) {

    }

    override fun paymentSucc(stripeIntent: StripeIntent) {
        stripeIntent.id?.let { StripeBridgeRegistry.callCompletePaymentAPI(it, STRIPE) }
    }

    override fun cardAdded() {

    }

    override fun paymentFail(message: String) {
        StripeBridgeRegistry.notifyFailure(message)
//        hideProgress()
//        errorToast(message)
    }

    override fun setCardDetail(paymentMethod: PaymentMethod?) {

        if (paymentMethod?.id != null) {
            cardId = paymentMethod.id ?: ""
//            binding.tvCardDigit.text =
                paymentMethod.card?.brand?.name + "-" + paymentMethod.card?.last4
//            binding.tvPaymentMode.text = getString(R.string.txt_change_payment_mode)
            this.paymentMethod = paymentMethod
            val paymentMethod = Gson().toJson(paymentMethod)
            StripeBridgeRegistry.savePaymentMethod(paymentMethod)
        } else {
//            binding.tvCardDigit.text = ""
//            binding.tvPaymentMode.text = getString(R.string.txt_select_payment_mode)
        }
    }

    override fun initializePaymentSession(
        amountInMinorUnits: Long,
        currency: String
    ) {
        initializePaymentSession()
    }

    override fun selectPaymentMethod() {
        selectPaymentMode()
    }

    override fun processPayment(paymentMethod: String) {
        val data = Gson().fromJson(paymentMethod, PaymentMethod::class.java)
        if (data?.id != null) {
            processPayment(data)
        } else {
            selectPaymentMode()
        }
    }

    override fun reset() {
        TODO("Not yet implemented")
    }

    override fun initCustomerSession(ephemeralKey: GetEphemeraKeyResponse.Data) {
        PaymentConfiguration.Companion.init(activity as MainActivity, PUBLISHABLE_KEY)

        CustomerSession.Companion.initCustomerSession(
            activity?.applicationContext ?: requireContext(),
            object : EphemeralKeyProvider {
                override fun createEphemeralKey(
                    apiVersion: String,
                    keyUpdateListener: EphemeralKeyUpdateListener
                ) {
                    // You already have ephemeralKey from your backend API (common)

                    val jsonArray = JSONArray()
                    for (associatedObject in ephemeralKey.associatedObjects) {
                        val associatedObjectJson = JSONObject()
                        associatedObjectJson.put("id", associatedObject.id)
                        associatedObjectJson.put("type", associatedObject.type)
                        jsonArray.put(associatedObjectJson)
                    }
                    val jsonObject = JSONObject()
                    jsonObject.put("id",ephemeralKey.id)
                    jsonObject.put("object",ephemeralKey.objec)
                    jsonObject.put("associated_objects",jsonArray)
                    jsonObject.put("created",ephemeralKey.created)
                    jsonObject.put("expires",ephemeralKey.expires)
                    jsonObject.put("livemode",ephemeralKey.livemode)
                    jsonObject.put("secret",ephemeralKey.secret)


                    println("SAI----ephemeralKeyData---$jsonObject")



                    keyUpdateListener.onKeyUpdate(jsonObject.toString())
                }
            }
        )
    }

    override fun retrievePaymentIntnt(clientSecret: String) {
        retrievePaymentIntent(clientSecret)
    }

}