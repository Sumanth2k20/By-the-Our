package com.bytheour.useCases

import com.bytheour.core.JtApiResult
import com.bytheour.data.model.response.GalleryPlan
import com.bytheour.data.model.response.ModelAddImage
import com.bytheour.data.model.response.ModelGalleryBookings
import com.bytheour.data.model.response.ModelGalleryPlan
import com.bytheour.repository.GalleryRepository
import com.bytheour.showProgress

class GalleryUseCases(
    private val galleryRepository: GalleryRepository
) {
    suspend fun galleryAddImageUseCase(
        files: Map<String, String>,
        workerService: String? = null
    ): JtApiResult<ModelAddImage> {
        showProgress.value = true
        val result = galleryRepository.galleryAddImage(
            files = files,
            workerService = workerService
        )
        showProgress.value = false
        return result
    }

    suspend fun galleryDeleteImageUseCase(
        image: String? = null
    ): JtApiResult<GalleryPlan> {
        showProgress.value = true
        val result = galleryRepository.galleryDeleteImage(
            image = image
        )
        showProgress.value = false
        return result
    }

    suspend fun getGalleryBookings():JtApiResult<ModelGalleryBookings>{
        showProgress.value = true
        val result = galleryRepository.galleryBookings()
        showProgress.value = false
        return result
    }

    suspend fun getGalleryPlans():JtApiResult<ModelGalleryPlan>{
        showProgress.value = true
        val result = galleryRepository.galleryGetPlans()
        showProgress.value = false
        return result
    }



}