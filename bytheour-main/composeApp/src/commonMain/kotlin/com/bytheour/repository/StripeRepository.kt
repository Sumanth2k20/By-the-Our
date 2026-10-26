package com.bytheour.repository

import com.bytheour.core.JTURLS
import com.bytheour.core.JTURLS.token
import com.bytheour.core.JtApiResult
import com.bytheour.core.safeApiCall
import com.bytheour.data.response.BankAccountStripConnectDeleteResponse
import com.bytheour.data.response.BankDetailsStripeManageResponse
import com.bytheour.data.response.CommonResponse
import com.bytheour.data.response.GetEphemeraKeyResponse
import com.bytheour.data.response.ModelPaymentIntent
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.headers
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.Parameters

class StripeRepository(private val httpClient: HttpClient) {

    suspend fun getEphemeralKey(stripeVersion: String): JtApiResult<GetEphemeraKeyResponse> {
        return safeApiCall(httpClient) {
            httpClient.submitForm(
                url = JTURLS.ephemeralKey(),
                formParameters = Parameters.build {
                    append("stripe_version", stripeVersion)
                },
                encodeInQuery = false
            ) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    suspend fun getGalleryEphemeralKey(stripeVersion: String): JtApiResult<GetEphemeraKeyResponse> {
        return safeApiCall(httpClient) {
            httpClient.submitForm(
                url = JTURLS.GALLERY_EPHEMERAL_KEY(),
                formParameters = Parameters.build {
                    append("stripe_version", stripeVersion)
                },
                encodeInQuery = false
            ) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    suspend fun galleryPaymentIntent(planId: String, noOfPics: String): JtApiResult<ModelPaymentIntent> {
        return safeApiCall(httpClient) {
            httpClient.submitForm(
                url = JTURLS.GALLERY_PAYMENT_INTENT(),
                formParameters = Parameters.build {
                    append("plan_id", planId)
                    append("no_of_pictures", noOfPics)
                },
                encodeInQuery = false
            ) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }
    suspend fun completePayment(bookingId: String, mode: String, payment: String?): JtApiResult<CommonResponse> {
        return safeApiCall(httpClient) {
            httpClient.submitForm(
                url = JTURLS.completePayment(),
                formParameters = Parameters.build {
                    append("booking", bookingId)
                    append("mode", mode)
                    payment?.let {append("payment", payment)}
                },
                encodeInQuery = false
            ) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    suspend fun makePayment(bookingId: String, useWalletAmount: String, promoCode: String?): JtApiResult<ModelPaymentIntent> {
        return safeApiCall(httpClient) {
            httpClient.submitForm(
                url = JTURLS.doPayment(),
                formParameters = Parameters.build {
                    append("booking", bookingId)
                    append("use_wallet_amount", useWalletAmount)
                    promoCode?.let {append("promo_code", promoCode)}
                },
                encodeInQuery = false
            ) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    suspend fun galleryCompletePayment(bookingId: String, payment: String?): JtApiResult<CommonResponse> {
        return safeApiCall(httpClient) {
            httpClient.submitForm(
                url = JTURLS.GALLERY_COMPLETE_PAYMENT(),
                formParameters = Parameters.build {
                    append("booking", bookingId)
                    payment?.let {append("payment", payment)}
                },
                encodeInQuery = false
            ) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }


    suspend fun getBankDetailsStripeConnect():JtApiResult<BankDetailsStripeManageResponse>{
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.bankDetailsStripeConnect())
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }


    suspend fun deleteBankDetailsStripeConnect():JtApiResult<BankAccountStripConnectDeleteResponse>{
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.removeBankDetailsStripeConnect())
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }



}