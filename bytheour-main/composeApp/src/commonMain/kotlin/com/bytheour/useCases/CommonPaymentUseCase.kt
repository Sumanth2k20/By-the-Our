package com.bytheour.useCases

import com.bytheour.core.JtApiResult
import com.bytheour.data.response.BankAccountStripConnectDeleteResponse
import com.bytheour.data.response.BankDetailsStripeManageResponse
import com.bytheour.data.response.CommonResponse
import com.bytheour.data.response.GetCardsResponse
import com.bytheour.data.response.GetEphemeraKeyResponse
import com.bytheour.data.response.ModelPaymentIntent
import com.bytheour.repository.CommonPaymentRepository
import com.bytheour.repository.StripeRepository
import com.bytheour.showProgress
import com.bytheour.utils.STRIPE_VERSION

class CommonPaymentUseCase(
    private val commonPaymentRepository: CommonPaymentRepository,
    private val stripeRepository: StripeRepository
) {
    suspend fun getCards(): JtApiResult<GetCardsResponse> {
        showProgress.value = true
        val result = commonPaymentRepository.getCards()
        showProgress.value = false
        return result
    }


    suspend fun getEphemeralKey(): JtApiResult<GetEphemeraKeyResponse> {
        showProgress.value = true
        val result = stripeRepository.getEphemeralKey(STRIPE_VERSION)
        showProgress.value = false
        return result
    }

    suspend fun makePayment(bookingId: String, useWalletAmount: String, promoCode: String?): JtApiResult<ModelPaymentIntent> {
        showProgress.value = true
        val result = stripeRepository.makePayment(bookingId,useWalletAmount,promoCode)
        showProgress.value = false
        return result
    }

    suspend fun completePayment(bookingId: String, mode: String, payment: String?): JtApiResult<CommonResponse> {
        showProgress.value = true
        val result = stripeRepository.completePayment(bookingId,mode,payment)
        showProgress.value = false
        return result
    }

    suspend fun getGalleryEphemeralKey(): JtApiResult<GetEphemeraKeyResponse> {
        showProgress.value = true
        val result = stripeRepository.getGalleryEphemeralKey(STRIPE_VERSION)
        showProgress.value = false
        return result
    }

    suspend fun galleryPaymentIntent(planId: String, noOfPics: String): JtApiResult<ModelPaymentIntent> {
        showProgress.value = true
        val result = stripeRepository.galleryPaymentIntent(planId,noOfPics)
        showProgress.value = false
        return result
    }

    suspend fun galleryCompletePayment(bookingId: String, payment: String?): JtApiResult<CommonResponse> {
        showProgress.value = true
        val result = stripeRepository.galleryCompletePayment(bookingId,payment)
        showProgress.value = false
        return result
    }


    suspend fun getBankDetailsStripeConnect():JtApiResult<BankDetailsStripeManageResponse>{
        showProgress.value = true
        val result = stripeRepository.getBankDetailsStripeConnect()
        showProgress.value = false
        return result
    }

    suspend fun deleteBankDetailsStripeConnect():JtApiResult<BankAccountStripConnectDeleteResponse>{
        showProgress.value = true
        val result = stripeRepository.deleteBankDetailsStripeConnect()
        showProgress.value = false
        return result
    }

}