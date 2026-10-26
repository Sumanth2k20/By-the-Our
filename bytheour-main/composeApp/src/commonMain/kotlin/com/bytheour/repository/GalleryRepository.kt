package com.bytheour.repository

import com.bytheour.core.JTURLS
import com.bytheour.core.JTURLS.token
import com.bytheour.core.JtApiResult
import com.bytheour.core.safeApiCall
import com.bytheour.data.model.response.GalleryPlan
import com.bytheour.data.model.response.ModelAddImage
import com.bytheour.data.model.response.ModelGalleryBookings
import com.bytheour.data.model.response.ModelGalleryPlan
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters

class GalleryRepository(
    private val httpClient: HttpClient
) {
    suspend fun galleryAddImage(
        files: Map<String, String>,
        workerService: String? = null
    ): JtApiResult<ModelAddImage> {
        return safeApiCall(httpClient) {
            httpClient.submitForm(
                url = JTURLS.GALLERY_ADD_IMAGE(),
                formParameters = Parameters.build {
                    files.forEach { (key, value) ->
                        append(key, value)
                    }
                    workerService?.let {
                        append("worker_service", it)
                    }
                },
                encodeInQuery = false
            ) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    suspend fun galleryDeleteImage(
        image: String? = null
    ): JtApiResult<GalleryPlan> {
        return safeApiCall(httpClient) {
            httpClient.submitForm(
                url = JTURLS.GALLERY_DELETE_IMAGE(),
                formParameters = Parameters.build {
                    image?.let { append("image", it) }
                },
                encodeInQuery = false
            ) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    suspend fun galleryBookings(): JtApiResult<ModelGalleryBookings> {
        return safeApiCall(httpClient) {
            httpClient.post(JTURLS.GALLERY_BOOKINGS()) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    suspend fun galleryGetPlans(): JtApiResult<ModelGalleryPlan> {
        return safeApiCall(httpClient) {
            httpClient.post(JTURLS.GALLERY_GET_PLANS()) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }



}